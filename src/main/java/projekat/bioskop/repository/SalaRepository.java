package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekat.bioskop.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>
{
    Sala findBySalaId(Long salaId);

}
