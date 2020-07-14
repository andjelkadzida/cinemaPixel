package projekat.bioskop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.bioskop.model.Film;
import projekat.bioskop.repository.FilmRepository;

import java.util.List;

@Service
public class FilmService
{
    @Autowired
    FilmRepository filmRepository;

    public List<Film> sviFilmovi()
    {
        return this.filmRepository.findAll();
    }
}
