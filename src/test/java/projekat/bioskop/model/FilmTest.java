package projekat.bioskop.model;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FilmTest
{
    @Test
    public void testConstructor()
    {
        Film actualFilm = new Film();
        actualFilm.setFilmId(5l);
        actualFilm.setNazivFilma("Maratonci trce pocasni krug");
        actualFilm.setOpis("https://www.imdb.com/title/tt0084302/?ref_=nv_sr_srsg_0");
        HashSet<Projekcija> projekcijaSet = new HashSet<Projekcija>();
        actualFilm.setProjekcije(projekcijaSet);
        actualFilm.setTehnologija("2D");
        actualFilm.setTrailer("https://www.youtube.com/embed/r9qP5g5ymko");
        actualFilm.setTrajanje(1);
        actualFilm.setZanr("Komedija");
        assertEquals(5l, actualFilm.getFilmId().longValue());
        assertEquals("Maratonci trce pocasni krug", actualFilm.getNazivFilma());
        assertEquals("https://www.imdb.com/title/tt0084302/?ref_=nv_sr_srsg_0", actualFilm.getOpis());
        assertSame(projekcijaSet, actualFilm.getProjekcije());
        assertEquals("2D", actualFilm.getTehnologija());
        assertEquals("https://www.youtube.com/embed/r9qP5g5ymko", actualFilm.getTrailer());
        assertEquals(1, actualFilm.getTrajanje());
        assertEquals("Komedija", actualFilm.getZanr());
    }

    @Test
    public void testConstructor2()
    {
        Film actualFilm = new Film(5l, "Maratonci trce pocasni krug", "Komedija", "2D", 1, "https://www.imdb.com/title/tt0084302/?ref_=nv_sr_srsg_0", "https://www.youtube.com/embed/r9qP5g5ymko");
        actualFilm.setFilmId(5l);
        actualFilm.setNazivFilma("Maratonci trce pocasni krug");
        actualFilm.setOpis("https://www.imdb.com/title/tt0084302/?ref_=nv_sr_srsg_0");
        HashSet<Projekcija> projekcijaSet = new HashSet<Projekcija>();
        actualFilm.setProjekcije(projekcijaSet);
        actualFilm.setTehnologija("2D");
        actualFilm.setTrailer("https://www.youtube.com/embed/r9qP5g5ymko");
        actualFilm.setTrajanje(1);
        actualFilm.setZanr("Komedija");
        assertEquals(5l, actualFilm.getFilmId().longValue());
        assertEquals("Maratonci trce pocasni krug", actualFilm.getNazivFilma());
        assertEquals("https://www.imdb.com/title/tt0084302/?ref_=nv_sr_srsg_0", actualFilm.getOpis());
        assertSame(projekcijaSet, actualFilm.getProjekcije());
        assertEquals("2D", actualFilm.getTehnologija());
        assertEquals("https://www.youtube.com/embed/r9qP5g5ymko", actualFilm.getTrailer());
        assertEquals(1, actualFilm.getTrajanje());
        assertEquals("Komedija", actualFilm.getZanr());
    }
}