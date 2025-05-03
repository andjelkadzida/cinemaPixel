package projekat.bioskop.controller;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {MenadzerController.class})
@ExtendWith(SpringExtension.class)
class MenadzerControllerTest {
    private static final String REPORT_URL = "/izvestajRezervacija";
    private static final String VIEW_NAME = "izvestajRezervacija";
    
    private MockMvc mockMvc;

    @Autowired
    private MenadzerController menadzerController;

    @MockitoBean
    private RezervacijaRepository rezervacijaRepository;

    @MockitoBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @BeforeEach
    void setUp() {
        StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        mockMvc = buildMockMvc(viewResolver);
    }

    /**
     * Test verifies that the reservation report endpoint returns correct view
     * with expected model attributes and status
     */
    @Test
    void shouldReturnReservationReportWithCorrectModelAttributes() throws Exception {
        // Given
        when(rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(REPORT_URL);
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rezervacijas", "rezervisanaSedistaSet", "datum"))
                .andExpect(MockMvcResultMatchers.view().name(VIEW_NAME))
                .andExpect(MockMvcResultMatchers.forwardedUrl(VIEW_NAME));
    }

    private MockMvc buildMockMvc(StandaloneMvcTestViewResolver viewResolver) {
        return MockMvcBuilders.standaloneSetup(menadzerController)
                .setViewResolvers(viewResolver)
                .build();
    }
}