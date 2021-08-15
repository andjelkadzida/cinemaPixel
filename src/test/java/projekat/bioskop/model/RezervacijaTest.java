package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RezervacijaTest
{
    @Test
    public void testRezervacija()
    {
        Rezervacija actualRezervacija = new Rezervacija();
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("bioskopas@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("SifraKorisnika792");
        korisnik.setKorisnikId(79L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("Bioskopic");
        korisnik.setPoeni(98);
        korisnik.setIme("As");
        actualRezervacija.setKorisnik(korisnik);
        actualRezervacija.setPotvrdjena(true);
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
        sala.setSalaId(2L);
        sala.setBrojSale(3);
        sala.setSedista(new HashSet<Sediste>());
        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(5L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(2021, 8, 15, 21, 00));
        projekcija.setKrajProjekcije(LocalDateTime.of(2021, 8, 15, 22, 30));
        actualRezervacija.setProjekcija(projekcija);
        actualRezervacija.setRezervacijaId(6L);
        HashSet<RezervisanaSedista> rezervisanaSedistaSet = new HashSet<RezervisanaSedista>();
        actualRezervacija.setRezervisanaSedista(rezervisanaSedistaSet);
        assertSame(korisnik, actualRezervacija.getKorisnik());
        assertTrue(actualRezervacija.getPotvrdjena());
        assertSame(projekcija, actualRezervacija.getProjekcija());
        assertEquals(6L, actualRezervacija.getRezervacijaId().longValue());
        assertSame(rezervisanaSedistaSet, actualRezervacija.getRezervisanaSedista());
    }
}

