package projekat.bioskop.controller;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.repository.SedisteRepository;
import projekat.bioskop.services.FilmService;
import projekat.bioskop.services.SalaService;

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