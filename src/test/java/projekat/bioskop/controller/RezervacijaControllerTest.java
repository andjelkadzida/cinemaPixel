package projekat.bioskop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {RezervacijaController.class})
@ExtendWith(SpringExtension.class)
public class RezervacijaControllerTest {
    @MockBean
    private KorisnikRepository korisnikRepository;

    @MockBean
    private ProjekcijaRepository projekcijaRepository;

    @Autowired
    private RezervacijaController rezervacijaController;

    @MockBean
    private RezervacijaRepository rezervacijaRepository;

    @MockBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @Test
    public void testAutomatskoOtkazivanjeRezervacije() {
        when(this.rezervacijaRepository.sveRezervacije()).thenReturn(new HashSet<Rezervacija>());
        when(this.projekcijaRepository.sveProjekcije()).thenReturn(new HashSet<Projekcija>());
        when(this.korisnikRepository.sviKorisnici()).thenReturn(new HashSet<Korisnik>());
        this.rezervacijaController.automatskoOtkazivanjeRezervacije();
        verify(this.rezervacijaRepository).sveRezervacije();
        verify(this.projekcijaRepository).sveProjekcije();
        verify(this.korisnikRepository).sviKorisnici();
    }

    @Test
    public void testAutomatskoOtkazivanjeRezervacije2() {
        Film film = new Film();
        film.setOpis("Opis");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Zanr");
        film.setNazivFilma("Naziv Filma");
        film.setTrailer("Trailer");
        film.setTrajanje(0);
        film.setFilmId(123L);
        film.setTehnologija("Tehnologija");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Grad");
        bioskop.setAdresa("Adresa");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Naziv");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("jane.doe@example.org");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("Tip Korisnika");
        korisnik.setSifra("Sifra");
        korisnik.setKorisnikId(123L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Prezime");
        korisnik.setPoeni(0);
        korisnik.setIme("Ime");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        rezervacijaSet.add(rezervacija);
        when(this.rezervacijaRepository.nadjiPoIdRezervacijeSet((Long) any()))
                .thenReturn(new HashSet<RezervisanaSedista>());
        when(this.rezervacijaRepository.sveRezervacije()).thenReturn(rezervacijaSet);

        Film film1 = new Film();
        film1.setOpis("Opis");
        film1.setProjekcije(new HashSet<Projekcija>());
        film1.setZanr("Zanr");
        film1.setNazivFilma("Naziv Filma");
        film1.setTrailer("Trailer");
        film1.setTrajanje(1);
        film1.setFilmId(123L);
        film1.setTehnologija("Tehnologija");

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);

        Sala sala1 = new Sala();
        sala1.setBioskop(bioskop1);
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(123L);
        sala1.setBrojSale(1);
        sala1.setSedista(new HashSet<Sediste>());

        Projekcija projekcija1 = new Projekcija();
        projekcija1.setFilm(film1);
        projekcija1.setProjekcijaId(123L);
        projekcija1.setRasporedSedista(new HashSet<Sediste>());
        projekcija1.setSala(sala1);
        projekcija1.setRezervacije(new HashSet<Rezervacija>());
        projekcija1.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija1.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        when(this.projekcijaRepository.getOne((Long) any())).thenReturn(projekcija1);
        when(this.projekcijaRepository.sveProjekcije()).thenReturn(new HashSet<Projekcija>());
        when(this.korisnikRepository.sviKorisnici()).thenReturn(new HashSet<Korisnik>());
        this.rezervacijaController.automatskoOtkazivanjeRezervacije();
        verify(this.rezervacijaRepository).nadjiPoIdRezervacijeSet((Long) any());
        verify(this.rezervacijaRepository).sveRezervacije();
        verify(this.projekcijaRepository).getOne((Long) any());
        verify(this.projekcijaRepository).sveProjekcije();
        verify(this.korisnikRepository).sviKorisnici();
    }

    @Test
    public void testAutomatskoBrisanjeRezervacijaZaZavrseneProjekcije() {
        when(this.rezervacijaRepository.sveRezervacije()).thenReturn(new HashSet<Rezervacija>());
        when(this.projekcijaRepository.sveProjekcije()).thenReturn(new HashSet<Projekcija>());
        when(this.korisnikRepository.sviKorisnici()).thenReturn(new HashSet<Korisnik>());
        this.rezervacijaController.automatskoBrisanjeRezervacijaZaZavrseneProjekcije();
        verify(this.rezervacijaRepository).sveRezervacije();
        verify(this.projekcijaRepository).sveProjekcije();
        verify(this.korisnikRepository).sviKorisnici();
    }

    @Test
    public void testAutomatskoBrisanjeRezervacijaZaZavrseneProjekcije2() {
        Film film = new Film();
        film.setOpis("Opis");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Zanr");
        film.setNazivFilma("Naziv Filma");
        film.setTrailer("Trailer");
        film.setTrajanje(0);
        film.setFilmId(123L);
        film.setTehnologija("Tehnologija");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Grad");
        bioskop.setAdresa("Adresa");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Naziv");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("jane.doe@example.org");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("Tip Korisnika");
        korisnik.setSifra("Sifra");
        korisnik.setKorisnikId(123L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Prezime");
        korisnik.setPoeni(0);
        korisnik.setIme("Ime");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        rezervacijaSet.add(rezervacija);
        doNothing().when(this.rezervacijaRepository).delete((Rezervacija) any());
        when(this.rezervacijaRepository.nadjiPoIdRezervacijeSet((Long) any()))
                .thenReturn(new HashSet<RezervisanaSedista>());
        when(this.rezervacijaRepository.sveRezervacije()).thenReturn(rezervacijaSet);

        Film film1 = new Film();
        film1.setOpis("Opis");
        film1.setProjekcije(new HashSet<Projekcija>());
        film1.setZanr("Zanr");
        film1.setNazivFilma("Naziv Filma");
        film1.setTrailer("Trailer");
        film1.setTrajanje(1);
        film1.setFilmId(123L);
        film1.setTehnologija("Tehnologija");

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);

        Sala sala1 = new Sala();
        sala1.setBioskop(bioskop1);
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(123L);
        sala1.setBrojSale(1);
        sala1.setSedista(new HashSet<Sediste>());

        Projekcija projekcija1 = new Projekcija();
        projekcija1.setFilm(film1);
        projekcija1.setProjekcijaId(123L);
        projekcija1.setRasporedSedista(new HashSet<Sediste>());
        projekcija1.setSala(sala1);
        projekcija1.setRezervacije(new HashSet<Rezervacija>());
        projekcija1.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija1.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Film film2 = new Film();
        film2.setOpis("Opis");
        film2.setProjekcije(new HashSet<Projekcija>());
        film2.setZanr("Zanr");
        film2.setNazivFilma("Naziv Filma");
        film2.setTrailer("Trailer");
        film2.setTrajanje(1);
        film2.setFilmId(123L);
        film2.setTehnologija("Tehnologija");

        Bioskop bioskop2 = new Bioskop();
        bioskop2.setGrad("Grad");
        bioskop2.setAdresa("Adresa");
        bioskop2.setSale(new HashSet<Sala>());
        bioskop2.setNaziv("Naziv");
        bioskop2.setBioskopId(123L);

        Sala sala2 = new Sala();
        sala2.setBioskop(bioskop2);
        sala2.setProjekcije(new HashSet<Projekcija>());
        sala2.setSalaId(123L);
        sala2.setBrojSale(1);
        sala2.setSedista(new HashSet<Sediste>());

        Projekcija projekcija2 = new Projekcija();
        projekcija2.setFilm(film2);
        projekcija2.setProjekcijaId(123L);
        projekcija2.setRasporedSedista(new HashSet<Sediste>());
        projekcija2.setSala(sala2);
        projekcija2.setRezervacije(new HashSet<Rezervacija>());
        projekcija2.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija2.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        when(this.projekcijaRepository.save((Projekcija) any())).thenReturn(projekcija2);
        when(this.projekcijaRepository.getOne((Long) any())).thenReturn(projekcija1);
        when(this.projekcijaRepository.sveProjekcije()).thenReturn(new HashSet<Projekcija>());
        when(this.korisnikRepository.sviKorisnici()).thenReturn(new HashSet<Korisnik>());
        this.rezervacijaController.automatskoBrisanjeRezervacijaZaZavrseneProjekcije();
        verify(this.rezervacijaRepository).delete((Rezervacija) any());
        verify(this.rezervacijaRepository).nadjiPoIdRezervacijeSet((Long) any());
        verify(this.rezervacijaRepository).sveRezervacije();
        verify(this.projekcijaRepository).getOne((Long) any());
        verify(this.projekcijaRepository).save((Projekcija) any());
        verify(this.projekcijaRepository).sveProjekcije();
        verify(this.korisnikRepository).sviKorisnici();
    }

    @Test
    public void testPotvrdaRezervacije() throws Exception {
        Film film = new Film();
        film.setOpis("Opis");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Zanr");
        film.setNazivFilma("Naziv Filma");
        film.setTrailer("Trailer");
        film.setTrajanje(1);
        film.setFilmId(123L);
        film.setTehnologija("Tehnologija");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Grad");
        bioskop.setAdresa("Adresa");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Naziv");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(1);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("jane.doe@example.org");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("Tip Korisnika");
        korisnik.setSifra("Sifra");
        korisnik.setKorisnikId(123L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Prezime");
        korisnik.setPoeni(1);
        korisnik.setIme("Ime");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        Film film1 = new Film();
        film1.setOpis("Opis");
        film1.setProjekcije(new HashSet<Projekcija>());
        film1.setZanr("Zanr");
        film1.setNazivFilma("Naziv Filma");
        film1.setTrailer("Trailer");
        film1.setTrajanje(1);
        film1.setFilmId(123L);
        film1.setTehnologija("Tehnologija");

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);

        Sala sala1 = new Sala();
        sala1.setBioskop(bioskop1);
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(123L);
        sala1.setBrojSale(1);
        sala1.setSedista(new HashSet<Sediste>());

        Projekcija projekcija1 = new Projekcija();
        projekcija1.setFilm(film1);
        projekcija1.setProjekcijaId(123L);
        projekcija1.setRasporedSedista(new HashSet<Sediste>());
        projekcija1.setSala(sala1);
        projekcija1.setRezervacije(new HashSet<Rezervacija>());
        projekcija1.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija1.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik1 = new Korisnik();
        korisnik1.setEmail("jane.doe@example.org");
        korisnik1.setClanKluba(true);
        korisnik1.setTipKorisnika("Tip Korisnika");
        korisnik1.setSifra("Sifra");
        korisnik1.setKorisnikId(123L);
        korisnik1.setRezervacije(new HashSet<Rezervacija>());
        korisnik1.setPrezime("Prezime");
        korisnik1.setPoeni(1);
        korisnik1.setIme("Ime");

        Rezervacija rezervacija1 = new Rezervacija();
        rezervacija1.setProjekcija(projekcija1);
        rezervacija1.setKorisnik(korisnik1);
        rezervacija1.setPotvrdjena(true);
        rezervacija1.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija1.setRezervacijaId(123L);
        when(this.rezervacijaRepository.save((Rezervacija) any())).thenReturn(rezervacija1);
        when(this.rezervacijaRepository.findByRezervacijaId((Long) any())).thenReturn(rezervacija);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/mojeRezervacije/potvrdi/{rezervacija_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.rezervacijaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/mojeRezervacije"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/mojeRezervacije"));
    }
}

