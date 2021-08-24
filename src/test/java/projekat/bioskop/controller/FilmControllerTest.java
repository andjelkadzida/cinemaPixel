package projekat.bioskop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Sediste;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.repository.SedisteRepository;
import projekat.bioskop.services.FilmService;

@ContextConfiguration(classes = {FilmController.class})
@ExtendWith(SpringExtension.class)
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;

    @MockBean
    private FilmService filmService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private KorisnikRepository korisnikRepository;

    @MockBean
    private ProjekcijaRepository projekcijaRepository;

    @MockBean
    private RezervacijaRepository rezervacijaRepository;

    @MockBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @MockBean
    private SedisteRepository sedisteRepository;

    @Test
    public void testConstructor() throws NoSuchProviderException {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        FilmController actualFilmController = new FilmController(javaMailSenderImpl);
        assertNull(actualFilmController.filmService);
        assertNull(actualFilmController.sedisteRepository);
        assertNull(actualFilmController.rezervisanaSedistaRepository);
        assertNull(actualFilmController.rezervacijaRepository);
        assertNull(actualFilmController.projekcijaRepository);
        assertNull(actualFilmController.korisnikRepository);
        JavaMailSender javaMailSender = actualFilmController.javaMailSender;
        assertTrue(javaMailSender instanceof JavaMailSenderImpl);
        assertNull(((JavaMailSenderImpl) javaMailSender).getDefaultEncoding());
        assertNull(((JavaMailSenderImpl) javaMailSender).getUsername());
        assertNull(((JavaMailSenderImpl) javaMailSender).getProtocol());
        assertTrue(((JavaMailSenderImpl) javaMailSender)
                .getDefaultFileTypeMap() instanceof org.springframework.mail.javamail.ConfigurableMimeFileTypeMap);
        Properties javaMailProperties = ((JavaMailSenderImpl) javaMailSender).getJavaMailProperties();
        assertTrue(javaMailProperties.isEmpty());
        assertNull(((JavaMailSenderImpl) javaMailSender).getPassword());
        assertNull(((JavaMailSenderImpl) javaMailSender).getHost());
        assertEquals(-1, ((JavaMailSenderImpl) javaMailSender).getPort());
        Session session = ((JavaMailSenderImpl) javaMailSender).getSession();
        assertEquals(12, session.getProviders().length);
        assertFalse(session.getDebug());
        assertSame(javaMailProperties, session.getProperties());
        Transport transport = session.getTransport();
        assertFalse(transport.isConnected());
        String expectedToStringResult = String.join("", "smtp://", System.getProperty("user.name"), "@");
        assertEquals(expectedToStringResult, transport.toString());
        String expectedToStringResult1 = String.join("", "smtp://", System.getProperty("user.name"), "@");
        URLName uRLName = transport.getURLName();
        assertEquals(expectedToStringResult1, uRLName.toString());
        String expectedUsername = System.getProperty("user.name");
        assertEquals(expectedUsername, uRLName.getUsername());
        assertNull(uRLName.getRef());
        assertEquals("smtp", uRLName.getProtocol());
        assertEquals(-1, uRLName.getPort());
        assertNull(uRLName.getPassword());
        assertNull(uRLName.getHost());
        assertNull(uRLName.getFile());
        assertSame(javaMailSender, javaMailSenderImpl);
    }

    @Test
    public void testIzabranaSedista() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/selektovanaSedista");
        MockHttpServletRequestBuilder paramResult = postResult.param("projekcijaId", String.valueOf(1L));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("sediste", String.valueOf(new HashSet<Long>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testIzabranaSedista2() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/selektovanaSedista");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("projekcijaId", String.valueOf(1L))
                .param("sediste", "https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSpisakFilmova() throws Exception {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        when(this.filmService.sviFilmovi()).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledFilmova");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("pregledFilmova"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledFilmova"));
    }

//    @Test
//    public void testIzaberiSedista() throws Exception {
//        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
//
//        when(this.korisnikRepository.findByEmail(anyString())).thenReturn(new Korisnik());
//        when(this.sedisteRepository.pronadjiSva()).thenReturn(new HashSet<Sediste>());
//        when(this.projekcijaRepository.getOne(anyLong())).thenReturn(new Projekcija());
//        when(this.projekcijaRepository.nadjiSva(anyLong())).thenReturn(new HashSet<>());
//
//        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/izborSedista");
//        MockHttpServletRequestBuilder requestBuilder = postResult.param("projekcijaId", String.valueOf(1L));
//        MockMvcBuilders.standaloneSetup(this.filmController)
//                .setViewResolvers(viewResolver)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().size(5))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik", "projekcijaId", "sedista", "pr", "rs"))
//                .andExpect(MockMvcResultMatchers.view().name("izborSedista"))
//                .andExpect(MockMvcResultMatchers.forwardedUrl("izborSedista"));
//    }

    @Test
    public void testSpisakProjekcija() throws Exception {
        when(this.projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(new HashSet<Projekcija>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledProjekcija/{film}", "Film");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("danas", "projekcija"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcija"));
    }

    @Test
    public void testSpisakProjekcija2() throws Exception {
        when(this.projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(new HashSet<Projekcija>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pregledProjekcija/{film}", "Film");
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("danas", "projekcija"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcija"));
    }
}

