package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class ProjekcijaTest
{
    @Test
    public void testConstructor2()
    {
        Projekcija projekcija = new Projekcija();
        LocalDateTime pocetakProjekcije = LocalDateTime.of(2021, 8, 15, 21, 00);
        LocalDateTime krajProjekcije = LocalDateTime.of(2021, 8, 15, 22, 30);
        Sala sala = new Sala();
        Projekcija actualProjekcija = new Projekcija(123L, pocetakProjekcije, krajProjekcije, sala, new Film());
        Film film = new Film();
        film.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("Horor, Triler");
        film.setNazivFilma("Countdown");
        film.setTrailer("https://www.youtube.com/embed/TZsgNH17_X4");
        film.setTrajanje(90);
        film.setFilmId(7L);
        film.setTehnologija("2D");
        actualProjekcija.setFilm(film);
        actualProjekcija.setKrajProjekcije(pocetakProjekcije);
        actualProjekcija.setPocetakProjekcije(krajProjekcije);
        actualProjekcija.setProjekcijaId(123L);
        HashSet<Sediste> sedisteSet = new HashSet<Sediste>();
        actualProjekcija.setRasporedSedista(sedisteSet);
        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        actualProjekcija.setRezervacije(rezervacijaSet);
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(246L);
        Sala sala1 = new Sala();
        sala1.setBioskop(bioskop);
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(2L);
        sala1.setBrojSale(3);
        sala1.setSedista(new HashSet<Sediste>());
        actualProjekcija.setSala(sala1);
        assertSame(film, actualProjekcija.getFilm());
        assertSame(pocetakProjekcije, actualProjekcija.getKrajProjekcije());
        assertSame(krajProjekcije, actualProjekcija.getPocetakProjekcije());
        assertEquals(123L, actualProjekcija.getProjekcijaId().longValue());
        assertSame(sedisteSet, actualProjekcija.getRasporedSedista());
        assertSame(rezervacijaSet, actualProjekcija.getRezervacije());
        assertSame(sala1, actualProjekcija.getSala());
    }
}

