package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;

import java.util.Set;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long>
{
    @Query("select r from Rezervacija r where r.rezervacijaId=?1")
    Rezervacija findByRezervacijaId(Long rezervacijaId);

    @Query("SELECT r from Rezervacija r where r.korisnik.email like?1")
    Set<Rezervacija> nadjiPoMailu(String email);

    @Query("SELECT rs from RezervisanaSedista rs where rs.rezervacija.korisnik.email like?1")
    Set<RezervisanaSedista> nadjiPoEmailu(String email);

    @Query("select  r from Rezervacija  r")
    Set<Rezervacija>sveRezervacije();


    @Query(value = "select rs from RezervisanaSedista rs where rs.rezervacija.korisnik.email like?1")
    RezervisanaSedista nadjiPoMailuKorisnika(String email);

    @Query("select rs from RezervisanaSedista rs where rs.rezervacija.rezervacijaId=?1")
    Set<RezervisanaSedista> nadjiPoIdRezervacijeSet(Long rezervacijaId);

}
