package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import projekat.bioskop.model.Korisnik;

public class KorisnikPrincipalTest
{
    @Test
    public void testKorisnikPrincipal()
    {
        assertTrue((new KorisnikPrincipal(new Korisnik())).isAccountNonExpired());
        assertTrue((new KorisnikPrincipal(new Korisnik())).isAccountNonLocked());
        assertTrue((new KorisnikPrincipal(new Korisnik())).isCredentialsNonExpired());
        assertTrue((new KorisnikPrincipal(new Korisnik())).isEnabled());
    }

    @Test
    public void testGetAuthorities()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setTipKorisnika("KORISNIK");
        assertEquals(1, (new KorisnikPrincipal(korisnik)).getAuthorities().size());
    }

    @Test
    public void testGetPasswordNull()
    {
        assertNull((new KorisnikPrincipal(new Korisnik())).getPassword());
    }

    @Test
    public void testGetPassword()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("korisnik@test.com");
        assertNull((new KorisnikPrincipal(korisnik)).getPassword());
    }

    @Test
    public void testGetUsernamenNull()
    {
        assertNull((new KorisnikPrincipal(new Korisnik())).getUsername());
    }

    @Test
    public void testGetUsername()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("korisnik@test.com");
        assertEquals("korisnik@test.com", (new KorisnikPrincipal(korisnik)).getUsername());
    }
}

