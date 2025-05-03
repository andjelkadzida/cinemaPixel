package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.model.Sediste;

public interface RezervisanaSedistaRepository extends JpaRepository<RezervisanaSedista, Long>
{
    @Query("select rs from RezervisanaSedista rs where rs.sediste.sedisteId=?1")
    RezervisanaSedista nadjiPoSedistu(Long rezervacijaId);
}