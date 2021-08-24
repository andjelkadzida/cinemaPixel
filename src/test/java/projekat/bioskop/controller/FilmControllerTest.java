package projekat.bioskop.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

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
public class FilmControllerTest
{

    @Autowired
    FilmController filmController;
    @MockBean
    FilmService filmService;
    @MockBean
    ProjekcijaRepository projekcijaRepository;
    @MockBean
    KorisnikRepository korisnikRepository;
    @MockBean
    RezervacijaRepository rezervacijaRepository;
    @MockBean
    SedisteRepository sedisteRepository;
    @MockBean
    RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    public void spisakFilmovaTest() throws Exception
    {
        when(this.filmService.sviFilmovi()).thenReturn(new ArrayList<Film>());
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

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
    public void spisakProjekcijaTest() throws Exception
    {
        when(this.projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(new HashSet<Projekcija>());
        LocalDateTime danas = LocalDateTime.now();

        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledProjekcija/{film}", "Film");
        MockMvcBuilders.standaloneSetup(this.filmController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projekcija", "danas"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcija"));
    }


}
