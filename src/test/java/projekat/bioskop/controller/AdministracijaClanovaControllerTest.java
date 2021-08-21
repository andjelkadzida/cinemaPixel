package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.services.KorisnikService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AdministracijaClanovaController.class})
@ExtendWith(SpringExtension.class)
public class AdministracijaClanovaControllerTest
{
    @MockBean
    KorisnikRepository korisnikRepository;
    @MockBean
    KorisnikService korisnikService;
    @MockBean
    ProjekcijaRepository projekcijaRepository;
    @MockBean
    RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @MockBean
    RezervacijaRepository rezervacijaRepository;

    @Autowired
    AdministracijaClanovaController administracijaClanovaController;

    @Test
    public void formaZaUpdateTest() throws Exception
    {
        Korisnik korisnik = new Korisnik(123L, "Andjelka", "Dzida", "andjelkadzida@gmail.com", "Andjelka123", "KORISNIK", 12,
                true);
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        when(this.korisnikRepository.findById(korisnik.getKorisnikId())).thenReturn(Optional.of(korisnik));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/korisnici/update/{korisnik_id}", korisnik.getKorisnikId());
        MockMvcBuilders.standaloneSetup(this.administracijaClanovaController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("updateKorisnika"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("updateKorisnika"));
    }

    @Test
    public void updateKorisnikaTest() throws Exception
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setClanKluba(true);
        korisnik.setEmail("andjelkadzida@gmail.com");
        korisnik.setIme("Andjelka");
        korisnik.setKorisnikId(123L);
        korisnik.setPrezime("Dzida");
        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        korisnik.setRezervacije(rezervacijaSet);
        korisnik.setSifra("Andjelka123");
        korisnik.setTipKorisnika("KORISNIK");
        when(this.korisnikRepository.save((Korisnik) any())).thenReturn(korisnik);
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/korisnici/update/{korisnik_id}", korisnik.getKorisnikId());
        MockMvcBuilders.standaloneSetup(this.administracijaClanovaController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnici"))
                .andExpect(MockMvcResultMatchers.view().name("updateKorisnika"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("updateKorisnika"));

    }

    @Test
    public void brisanjeKorisnikaTest() throws Exception
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setClanKluba(true);
        korisnik.setEmail("andjelkadzida@gmail.com");
        korisnik.setIme("Andjelka");
        korisnik.setKorisnikId(123L);
        korisnik.setPrezime("Dzida");
        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        korisnik.setRezervacije(rezervacijaSet);
        korisnik.setSifra("Andjelka123");
        korisnik.setTipKorisnika("KORISNIK");
        HashSet<Sediste> sedistaSet = new HashSet<>();


        when(this.korisnikRepository.getOne(any())).thenReturn(korisnik);
        when(this.rezervacijaRepository.nadjiPoMailu(korisnik.getEmail())).thenReturn(korisnik.getRezervacije());
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());
        this.korisnikRepository.delete(korisnik);
        verify(this.korisnikRepository,times(1)).delete(korisnik);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/korisnici/delete/{korisnik_id}", korisnik.getKorisnikId());
        MockMvcBuilders.standaloneSetup(this.administracijaClanovaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("administriranjeClanova"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("administriranjeClanova"));
    }

    @Test
    public void spisakKorisnikaTest() throws Exception
    {
        when(this.korisnikService.findAll()).thenReturn(new ArrayList<Korisnik>());

        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/administriranjeClanova/korisnici");
        MockMvcBuilders.standaloneSetup(this.administracijaClanovaController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("administriranjeClanova"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("administriranjeClanova"));
    }
}
