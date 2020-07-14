package projekat.bioskop.model;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "FILM")
public class Film
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", unique = true)
    private Long filmId;

    @Column(name = "NazivFilma", length = 50, nullable = false, unique = false)
    private String nazivFilma;

    @Column(name = "Zanr", length = 50, nullable = false, unique = false)
    private String zanr;

    @Column(name = "Tehnologija", length = 50, nullable = false, unique = false)
    private String tehnologija;

    @Column(name = "Trajanje", length = 50, nullable = false, unique = false)
    private int trajanje;

    @Column(name = "Opis", length = 120, nullable = false, unique = false)
    private String opis;

    @Column(name = "Trailer", length = 120, nullable = false, unique = false)
    private String Trailer;

    @OneToMany(mappedBy = "film")
    private Set<Projekcija> projekcije;

    //Getteri, setteri i konstruktori
    public Long getFilmId()
    {
        return filmId;
    }

    public Set<Projekcija> getProjekcije()
    {
        return projekcije;
    }

    public void setProjekcije(Set<Projekcija> projekcije)
    {
        this.projekcije = projekcije;
    }

    public void setFilmId(Long filmId)
    {
        this.filmId = filmId;
    }

    public String getNazivFilma()
    {
        return nazivFilma;
    }

    public void setNazivFilma(String nazivFilma)
    {
        this.nazivFilma = nazivFilma;
    }

    public String getZanr()
    {
        return zanr;
    }

    public void setZanr(String zanr)
    {
        this.zanr = zanr;
    }

    public String getTehnologija()
    {
        return tehnologija;
    }

    public void setTehnologija(String tehnologija)
    {
        this.tehnologija = tehnologija;
    }

    public int getTrajanje()
    {
        return trajanje;
    }

    public void setTrajanje(int trajanje)
    {
        this.trajanje = trajanje;
    }

    public String getOpis()
    {
        return opis;
    }

    public void setOpis(String opis)
    {
        this.opis = opis;
    }

    public String getTrailer()
    {
        return Trailer;
    }

    public void setTrailer(String Trailer)
    {
        this.Trailer = Trailer;
    }

    public Film(Long filmId, String nazivFilma, String zanr, String tehnologija, int trajanje, String opis, String Trailer)
    {
        this.filmId = filmId;
        this.nazivFilma = nazivFilma;
        this.zanr = zanr;
        this.tehnologija = tehnologija;
        this.trajanje = trajanje;
        this.opis = opis;
        this.Trailer = Trailer;
    }

    public Film()
    {
    }
}
