package projekat.bioskop.controller;

import java.time.LocalDateTime;
import java.util.*;
import javax.mail.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.*;
import projekat.bioskop.services.FilmService;
import projekat.bioskop.services.SalaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ContextConfiguration(classes = {ProjekcijaController.class})
@ExtendWith(SpringExtension.class)
class ProjekcijaControllerTest
{
    @Autowired
    private ProjekcijaController projekcijaController;

    @MockBean
    private FilmService filmService;

    @MockBean
    private SalaService salaService;

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

    @MockBean
    private Sala sala;

    @MockBean
    private FilmRepository filmRepository;
    @MockBean
    private SalaRepository salaRepository;

    @Test
    public void novaProjekcijaTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        when(this.filmService.sviFilmovi()).thenReturn(new ArrayList<>());
        when(this.salaService.sveSale()).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/novaProjekcija");
        MockMvcBuilders.standaloneSetup(this.projekcijaController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film", "sala"))
                .andExpect(MockMvcResultMatchers.view().name("novaProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("novaProjekcija"));
    }


    @Test
    public void pregledProjekcijaAdminTest() throws Exception
    {
        when(this.projekcijaRepository.projekcijaPoFilmu(anyString())).thenReturn(new HashSet<Projekcija>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledProjekcijaAdmin/{film}", "Film");
        MockMvcBuilders.standaloneSetup(this.projekcijaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projekcija"))
                .andExpect(MockMvcResultMatchers.view().name("pregledProjekcijaAdmin"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledProjekcijaAdmin"));
    }

    @Test
    public void pregledFilmovaAdminTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        when(this.filmService.sviFilmovi()).thenReturn(new ArrayList<Film>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledFilmovaAdmin");
        MockMvcBuilders.standaloneSetup(this.projekcijaController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledFilmovaAdmin"));
    }

    @Test
    public void izmenaProjekcijaViewTest() throws Exception
    {
        when(this.projekcijaRepository.getOne(anyLong())).thenReturn(new Projekcija());
        when(this.salaService.sveSale()).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izmenaProjekcija/{projekcijaId}", 1);
        MockMvcBuilders.standaloneSetup(this.projekcijaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projekcija", "sala"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaProjekcija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaProjekcija"));
    }

    @Test
    public void otkazivanjeRezervacijeTest() throws Exception
    {
        when(this.projekcijaRepository.getOne(anyLong())).thenReturn(new Projekcija());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());

        Projekcija projekcija = new Projekcija();

        this.projekcijaRepository.delete(projekcija);
        verify(this.projekcijaRepository,times(1)).delete(projekcija);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/otkazivanjeProjekcija/{id}", 1l);
        MockMvcBuilders.standaloneSetup(this.projekcijaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }
}