package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Film;
import projekat.bioskop.repository.FilmRepository;

@ContextConfiguration(classes = {FilmService.class})
@ExtendWith(SpringExtension.class)
public class FilmServiceTest
{
    @MockBean
    private FilmRepository filmRepository;

    @Autowired
    private FilmService filmService;

    @Test
    public void testSviFilmovi()
    {
        ArrayList<Film> filmList = new ArrayList<Film>();
        when(this.filmRepository.findAll()).thenReturn(filmList);
        List<Film> actualSviFilmoviResult = this.filmService.sviFilmovi();
        assertSame(filmList, actualSviFilmoviResult);
        assertTrue(actualSviFilmoviResult.isEmpty());
        verify(this.filmRepository).findAll();
    }
}

