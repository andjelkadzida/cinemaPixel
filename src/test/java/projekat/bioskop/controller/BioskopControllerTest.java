package projekat.bioskop.controller;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
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

@ContextConfiguration(classes = {BioskopController.class})
@ExtendWith(SpringExtension.class)
public class BioskopControllerTest {
    private static final String TEST_GRAD = "Test Grad";
    private static final String TEST_ADRESA = "Test Adresa";
    private static final String TEST_NAZIV = "Test Bioskop";
    private static final Long TEST_ID = 123L;

    // Primary repository under test
    @MockitoBean
    private BioskopRepository bioskopRepository;

    // Related repositories
    @MockitoBean
    private SalaRepository salaRepository;
    @MockitoBean
    private ProjekcijaRepository projekcijaRepository;
    
    // Reservation related repositories
    @MockitoBean
    private RezervacijaRepository rezervacijaRepository;
    @MockitoBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @MockitoBean
    private SedisteRepository sedisteRepository;

    @Autowired
    private BioskopController bioskopController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bioskopController)
                .setViewResolvers(new StandaloneMvcTestViewResolver())
                .build();
    }

    private static class TestEntityBuilder {
        static Bioskop createBioskop(String grad, String adresa, String naziv, Long id) {
            Bioskop bioskop = new Bioskop();
            bioskop.setGrad(grad);
            bioskop.setAdresa(adresa);
            bioskop.setNaziv(naziv);
            bioskop.setBioskopId(id);
            return bioskop;
        }
    }

    @Test
    public void shouldSuccessfullyAddNewBioskop() throws Exception {
        // Given
        Bioskop testBioskop = TestEntityBuilder.createBioskop("Valjevo", "Vlade Danilovica 15", "New Pixel", 174L);
        when(bioskopRepository.nadjiPoAdresi(anyString(), anyString())).thenReturn(testBioskop);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/dodavanjeBioskopa"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop", "poruka"))
                .andExpect(MockMvcResultMatchers.view().name("noviBioskop"));
    }

    @Test
    public void shouldSuccessfullyUpdateBioskop() throws Exception {
        // Given
        Bioskop updatedBioskop = TestEntityBuilder.createBioskop("Novi Beograd", 
            "Bulevar Milutina Milankovica 13", "Pixel", 234L);
        when(bioskopRepository.save(any())).thenReturn(updatedBioskop);
        when(bioskopRepository.nadjiPoAdresiId(anyString(), anyString(), any())).thenReturn(updatedBioskop);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/izmenaBioskopa/{bioskop_id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void shouldSuccessfullyDeleteBioskop() throws Exception {
        // Given
        Bioskop testBioskop = TestEntityBuilder.createBioskop(TEST_GRAD, TEST_ADRESA, TEST_NAZIV, TEST_ID);
        when(bioskopRepository.findByBioskopId(any())).thenReturn(testBioskop);
        mockEmptyRepositories();

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    private void mockEmptyRepositories() {
        when(sedisteRepository.findAll()).thenReturn(new ArrayList<>());
        when(salaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(projekcijaRepository.findAll()).thenReturn(new ArrayList<>());
    }
}