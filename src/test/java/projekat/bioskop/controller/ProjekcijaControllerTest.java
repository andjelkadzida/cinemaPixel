package projekat.bioskop.controller;

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Sala;
import projekat.bioskop.repository.*;
import projekat.bioskop.services.FilmService;
import projekat.bioskop.services.SalaService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProjekcijaController.class})
@ExtendWith(SpringExtension.class)
class ProjekcijaControllerTest {
    // Constants
    private static final String PROJEKCIJA_ID_PATH = "/{projekcijaId}";
    private static final Long TEST_PROJEKCIJA_ID = 1L;
    private static final String TEST_FILM_NAME = "Film";

    // Controller under test
    @Autowired
    private ProjekcijaController projekcijaController;

    // Services
    @MockitoBean private FilmService filmService;
    @MockitoBean private SalaService salaService;
    @MockitoBean private JavaMailSender javaMailSender;

    // Repositories
    @MockitoBean private FilmRepository filmRepository;
    @MockitoBean private SalaRepository salaRepository;
    @MockitoBean private ProjekcijaRepository projekcijaRepository;
    @MockitoBean private KorisnikRepository korisnikRepository;
    @MockitoBean private RezervacijaRepository rezervacijaRepository;
    @MockitoBean private RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @MockitoBean private SedisteRepository sedisteRepository;

    // Test dependencies
    @MockitoBean private Sala sala;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(projekcijaController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldDisplayNewProjectionForm() throws Exception {
        // Given
        when(filmService.sviFilmovi()).thenReturn(new ArrayList<>());
        when(salaService.sveSale()).thenReturn(new ArrayList<>());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/novaProjekcija"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film", "sala"))
                .andExpect(MockMvcResultMatchers.view().name("novaProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("novaProjekcija"));
    }

    @Test
    public void testConstructor() {
        ProjekcijaController actualProjekcijaController = new ProjekcijaController();
        assertNull(actualProjekcijaController.filmRepository);
        assertNull(actualProjekcijaController.salaService);
        assertNull(actualProjekcijaController.salaRepository);
        assertNull(actualProjekcijaController.rezervisanaSedistaRepository);
        assertNull(actualProjekcijaController.rezervacijaRepository);
        assertNull(actualProjekcijaController.projekcijaRepository);
        assertNull(actualProjekcijaController.filmService);
    }


    @Test
    void shouldDisplayAdminProjectionsList() throws Exception {
        // Given
        when(projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(new HashSet<>());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/pregledProjekcijaAdmin/{film}", TEST_FILM_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projekcija"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcijaAdmin"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcijaAdmin"));
    }

    @Test
    void shouldDisplayAdminMoviesList() throws Exception {
        // Given
        when(filmService.sviFilmovi()).thenReturn(new ArrayList<>());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledFilmovaAdmin"));
    }

    @Test
    void shouldDisplayProjectionEditForm() throws Exception {
        // Given
        when(projekcijaRepository.getOne(anyLong())).thenReturn(new Projekcija());
        when(salaService.sveSale()).thenReturn(new ArrayList<>());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/izmenaProjekcija" + PROJEKCIJA_ID_PATH, TEST_PROJEKCIJA_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projekcija", "sala"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaProjekcija"));
    }

    @Test
    void shouldCancelProjection() throws Exception {
        // Given
        when(projekcijaRepository.getOne(anyLong())).thenReturn(new Projekcija());
        when(rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        Projekcija projekcija = new Projekcija();
        projekcijaRepository.delete(projekcija);

        // Then
        verify(projekcijaRepository, times(1)).delete(projekcija);
        mockMvc.perform(MockMvcRequestBuilders.get("/otkazivanjeProjekcija/{id}", TEST_PROJEKCIJA_ID))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }
}