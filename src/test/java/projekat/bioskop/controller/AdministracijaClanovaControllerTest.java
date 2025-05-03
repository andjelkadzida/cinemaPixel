package projekat.bioskop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.*;
import projekat.bioskop.services.KorisnikService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AdministracijaClanovaController.class})
@ExtendWith(SpringExtension.class)
public class AdministracijaClanovaControllerTest {
    private static final String VIEW_ADMIN_CLANOVI = "administriranjeClanova";
    private static final String VIEW_UPDATE_KORISNIKA = "updateKorisnika";
    private static final String ATTR_KORISNIK = "korisnik";
    private static final String ATTR_KORISNICI = "korisnici";
    private static final Long TEST_KORISNIK_ID = 123L;

    // Repository mocks
    @MockitoBean private KorisnikRepository korisnikRepository;
    @MockitoBean private ProjekcijaRepository projekcijaRepository;
    @MockitoBean private RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @MockitoBean private RezervacijaRepository rezervacijaRepository;
    
    // Service mocks
    @MockitoBean private KorisnikService korisnikService;
    
    @Autowired private AdministracijaClanovaController administracijaClanovaController;
    private final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(administracijaClanovaController)
                .setViewResolvers(viewResolver)
                .build();
    }

    private Korisnik createTestKorisnik() {
        return new Korisnik(TEST_KORISNIK_ID, "Andjelka", "Dzida", 
                "andjelkadzida@gmail.com", "Andjelka123", "KORISNIK", 
                12, true);
    }

    @Test
    public void shouldDisplayUpdateForm() throws Exception {
        Korisnik testKorisnik = createTestKorisnik();
        when(korisnikRepository.findById(testKorisnik.getKorisnikId()))
                .thenReturn(Optional.of(testKorisnik));

        mockMvc.perform(MockMvcRequestBuilders.get("/korisnici/update/{korisnik_id}", 
                        testKorisnik.getKorisnikId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists(ATTR_KORISNIK))
                .andExpect(MockMvcResultMatchers.view().name(VIEW_UPDATE_KORISNIKA))
                .andExpect(MockMvcResultMatchers.forwardedUrl(VIEW_UPDATE_KORISNIKA));
    }

    @Test
    public void shouldDeleteKorisnik() throws Exception {
        setupDeleteMocks();
        Korisnik testKorisnik = createTestKorisnik();
        testKorisnik.setRezervacije(new HashSet<>());
        when(korisnikRepository.getOne(any())).thenReturn(testKorisnik);
        when(korisnikRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/korisnici/delete/{korisnik_id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists(ATTR_KORISNIK))
                .andExpect(MockMvcResultMatchers.view().name(VIEW_ADMIN_CLANOVI))
                .andExpect(MockMvcResultMatchers.forwardedUrl(VIEW_ADMIN_CLANOVI));
    }

    private void setupDeleteMocks() {
        doNothing().when(rezervisanaSedistaRepository).deleteAll(any());
        doNothing().when(rezervacijaRepository).deleteAll(any());
        doNothing().when(korisnikRepository).delete(any());
    }

    @Test
    public void shouldUpdateKorisnik() throws Exception {
        Korisnik testKorisnik = createTestKorisnik();
        when(korisnikRepository.save(any())).thenReturn(testKorisnik);

        mockMvc.perform(MockMvcRequestBuilders.post("/korisnici/update/{korisnik_id}", 
                        testKorisnik.getKorisnikId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists(ATTR_KORISNICI))
                .andExpect(MockMvcResultMatchers.view().name(VIEW_UPDATE_KORISNIKA))
                .andExpect(MockMvcResultMatchers.forwardedUrl(VIEW_UPDATE_KORISNIKA));
    }

    @Test
    public void shouldDisplayKorisnikList() throws Exception {
        when(korisnikService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/administriranjeClanova/korisnici"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists(ATTR_KORISNIK))
                .andExpect(MockMvcResultMatchers.view().name(VIEW_ADMIN_CLANOVI))
                .andExpect(MockMvcResultMatchers.forwardedUrl(VIEW_ADMIN_CLANOVI));
    }
}