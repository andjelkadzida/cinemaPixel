package projekat.bioskop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Sediste;
import projekat.bioskop.repository.*;
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
