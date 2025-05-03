package projekat.bioskop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Bioskop;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.Sala;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

@ContextConfiguration(classes = {RezervacijaController.class})
@ExtendWith(SpringExtension.class)
public class RezervacijaControllerTest {
    private static final Long TEST_ID = 123L;
    private static final String TEST_EMAIL = "jane.doe@example.org";
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(1, 1, 1, 1, 1);

    @MockitoBean
    private KorisnikRepository korisnikRepository;
    @MockitoBean
    private ProjekcijaRepository projekcijaRepository;
    @Autowired
    private RezervacijaController rezervacijaController;
    @MockitoBean
    private RezervacijaRepository rezervacijaRepository;
    @MockitoBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @BeforeEach
    void setUp() {
        when(rezervacijaRepository.sveRezervacije()).thenReturn(new HashSet<>());
        when(projekcijaRepository.sveProjekcije()).thenReturn(new HashSet<>());
        when(korisnikRepository.sviKorisnici()).thenReturn(new HashSet<>());
    }

    @Test
    void shouldHandleEmptyReservationsForAutomaticCancellation() {
        rezervacijaController.automatskoOtkazivanjeRezervacije();
        verifyRepositoryCalls();
    }

    @Test
    void shouldCancelReservationAutomatically() {
        // Setup
        Rezervacija testRezervacija = createTestRezervacija();
        HashSet<Rezervacija> rezervacije = new HashSet<>();
        rezervacije.add(testRezervacija);

        when(rezervacijaRepository.sveRezervacije()).thenReturn(rezervacije);
        when(rezervacijaRepository.nadjiPoIdRezervacijeSet(any())).thenReturn(new HashSet<>());
        when(projekcijaRepository.getOne(any())).thenReturn(createTestProjekcija());

        // Execute
        rezervacijaController.automatskoOtkazivanjeRezervacije();

        // Verify
        verifyRepositoryCalls();
        verify(rezervacijaRepository).nadjiPoIdRezervacijeSet(any());
        verify(projekcijaRepository).getOne(any());
    }

    private void verifyRepositoryCalls() {
        verify(rezervacijaRepository).sveRezervacije();
        verify(projekcijaRepository).sveProjekcije();
        verify(korisnikRepository).sviKorisnici();
    }

    private Rezervacija createTestRezervacija() {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(createTestProjekcija());
        rezervacija.setKorisnik(createTestKorisnik());
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<>());
        rezervacija.setRezervacijaId(TEST_ID);
        return rezervacija;
    }

    private Korisnik createTestKorisnik() {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(TEST_EMAIL);
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("Tip Korisnika");
        korisnik.setKorisnikId(TEST_ID);
        korisnik.setRezervacije(new HashSet<>());
        return korisnik;
    }

    private Projekcija createTestProjekcija() {
        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(createTestFilm());
        projekcija.setProjekcijaId(TEST_ID);
        projekcija.setSala(createTestSala());
        projekcija.setPocetakProjekcije(TEST_DATE_TIME);
        projekcija.setKrajProjekcije(TEST_DATE_TIME);
        return projekcija;
    }

    private Film createTestFilm() {
        Film film = new Film();
        film.setFilmId(TEST_ID);
        film.setNazivFilma("Naziv Filma");
        film.setTrajanje(1);
        return film;
    }

    private Sala createTestSala() {
        Sala sala = new Sala();
        sala.setBioskop(createTestBioskop());
        sala.setSalaId(TEST_ID);
        sala.setBrojSale(1);
        return sala;
    }

    private Bioskop createTestBioskop() {
        Bioskop bioskop = new Bioskop();
        bioskop.setBioskopId(TEST_ID);
        bioskop.setNaziv("Naziv");
        return bioskop;
    }
}