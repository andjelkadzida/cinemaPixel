package projekat.bioskop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.URLName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

@ContextConfiguration(classes = {FilmController.class})
@ExtendWith(SpringExtension.class)
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;

    @MockitoBean
    private FilmService filmService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @MockitoBean
    private KorisnikRepository korisnikRepository;

    @MockitoBean
    private ProjekcijaRepository projekcijaRepository;

    @MockitoBean
    private RezervacijaRepository rezervacijaRepository;

    @MockitoBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @MockitoBean
    private SedisteRepository sedisteRepository;

    private Film testFilm;
    private Projekcija testProjekcija;
    private StandaloneMvcTestViewResolver viewResolver;

    @BeforeEach
    public void setup() {
        testFilm = new Film();
        testFilm.setNazivFilma("Test Film");
        testFilm.setFilmId(1L);

        testProjekcija = new Projekcija();
        testProjekcija.setProjekcijaId(1L);
        testProjekcija.setFilm(testFilm);

        viewResolver = new StandaloneMvcTestViewResolver();
    }

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
        assertInstanceOf(JavaMailSenderImpl.class, javaMailSender);
        assertNull(((JavaMailSenderImpl) javaMailSender).getDefaultEncoding());
        assertNull(((JavaMailSenderImpl) javaMailSender).getUsername());
        assertNull(((JavaMailSenderImpl) javaMailSender).getProtocol());
        assertInstanceOf(ConfigurableMimeFileTypeMap.class, ((JavaMailSenderImpl) javaMailSender)
                .getDefaultFileTypeMap());
        Properties javaMailProperties = ((JavaMailSenderImpl) javaMailSender).getJavaMailProperties();
        assertTrue(javaMailProperties.isEmpty());
        assertNull(((JavaMailSenderImpl) javaMailSender).getPassword());
        assertNull(((JavaMailSenderImpl) javaMailSender).getHost());
        assertEquals(-1, ((JavaMailSenderImpl) javaMailSender).getPort());
        Session session = ((JavaMailSenderImpl) javaMailSender).getSession();
        assertEquals(14, session.getProviders().length);
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
        MockHttpServletRequestBuilder paramResult = postResult.param("projekcijaId", String.valueOf(testProjekcija.getProjekcijaId()));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("sediste", String.valueOf(new HashSet<Long>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testIzabranaSedista2() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/selektovanaSedista");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("projekcijaId", String.valueOf(testProjekcija.getProjekcijaId()))
                .param("sediste", "https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.filmController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSpisakFilmova() throws Exception {
        ArrayList<Film> filmovi = new ArrayList<>();
        filmovi.add(testFilm);
        when(this.filmService.sviFilmovi()).thenReturn(filmovi);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledFilmova");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"));
    }

    @Test
    public void spisakFilmovaTest() throws Exception {
        ArrayList<Film> filmovi = new ArrayList<>();
        filmovi.add(testFilm);
        when(this.filmService.sviFilmovi()).thenReturn(filmovi);

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

    @Test
    public void spisakProjekcijaTest() throws Exception {
        HashSet<Projekcija> projekcije = new HashSet<>();
        projekcije.add(testProjekcija);
        when(this.projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(projekcije);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledProjekcija/{film}", testFilm.getNazivFilma());
        requestBuilder.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("danas", "projekcija"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcija"));
    }
}