package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class SedisteTest
{
    @Test
    public void testSediste()
    {
        Sediste sedistaSala = new Sediste(new Sala());
        Sediste sedisteProjekcija = new Sediste(new HashSet<Projekcija>());
        Sediste s = new Sediste(123L, "Standardno");
        Sediste actualSediste = new Sediste();
        actualSediste.setBrojSedista(1);
        HashSet<Projekcija> projekcijaSet = new HashSet<Projekcija>();
        actualSediste.setProjekcijeSedista(projekcijaSet);
        HashSet<RezervisanaSedista> rezervisanaSedistaSet = new HashSet<RezervisanaSedista>();
        actualSediste.setRezervisanaSedista(rezervisanaSedistaSet);
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);
        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(1);
        sala.setSedista(new HashSet<Sediste>());
        actualSediste.setSala(sala);
        actualSediste.setSedisteId(123L);
        actualSediste.setTipSedista("Standardno");
        assertEquals(1, actualSediste.getBrojSedista());
        assertSame(projekcijaSet, actualSediste.getProjekcijeSedista());
        assertSame(rezervisanaSedistaSet, actualSediste.getRezervisanaSedista());
        assertSame(sala, actualSediste.getSala());
        assertEquals(123L, actualSediste.getSedisteId().longValue());
        assertEquals("Standardno", actualSediste.getTipSedista());
    }
}

