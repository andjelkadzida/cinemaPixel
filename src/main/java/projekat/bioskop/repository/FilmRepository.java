package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projekat.bioskop.model.Film;


public interface FilmRepository extends JpaRepository<Film, Long>
{
    @Query(value = "select * from Film f where f.naziv_filma=:naziv", nativeQuery = true)
    Film nadjiPoNazivuFilma(@Param("naziv") String naziv);

    @Query(value = "select * from Film f where f.naziv_filma=:naziv and f.film_id=:filmId", nativeQuery = true)
    Film nadjiPoNazivuFilmaId(@Param("naziv") String naziv, @Param("filmId") Long filmId);
}
