package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Sediste;

import java.util.Set;

public interface ProjekcijaRepository extends JpaRepository<Projekcija, Long>
{
    @Query("select p from Projekcija  p where p.film.nazivFilma like ?1")
    Set<Projekcija> projekcijaPoFilmu(String nazivFilma);

    @Query("select rs.rasporedSedista from Projekcija rs where rs.projekcijaId=?1")
    Set<Sediste> nadjiSva(Long projekcijaId);

    @Query(value = "select  p from Projekcija  p where  p.film.filmId=?1")
    Set<Projekcija> nadjiPoIdFilma(Long filmId);

    @Query(value = "select  p from Projekcija  p")
    Set<Projekcija>sveProjekcije();
}
