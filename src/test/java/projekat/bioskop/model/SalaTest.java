package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class SalaTest
{
    @Test
    public void testSala()
    {
        Sala actualSala = new Sala();
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(246L);
        actualSala.setBioskop(bioskop);
        actualSala.setBrojSale(5);
        HashSet<Projekcija> projekcijaSet = new HashSet<>();
        actualSala.setProjekcije(projekcijaSet);
        actualSala.setSalaId(5L);
        HashSet<Sediste> sedisteSet = new HashSet<>();
        actualSala.setSedista(sedisteSet);
        assertSame(bioskop, actualSala.getBioskop());
        assertEquals(5, actualSala.getBrojSale());
        assertSame(projekcijaSet, actualSala.getProjekcije());
        assertEquals(5L, actualSala.getSalaId().longValue());
        assertSame(sedisteSet, actualSala.getSedista());
    }
}