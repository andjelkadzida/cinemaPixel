package projekat.bioskop.controller;

import java.util.*;
import javax.mail.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.repository.SedisteRepository;
import projekat.bioskop.services.FilmService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {FilmController.class})
@ExtendWith(SpringExtension.class)
class FilmControllerTest
{

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
    public void testConstructor() throws NoSuchProviderException
    {
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
    public void testIzabranaSedista() throws Exception
    {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/selektovanaSedista");
        MockHttpServletRequestBuilder paramResult = postResult.param("projekcijaId", String.valueOf(1L));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("sediste", String.valueOf(new HashSet<Long>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testIzabranaSedista2() throws Exception
    {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/selektovanaSedista");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("projekcijaId", String.valueOf(1L))
                .param("sediste", "https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSpisakFilmova() throws Exception
    {
        when(this.filmService.sviFilmovi()).thenReturn(new ArrayList<Film>());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testSpisakProjekcija() throws Exception
    {
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
    public void testSpisakProjekcija2() throws Exception
    {
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