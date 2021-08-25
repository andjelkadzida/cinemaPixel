package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.repository.KorisnikRepository;

@ContextConfiguration(classes = {MyUserDetailService.class})
@ExtendWith(SpringExtension.class)
public class MyUserDetailServiceTest
{
    @MockBean
    private KorisnikRepository korisnikRepository;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Test
    public void testLoadUserByUsername() throws UsernameNotFoundException
    {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("test@korisnik.com");
        when(this.korisnikRepository.findByEmail(anyString())).thenReturn(korisnik);
        assertEquals("test@korisnik.com",
                this.myUserDetailService.loadUserByUsername("test@korisnik.com").getUsername());
        verify(this.korisnikRepository).findByEmail(anyString());
    }

    @Test
    public void testLoadUserByUsernameException()
    {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                myUserDetailService.loadUserByUsername(""));
    }
}

