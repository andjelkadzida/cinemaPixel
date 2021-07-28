package projekat.bioskop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest
{
    Film film = new Film(5l,"Maratonci trce pocasni krug","Komedija","3D",120,"Dobra stara komedija...","dsadsaasd");


    @Test
    void getFilmId()
    {
        assertEquals(film.getFilmId(), 5l);
    }

    @Test
    void getProjekcije()
    {
    }

    @Test
    void setProjekcije()
    {
    }

    @Test
    void setFilmId()
    {
    }

    @Test
    void getNazivFilma()
    {

    }

    @Test
    void setNazivFilma()
    {
    }

    @Test
    void getZanr()
    {
    }

    @Test
    void setZanr()
    {
    }

    @Test
    void getTehnologija()
    {
    }

    @Test
    void setTehnologija()
    {
    }

    @Test
    void getTrajanje()
    {
    }

    @Test
    void setTrajanje()
    {
    }

    @Test
    void getOpis()
    {
    }

    @Test
    void setOpis()
    {
    }

    @Test
    void getTrailer()
    {
    }

    @Test
    void setTrailer()
    {
    }
}