package projekat.bioskop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class KorisnikTest
{
    @Test
    public void testGetPoeni()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setPoeni(20);
        assertEquals(20, korisnik.getPoeni());
    }

    @Test
    public void testSetPoeni()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setPoeni(12);
        assertEquals(12, korisnik.getPoeni());
    }

    @Test
    public void testConstructor()
    {
        Korisnik actualKorisnik = new Korisnik();
        actualKorisnik.setClanKluba(true);
        actualKorisnik.setEmail("andjelkadzida@gmail.com");
        actualKorisnik.setIme("Andjelka");
        actualKorisnik.setKorisnikId(123L);
        actualKorisnik.setPrezime("Dzida");
        HashSet<Rezervacija> rezervacijaSet = new HashSet<Rezervacija>();
        actualKorisnik.setRezervacije(rezervacijaSet);
        actualKorisnik.setSifra("Andjelka123");
        actualKorisnik.setTipKorisnika("KORISNIK");
        assertTrue(actualKorisnik.getClanKluba());
        assertEquals("andjelkadzida@gmail.com", actualKorisnik.getEmail());
        assertEquals("Andjelka", actualKorisnik.getIme());
        assertEquals(123L, actualKorisnik.getKorisnikId().longValue());
        assertEquals("Dzida", actualKorisnik.getPrezime());
        assertSame(rezervacijaSet, actualKorisnik.getRezervacije());
        assertEquals("Andjelka123", actualKorisnik.getSifra());
        assertEquals("KORISNIK", actualKorisnik.getTipKorisnika());
    }

    @Test
    public void testConstructor2()
    {
        Korisnik actualKorisnik = new Korisnik(123L, "Andjelka", "Dzida", "andjelkadzida@gmail.com", "Andjelka123", "KORISNIK", 12,
                true);

        assertTrue(actualKorisnik.getClanKluba());
        assertEquals("KORISNIK", actualKorisnik.getTipKorisnika());
        assertEquals("Andjelka123", actualKorisnik.getSifra());
        assertEquals("Dzida", actualKorisnik.getPrezime());
        assertEquals(12, actualKorisnik.getPoeni());
        assertEquals(123L, actualKorisnik.getKorisnikId().longValue());
        assertEquals("Andjelka", actualKorisnik.getIme());
        assertEquals("andjelkadzida@gmail.com", actualKorisnik.getEmail());
    }
}

