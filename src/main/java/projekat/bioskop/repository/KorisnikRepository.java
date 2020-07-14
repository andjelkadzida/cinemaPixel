package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projekat.bioskop.model.Korisnik;

import java.util.Set;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long>
{
    Korisnik findByEmail(String email);

    @Query("SELECT k from Korisnik k where k.email like ?1")
    Korisnik findByMail(String mail);

    @Query("select k from Korisnik k")
    Set<Korisnik> sviKorisnici();
}
