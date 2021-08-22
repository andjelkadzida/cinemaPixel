package projekat.bioskop.controller;

import java.time.LocalDateTime;

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
import projekat.bioskop.model.Bioskop;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.model.Sala;
import projekat.bioskop.model.Sediste;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.services.KorisnikService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
    public void brisanjeKorisnikaTest() throws Exception
    {
        doNothing().when(this.rezervisanaSedistaRepository)
                .deleteAll(org.mockito.Mockito.any());
        doNothing().when(this.rezervacijaRepository).deleteAll(org.mockito.Mockito.any());

        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Horor, Triler");
        film.setNazivFilma("Countdown");
        film.setTrailer("https://www.youtube.com/embed/TZsgNH17_X4");
        film.setTrajanje(90);
        film.setFilmId(7L);
        film.setTehnologija("2D");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(5L);
        sala.setBrojSale(5);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(2021, 8, 15, 21, 00));
        projekcija.setKrajProjekcije(LocalDateTime.of(2021, 8, 15, 22, 30));
        when(this.projekcijaRepository.save((Projekcija) org.mockito.Mockito.any())).thenReturn(projekcija);

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("testKorisnik@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("Sifra123");
        korisnik.setKorisnikId(12L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Test");
        korisnik.setPoeni(76);
        korisnik.setIme("Korisnik");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        rezervacijaSet.add(rezervacija);

        Korisnik korisnik1 = new Korisnik();
        korisnik1.setEmail("noviTestKorisnik@gmail.com");
        korisnik1.setClanKluba(true);
        korisnik1.setTipKorisnika("KORISNIK");
        korisnik1.setSifra("Sifra456");
        korisnik1.setKorisnikId(11L);
        korisnik1.setRezervacije(rezervacijaSet);
        korisnik1.setPrezime("Test");
        korisnik1.setPoeni(67);
        korisnik1.setIme("Novi");

        when(this.korisnikRepository.findAll()).thenReturn(new ArrayList<Korisnik>());
        doNothing().when(this.korisnikRepository).delete((Korisnik) org.mockito.Mockito.any());
        when(this.korisnikRepository.getOne((Long) org.mockito.Mockito.any())).thenReturn(korisnik1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/korisnici/delete/{korisnik_id}", 1L);
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
