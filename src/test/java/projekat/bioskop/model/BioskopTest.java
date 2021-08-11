package projekat.bioskop.model;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BioskopTest
{
    @Test
    public void testConstructor()
    {
        Bioskop actualBioskop = new Bioskop();
        actualBioskop.setAdresa("Arsenija Carnojevica 45");
        actualBioskop.setBioskopId(234L);
        actualBioskop.setGrad("Novi Beograd");
        actualBioskop.setNaziv("Pixel");
        HashSet<Sala> salaSet = new HashSet<Sala>();
        actualBioskop.setSale(salaSet);
        assertEquals("Arsenija Carnojevica 45", actualBioskop.getAdresa());
        assertEquals(234L, actualBioskop.getBioskopId().longValue());
        assertEquals("Novi Beograd", actualBioskop.getGrad());
        assertEquals("Pixel", actualBioskop.getNaziv());
        assertSame(salaSet, actualBioskop.getSale());
    }

    @Test
    public void testConstructor2()
    {
        Bioskop actualBioskop = new Bioskop(234L, "Pixel", "Arsenija Carnojevica 45", "Novi Beograd");
        actualBioskop.setAdresa("Arsenija Carnojevica 45");
        actualBioskop.setBioskopId(234L);
        actualBioskop.setGrad("Novi Beograd");
        actualBioskop.setNaziv("Pixel");
        HashSet<Sala> salaSet = new HashSet<Sala>();
        actualBioskop.setSale(salaSet);
        assertEquals("Arsenija Carnojevica 45", actualBioskop.getAdresa());
        assertEquals(234L, actualBioskop.getBioskopId().longValue());
        assertEquals("Novi Beograd", actualBioskop.getGrad());
        assertEquals("Pixel", actualBioskop.getNaziv());
        assertSame(salaSet, actualBioskop.getSale());
    }

    @Test
    public void testConstructor3()
    {
        Bioskop actualBioskop = new Bioskop();
        HashSet<Sala> salaSet = new HashSet<Sala>();
        actualBioskop.setSale(salaSet);
        assertSame(salaSet, actualBioskop.getSale());
    }

}