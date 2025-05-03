package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.repository.KorisnikRepository;

@ContextConfiguration(classes = {MyUserDetailService.class})
@ExtendWith(SpringExtension.class)
public class MyUserDetailServiceTest
{
    @MockitoBean
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