package projekat.bioskop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.FilmRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {DodavanjeFilmController.class})
@ExtendWith(SpringExtension.class)
public class DodavanjeFilmControllerTest {

    @Autowired
    private DodavanjeFilmController dodavanjeFilmController;

    @MockitoBean
    private FilmRepository filmRepository;

    @MockitoBean
    private ProjekcijaRepository projekcijaRepository;

    @MockitoBean
    private RezervacijaRepository rezervacijaRepository;

    @MockitoBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    private Film film;
    private Projekcija projekcija;
    private Rezervacija rezervacija;

    @BeforeEach
    public void setup() {
        film = new Film();
        film.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film.setProjekcije(new HashSet<>());
        film.setZanr("Horor, Triler");
        film.setNazivFilma("Countdown");
        film.setTrailer("https://www.youtube.com/embed/TZsgNH17_X4");
        film.setTrajanje(90);
        film.setFilmId(7L);
        film.setTehnologija("2D");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(246L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<>());
        sala.setSalaId(5L);
        sala.setBrojSale(5);
        sala.setSedista(new HashSet<>());

        projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(2021, 8, 15, 21, 8));
        projekcija.setKrajProjekcije(LocalDateTime.of(2021, 8, 15, 22, 30));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("nikoladrikic@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("SifraKorisnika7915");
        korisnik.setKorisnikId(8L);
        korisnik.setRezervacije(new HashSet<>());
        korisnik.setPrezime("Drikic");
        korisnik.setPoeni(50);
        korisnik.setIme("Nikola");

        rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<>());
        rezervacija.setRezervacijaId(9L);
    }

    @Test
    public void testBrisanjeRezervisanihFilmova() throws Exception {
        when(rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());
        when(projekcijaRepository.nadjiPoIdFilma(any())).thenReturn(new HashSet<>());

        ArrayList<Rezervacija> rezervacijaList = new ArrayList<>();
        rezervacijaList.add(rezervacija);
        when(rezervacijaRepository.findAll()).thenReturn(rezervacijaList);
        when(projekcijaRepository.nadjiPoIdFilma(any())).thenReturn(new HashSet<>());

        doNothing().when(filmRepository).delete(any());
        when(filmRepository.getOne(any())).thenReturn(film);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testBrisanjeFilmovaProjekcija() throws Exception {
        when(rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<>());
        when(rezervacijaRepository.findAll()).thenReturn(new ArrayList<>());

        HashSet<Projekcija> projekcijaSet = new HashSet<>();
        projekcijaSet.add(projekcija);
        doNothing().when(projekcijaRepository).delete(any());
        when(projekcijaRepository.nadjiPoIdFilma(any())).thenReturn(projekcijaSet);

        doNothing().when(filmRepository).delete(any());
        when(filmRepository.getOne(any())).thenReturn(film);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testDodavanjeNovogFilma() throws Exception {
        when(filmRepository.nadjiPoNazivuFilma(anyString())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/dodavanjeFilmova");
        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film", "poruka"))
                .andExpect(MockMvcResultMatchers.view().name("noviFilm"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviFilm"));
    }

    @Test
    public void testIzmenaFilmova() throws Exception {
        Film film1 = new Film();
        film1.setOpis(film.getOpis());
        film1.setProjekcije(new HashSet<>());
        film1.setZanr("Horror, Triler");
        film1.setNazivFilma("Countdown - Odbrojavanje");
        film1.setTrailer("Trailer");
        film1.setTrajanje(90);
        film1.setFilmId(7L);
        film1.setTehnologija("2D");

        when(filmRepository.save(any())).thenReturn(film1);
        when(filmRepository.nadjiPoNazivuFilmaId(anyString(), any())).thenReturn(film1);
        when(filmRepository.nadjiPoNazivuFilma(anyString())).thenReturn(null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/izmenaFilmova/{film_id}", 1L)
                .param("nazivFilma", "Countdown - Odbrojavanje")
                .param("zanr", "Horror, Triler")
                .param("tehnologija", "2D")
                .param("trajanje", "90")
                .param("opis", film.getOpis())
                .param("trailer", "Trailer");

        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testIzmenaFilmovaView() throws Exception {
        when(filmRepository.getOne(any())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izmenaFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaFilmova"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaFilmova"));
    }

    @Test
    public void testNoviFilmView() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/dodavanjeFilmova");
        getResult.contentType("Filmovi");
        MockMvcBuilders.standaloneSetup(dodavanjeFilmController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("noviFilm"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviFilm"));
    }
}