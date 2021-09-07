package projekat.bioskop.model;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FilmTest
{
    @Test
    public void testFilm()
    {
        Film film = new Film();
        Film actualFilm = new Film(7L, "Countdown", "Horor, Triler", "2D", 90, "https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt", "https://www.youtube.com/embed/TZsgNH17_X4");
        actualFilm.setFilmId(7L);
        actualFilm.setNazivFilma("Countdown");
        actualFilm.setOpis("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt");
        HashSet<Projekcija> projekcijaSet = new HashSet<Projekcija>();
        actualFilm.setProjekcije(projekcijaSet);
        actualFilm.setTehnologija("2D");
        actualFilm.setTrailer("https://www.youtube.com/embed/TZsgNH17_X4");
        actualFilm.setTrajanje(90);
        actualFilm.setZanr("Horor, Triler");
        assertEquals(7L, actualFilm.getFilmId().longValue());
        assertEquals("Countdown", actualFilm.getNazivFilma());
        assertEquals("https://www.imdb.com/title/tt10039344/?ref_=wl_li_tt", actualFilm.getOpis());
        assertSame(projekcijaSet, actualFilm.getProjekcije());
        assertEquals("2D", actualFilm.getTehnologija());
        assertEquals("https://www.youtube.com/embed/TZsgNH17_X4", actualFilm.getTrailer());
        assertEquals(90, actualFilm.getTrajanje());
        assertEquals("Horor, Triler", actualFilm.getZanr());
    }
}