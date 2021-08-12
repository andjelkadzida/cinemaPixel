package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RezervisanaSedistaTest
{
    @Test
    public void testRezervisanaSedista()
    {
        Rezervacija rezervacija = new Rezervacija();
        RezervisanaSedista rezervisanaSedista = new RezervisanaSedista(rezervacija, new Sediste());
        RezervisanaSedista actualRezervisanaSedista = new RezervisanaSedista();
        actualRezervisanaSedista.setCenaKarte(10.0);
        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt0090756/?ref_=ttls_li_tt");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Drama, Misterija, Triler");
        film.setNazivFilma("Blue Velvet");
        film.setTrailer("https://www.youtube.com/embed/lGQlJQHHTgA");
        film.setTrajanje(120);
        film.setFilmId(4L);
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
        sala.setSalaId(3L);
        sala.setBrojSale(2);
        sala.setSedista(new HashSet<Sediste>());
        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(2L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(2021, 8, 12, 20, 11));
        projekcija.setKrajProjekcije(LocalDateTime.of(2021, 8, 12, 22, 11));
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("aaa@aaa.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("Sifra12132");
        korisnik.setKorisnikId(254L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Aaa");
        korisnik.setPoeni(42);
        korisnik.setIme("Aaa");
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(158L);
        actualRezervisanaSedista.setRezervacija(rezervacija);
        actualRezervisanaSedista.setRezevisanaSedista_id(1L);
        Sediste sediste = new Sediste();
        sediste.setProjekcijeSedista(new HashSet<Projekcija>());
        sediste.setSala(sala);
        sediste.setTipSedista("Standardno");
        sediste.setSedisteId(17L);
        sediste.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        sediste.setBrojSedista(1);
        actualRezervisanaSedista.setSediste(sediste);
        assertEquals(10.0, actualRezervisanaSedista.getCenaKarte());
        assertSame(rezervacija, actualRezervisanaSedista.getRezervacija());
        assertEquals(1L, actualRezervisanaSedista.getRezevisanaSedista_id().longValue());
        assertSame(sediste, actualRezervisanaSedista.getSediste());
    }
}

