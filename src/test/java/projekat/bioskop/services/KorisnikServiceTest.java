package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.repository.KorisnikRepository;

@ContextConfiguration(classes = {KorisnikService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
public class KorisnikServiceTest
{
    @MockitoBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockitoBean
    private KorisnikRepository korisnikRepository;

    @Autowired
    private KorisnikService korisnikService;

    @Test
    public void testFindById()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnikId(25L);
        Optional<Korisnik> korisnikOptional = Optional.of(korisnik);
        when(this.korisnikRepository.findById(any())).thenReturn(korisnikOptional);
        Optional<Korisnik> actualFindByIdResult = this.korisnikService.findById(123L);
        assertSame(korisnikOptional, actualFindByIdResult);
        assertTrue(actualFindByIdResult.isPresent());
        verify(this.korisnikRepository).findById(any());
    }

    @Test
    public void testFindAll()
    {
        assertTrue(this.korisnikService.findAll().isEmpty());
    }

    @Test
    public void testSacuvajKorisnika()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("Andjelkadzida@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("KorisnickaSifra741");
        korisnik.setKorisnikId(25L);
        korisnik.setRezervacije(new HashSet<>());
        korisnik.setPrezime("Dzida");
        korisnik.setPoeni(12);
        korisnik.setIme("Andjelka");
        when(this.korisnikRepository.save(any())).thenReturn(korisnik);
        when(this.bCryptPasswordEncoder.encode(any())).thenReturn("foo");
        this.korisnikService.sacuvajKorisnika(korisnik);
        verify(this.korisnikRepository).save(any());
        verify(this.bCryptPasswordEncoder).encode(any());
        assertEquals("foo", korisnik.getSifra());
    }

    @Test
    public void testPostojiKorisnik()
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("Andjelkadzida@gmail.com");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("KORISNIK");
        korisnik.setSifra("KorisnickaSifra741");
        korisnik.setKorisnikId(25L);
        korisnik.setRezervacije(new HashSet<>());
        korisnik.setPrezime("Dzida");
        korisnik.setPoeni(12);
        korisnik.setIme("Andjelka");
        when(this.korisnikRepository.findByEmail(anyString())).thenReturn(korisnik);
        assertTrue(this.korisnikService.postojiKorisnik(korisnik));
        verify(this.korisnikRepository).findByEmail(anyString());
    }
}

