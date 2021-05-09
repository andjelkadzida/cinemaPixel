package projekat.bioskop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BioskopTest
{

    Bioskop bioskop = new Bioskop(null, "Pixel", "Arsenija Carnojevica 45", "Novi Beograd");

    @Test
    void getBioskopId()
    {
        assertNull(bioskop.getBioskopId());
    }

    @Test
    void setBioskopId()
    {
    }

    @Test
    void getNaziv()
    {
        assertEquals(bioskop.getNaziv(), "Pixel");
    }

    @Test
    void setNaziv()
    {
    }

    @Test
    void getAdresa()
    {
    }

    @Test
    void setAdresa()
    {
    }

    @Test
    void getGrad()
    {
    }

    @Test
    void setGrad()
    {
    }

    @Test
    void getSale()
    {
    }

    @Test
    void setSale()
    {
    }
}