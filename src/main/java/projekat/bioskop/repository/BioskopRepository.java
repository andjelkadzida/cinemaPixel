package projekat.bioskop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekat.bioskop.model.Bioskop;

@Repository
public interface BioskopRepository extends JpaRepository<Bioskop, Long>
{
    Bioskop findByBioskopId(Long bioskopId);

    @Query(value = "select * from Bioskop b where b.adresa=:adresa and b.grad=:grad", nativeQuery = true)
    Bioskop nadjiPoAdresi(@Param("adresa") String adresa, @Param("grad") String grad);

    @Query(value = "select * from Bioskop b where b.adresa=:adresa and b.grad=:grad and b.bioskop_id=:bioskopId", nativeQuery = true)
    Bioskop nadjiPoAdresiId(@Param("adresa") String adresa, @Param("grad") String grad, @Param("bioskopId") Long bioskopId);
}
