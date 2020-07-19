package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.model.Sediste;

import java.util.Set;


public interface RezervisanaSedistaRepository extends JpaRepository<RezervisanaSedista, Long>
{
    @Query("select s from Sediste s where s.sedisteId=?1")
    Sediste nadjiPo(Long sedisteId);

    @Query("select rs from RezervisanaSedista rs where rs.rezervacija.rezervacijaId=?1")
    Sediste nadjiPoRezervaciji(Long rezervacijaId);

    @Query("select rs from RezervisanaSedista rs where rs.sediste.sedisteId=?1")
    RezervisanaSedista nadjiPoSedistu(Long rezervacijaId);
}
