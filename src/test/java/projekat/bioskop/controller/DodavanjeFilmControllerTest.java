package projekat.bioskop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

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
import projekat.bioskop.repository.FilmRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {DodavanjeFilmController.class})
@ExtendWith(SpringExtension.class)
public class DodavanjeFilmControllerTest
{
    @Autowired
    private DodavanjeFilmController dodavanjeFilmController;

    @MockBean
    private FilmRepository filmRepository;

    @MockBean
    private ProjekcijaRepository projekcijaRepository;

    @MockBean
    private RezervacijaRepository rezervacijaRepository;

    @MockBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @Test
    public void testBrisanjeRezervisanihFilmova() throws Exception
    {
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());
        when(this.projekcijaRepository.nadjiPoIdFilma((Long) any())).thenReturn(new HashSet<Projekcija>());

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
        bioskop.setBioskopId(246L);

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

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("nikoladrikic@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("SifraKorisnika7915");
        korisnik.setKorisnikId(8L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Drikic");
        korisnik.setPoeni(50);
        korisnik.setIme("Nikola");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(9L);

        ArrayList<Rezervacija> rezervacijaList = new ArrayList<Rezervacija>();
        rezervacijaList.add(rezervacija);
        when(this.rezervacijaRepository.findAll()).thenReturn(rezervacijaList);
        when(this.projekcijaRepository.nadjiPoIdFilma((Long) any())).thenReturn(new HashSet<Projekcija>());

        doNothing().when(this.filmRepository).delete((Film) any());
        when(this.filmRepository.getOne((Long) any())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testBrisanjeFilmovaProjekcija() throws Exception
    {
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());

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
        bioskop.setBioskopId(246L);

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

        HashSet<Projekcija> projekcijaSet = new HashSet<Projekcija>();
        projekcijaSet.add(projekcija);
        doNothing().when(this.projekcijaRepository).delete((Projekcija) any());
        when(this.projekcijaRepository.nadjiPoIdFilma((Long) any())).thenReturn(projekcijaSet);

        doNothing().when(this.filmRepository).delete((Film) any());
        when(this.filmRepository.getOne((Long) any())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testDodavanjeNovogFilma() throws Exception
    {
        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt0068646/?ref_=wl_li_i");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Krimi, Drama");
        film.setNazivFilma("The Godfather");
        film.setTrailer("https://www.youtube.com/embed/sY1S34973zA");
        film.setTrajanje(175);
        film.setFilmId(11L);
        film.setTehnologija("2D");
        when(this.filmRepository.nadjiPoNazivuFilma(anyString())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/dodavanjeFilmova");
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film", "poruka"))
                .andExpect(MockMvcResultMatchers.view().name("noviFilm"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviFilm"));
    }

    @Test
    public void testIzmenaFilmova() throws Exception
    {
        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Horor, Triler");
        film.setNazivFilma("Countdown");
        film.setTrailer("Trailer");
        film.setTrajanje(90);
        film.setFilmId(7L);
        film.setTehnologija("2D");

        Film film1 = new Film();
        film1.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film1.setProjekcije(new HashSet<Projekcija>());
        film1.setZanr("Horror, Triler");
        film1.setNazivFilma("Countdown - Odbrojavanje");
        film1.setTrailer("Trailer");
        film1.setTrajanje(90);
        film1.setFilmId(7L);
        film1.setTehnologija("2D");

        when(this.filmRepository.save((Film) any())).thenReturn(film1);
        when(this.filmRepository.nadjiPoNazivuFilmaId(anyString(), (Long) any())).thenReturn(film1);
        when(this.filmRepository.nadjiPoNazivuFilma(anyString())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/izmenaFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledFilmovaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledFilmovaAdmin"));
    }

    @Test
    public void testIzmenaFilmovaView() throws Exception
    {
        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Horor, Triler");
        film.setNazivFilma("Countdown");
        film.setTrailer("https://www.youtube.com/embed/TZsgNH17_X4");
        film.setTrajanje(90);
        film.setFilmId(7L);
        film.setTehnologija("2D");
        when(this.filmRepository.getOne((Long) any())).thenReturn(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izmenaFilmova/{film_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaFilmova"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaFilmova"));
    }

    @Test
    public void testNoviFilmView() throws Exception
    {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/dodavanjeFilmova");
        getResult.contentType("Filmovi");
        MockMvcBuilders.standaloneSetup(this.dodavanjeFilmController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("film"))
                .andExpect(MockMvcResultMatchers.view().name("noviFilm"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviFilm"));
    }
}

