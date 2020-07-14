package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekat.bioskop.model.Sediste;

import java.util.Set;


public interface SedisteRepository extends JpaRepository<Sediste, Long>
{
    @Query("SELECT s from Sediste s order by s.sedisteId asc")
    Set<Sediste>pronadjiSva();
}
