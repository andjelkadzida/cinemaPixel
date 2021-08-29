package projekat.bioskop.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {MenadzerController.class})
@ExtendWith(SpringExtension.class)
public class MenadzerControllerTest {
    @Autowired
    private MenadzerController menadzerController;

    @MockBean
    private RezervacijaRepository rezervacijaRepository;

    @MockBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @Test
    public void testListaSvihRezervacija() throws Exception {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izvestajRezervacija");
        MockMvcBuilders.standaloneSetup(this.menadzerController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rezervacijas", "rezervisanaSedistaSet", "datum"))
                .andExpect(MockMvcResultMatchers.view().name("izvestajRezervacija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izvestajRezervacija"));
    }
}

