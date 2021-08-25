package projekat.bioskop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Bioskop;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.model.Sala;
import projekat.bioskop.model.Sediste;
import projekat.bioskop.repository.BioskopRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.repository.SalaRepository;
import projekat.bioskop.repository.SedisteRepository;

@ContextConfiguration(classes = {BioskopController.class})
@ExtendWith(SpringExtension.class)
public class BioskopControllerTest
{
    @MockBean
    private ProjekcijaRepository projekcijaRepository;

    @MockBean
    private RezervacijaRepository rezervacijaRepository;

    @MockBean
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @MockBean
    private SedisteRepository sedisteRepository;

    @Autowired
    BioskopController bioskopController;

    @MockBean
    BioskopRepository bioskopRepository;

    @MockBean
    SalaRepository salaRepository;

    @Test
    public void testDodavanjeNovogBioskopa() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Valjevo");
        bioskop.setAdresa("Vlade Danilovica 15");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("New Pixel");
        bioskop.setBioskopId(174L);
        when(this.bioskopRepository.nadjiPoAdresi(anyString(), anyString())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/dodavanjeBioskopa");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop", "poruka"))
                .andExpect(MockMvcResultMatchers.view().name("noviBioskop"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviBioskop"));
    }

    @Test
    public void testIzmenaBioskopa() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Novi Beograd");
        bioskop1.setAdresa("Bulevar Milutina Milankovica 13");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Pixel");
        bioskop1.setBioskopId(234L);

        when(this.bioskopRepository.save((Bioskop) any())).thenReturn(bioskop1);
        when(this.bioskopRepository.nadjiPoAdresiId(anyString(), anyString(), (Long) any())).thenReturn(bioskop1);
        when(this.bioskopRepository.nadjiPoAdresi(anyString(), anyString())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/izmenaBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testIzmenaBioskopaView() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izmenaBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaBioskopa"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaBioskopa"));
    }

    @Test
    public void testNoviBioskopView() throws Exception
    {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/dodavanjeBioskopa");
        getResult.contentType("Novi bioskop view");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("noviBioskop"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviBioskop"));
    }

    @Test
    public void testPregledBioskopa() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        when(this.bioskopRepository.findAll()).thenReturn(new ArrayList<Bioskop>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledBioskopa");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("pregledBioskopa"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledBioskopa"));
    }

    @Test
    public void testPregledBioskopaAdmin() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        when(this.bioskopRepository.findAll()).thenReturn(new ArrayList<Bioskop>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pregledBioskopaAdmin");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pregledBioskopaAdmin"));
    }

   /**BRISANJE BIOSKOPA... LUKA AKO IMAS VOLJE, OPTIMIZUJ PLS*/
   @Test
   public void testBrisanjeBioskopa() throws Exception
   {
       when(this.sedisteRepository.findAll()).thenReturn(new ArrayList<Sediste>());
       when(this.salaRepository.findAll()).thenReturn(new ArrayList<Sala>());
       when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
       when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());
       when(this.projekcijaRepository.findAll()).thenReturn(new ArrayList<Projekcija>());

       Bioskop bioskop = new Bioskop();
       bioskop.setGrad("Grad");
       bioskop.setAdresa("Adresa");
       bioskop.setSale(new HashSet<Sala>());
       bioskop.setNaziv("Naziv");
       bioskop.setBioskopId(123L);
       doNothing().when(this.bioskopRepository).delete((Bioskop) any());
       when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop);
       MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
       MockMvcBuilders.standaloneSetup(this.bioskopController)
               .build()
               .perform(requestBuilder)
               .andExpect(MockMvcResultMatchers.status().isFound())
               .andExpect(MockMvcResultMatchers.model().size(0))
               .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
               .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
   }

    @Test
    public void testBrisanjeBioskopa2() throws Exception
    {
        when(this.sedisteRepository.findAll()).thenReturn(new ArrayList<Sediste>());

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("?");
        bioskop.setAdresa("?");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("?");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        ArrayList<Sala> salaList = new ArrayList<Sala>();
        salaList.add(sala);
        when(this.salaRepository.findAll()).thenReturn(salaList);
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());
        when(this.projekcijaRepository.findAll()).thenReturn(new ArrayList<Projekcija>());

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);
        doNothing().when(this.bioskopRepository).delete((Bioskop) any());
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testBrisanjeBioskopa3() throws Exception
    {
        when(this.sedisteRepository.findAll()).thenReturn(new ArrayList<Sediste>());
        when(this.salaRepository.findAll()).thenReturn(new ArrayList<Sala>());
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());

        Film film = new Film();
        film.setOpis("?");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("?");
        film.setNazivFilma("?");
        film.setTrailer("?");
        film.setTrajanje(0);
        film.setFilmId(123L);
        film.setTehnologija("?");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("?");
        bioskop.setAdresa("?");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("?");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("jane.doe@example.org");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("?");
        korisnik.setSifra("?");
        korisnik.setKorisnikId(123L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("?");
        korisnik.setPoeni(0);
        korisnik.setIme("?");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        ArrayList<Rezervacija> rezervacijaList = new ArrayList<Rezervacija>();
        rezervacijaList.add(rezervacija);
        when(this.rezervacijaRepository.findAll()).thenReturn(rezervacijaList);
        when(this.projekcijaRepository.findAll()).thenReturn(new ArrayList<Projekcija>());

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);
        doNothing().when(this.bioskopRepository).delete((Bioskop) any());
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testBrisanjeBioskopa4() throws Exception
    {
        when(this.sedisteRepository.findAll()).thenReturn(new ArrayList<Sediste>());
        when(this.salaRepository.findAll()).thenReturn(new ArrayList<Sala>());
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());

        Film film = new Film();
        film.setOpis("?");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("?");
        film.setNazivFilma("?");
        film.setTrailer("?");
        film.setTrajanje(0);
        film.setFilmId(123L);
        film.setTehnologija("?");

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("?");
        bioskop.setAdresa("?");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("?");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        ArrayList<Projekcija> projekcijaList = new ArrayList<Projekcija>();
        projekcijaList.add(projekcija);
        when(this.projekcijaRepository.findAll()).thenReturn(projekcijaList);

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Grad");
        bioskop1.setAdresa("Adresa");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Naziv");
        bioskop1.setBioskopId(123L);
        doNothing().when(this.bioskopRepository).delete((Bioskop) any());
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testBrisanjeBioskopa5() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("?");
        bioskop.setAdresa("?");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("?");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Sediste sediste = new Sediste();
        sediste.setProjekcijeSedista(new HashSet<Projekcija>());
        sediste.setSala(sala);
        sediste.setTipSedista("?");
        sediste.setSedisteId(123L);
        sediste.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        sediste.setBrojSedista(0);

        ArrayList<Sediste> sedisteList = new ArrayList<Sediste>();
        sedisteList.add(sediste);
        when(this.sedisteRepository.findAll()).thenReturn(sedisteList);

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("?");
        bioskop1.setAdresa("?");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("?");
        bioskop1.setBioskopId(123L);

        Sala sala1 = new Sala();
        sala1.setBioskop(bioskop1);
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(123L);
        sala1.setBrojSale(0);
        sala1.setSedista(new HashSet<Sediste>());

        ArrayList<Sala> salaList = new ArrayList<Sala>();
        salaList.add(sala1);
        when(this.salaRepository.findAll()).thenReturn(salaList);
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(new ArrayList<RezervisanaSedista>());
        when(this.rezervacijaRepository.findAll()).thenReturn(new ArrayList<Rezervacija>());
        when(this.projekcijaRepository.findAll()).thenReturn(new ArrayList<Projekcija>());

        Bioskop bioskop2 = new Bioskop();
        bioskop2.setGrad("Grad");
        bioskop2.setAdresa("Adresa");
        bioskop2.setSale(new HashSet<Sala>());
        bioskop2.setNaziv("Naziv");
        bioskop2.setBioskopId(123L);
        doNothing().when(this.bioskopRepository).delete((Bioskop) any());
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testBrisanjeBioskopa6() throws Exception
    {
        when(this.sedisteRepository.findAll()).thenReturn(new ArrayList<Sediste>());
        when(this.salaRepository.findAll()).thenReturn(new ArrayList<Sala>());

        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("?");
        bioskop.setAdresa("?");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("?");
        bioskop.setBioskopId(123L);

        Sala sala = new Sala();
        sala.setBioskop(bioskop);
        sala.setProjekcije(new HashSet<Projekcija>());
        sala.setSalaId(123L);
        sala.setBrojSale(0);
        sala.setSedista(new HashSet<Sediste>());

        Sediste sediste = new Sediste();
        sediste.setProjekcijeSedista(new HashSet<Projekcija>());
        sediste.setSala(sala);
        sediste.setTipSedista("?");
        sediste.setSedisteId(123L);
        sediste.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        sediste.setBrojSedista(0);

        Film film = new Film();
        film.setOpis("?");
        film.setProjekcije(new HashSet<Projekcija>());
        film.setZanr("?");
        film.setNazivFilma("?");
        film.setTrailer("?");
        film.setTrajanje(0);
        film.setFilmId(123L);
        film.setTehnologija("?");

        Sala sala1 = new Sala();
        sala1.setBioskop(new Bioskop());
        sala1.setProjekcije(new HashSet<Projekcija>());
        sala1.setSalaId(123L);
        sala1.setBrojSale(0);
        sala1.setSedista(new HashSet<Sediste>());

        Projekcija projekcija = new Projekcija();
        projekcija.setFilm(film);
        projekcija.setProjekcijaId(123L);
        projekcija.setRasporedSedista(new HashSet<Sediste>());
        projekcija.setSala(sala1);
        projekcija.setRezervacije(new HashSet<Rezervacija>());
        projekcija.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik = new Korisnik();
        korisnik.setEmail("jane.doe@example.org");
        korisnik.setClanKluba(true);
        korisnik.setTipKorisnika("?");
        korisnik.setSifra("?");
        korisnik.setKorisnikId(123L);
        korisnik.setRezervacije(new HashSet<Rezervacija>());
        korisnik.setPrezime("?");
        korisnik.setPoeni(0);
        korisnik.setIme("?");

        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setProjekcija(projekcija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setPotvrdjena(true);
        rezervacija.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija.setRezervacijaId(123L);

        RezervisanaSedista rezervisanaSedista = new RezervisanaSedista();
        rezervisanaSedista.setSediste(sediste);
        rezervisanaSedista.setCenaKarte(10.0);
        rezervisanaSedista.setRezevisanaSedista_id(0L);
        rezervisanaSedista.setRezervacija(rezervacija);

        ArrayList<RezervisanaSedista> rezervisanaSedistaList = new ArrayList<RezervisanaSedista>();
        rezervisanaSedistaList.add(rezervisanaSedista);
        when(this.rezervisanaSedistaRepository.findAll()).thenReturn(rezervisanaSedistaList);

        Film film1 = new Film();
        film1.setOpis("?");
        film1.setProjekcije(new HashSet<Projekcija>());
        film1.setZanr("?");
        film1.setNazivFilma("?");
        film1.setTrailer("?");
        film1.setTrajanje(0);
        film1.setFilmId(123L);
        film1.setTehnologija("?");

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("?");
        bioskop1.setAdresa("?");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("?");
        bioskop1.setBioskopId(123L);

        Sala sala2 = new Sala();
        sala2.setBioskop(bioskop1);
        sala2.setProjekcije(new HashSet<Projekcija>());
        sala2.setSalaId(123L);
        sala2.setBrojSale(0);
        sala2.setSedista(new HashSet<Sediste>());

        Projekcija projekcija1 = new Projekcija();
        projekcija1.setFilm(film1);
        projekcija1.setProjekcijaId(123L);
        projekcija1.setRasporedSedista(new HashSet<Sediste>());
        projekcija1.setSala(sala2);
        projekcija1.setRezervacije(new HashSet<Rezervacija>());
        projekcija1.setPocetakProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));
        projekcija1.setKrajProjekcije(LocalDateTime.of(1, 1, 1, 1, 1));

        Korisnik korisnik1 = new Korisnik();
        korisnik1.setEmail("jane.doe@example.org");
        korisnik1.setClanKluba(true);
        korisnik1.setTipKorisnika("?");
        korisnik1.setSifra("?");
        korisnik1.setKorisnikId(123L);
        korisnik1.setRezervacije(new HashSet<Rezervacija>());
        korisnik1.setPrezime("?");
        korisnik1.setPoeni(0);
        korisnik1.setIme("?");

        Rezervacija rezervacija1 = new Rezervacija();
        rezervacija1.setProjekcija(projekcija1);
        rezervacija1.setKorisnik(korisnik1);
        rezervacija1.setPotvrdjena(true);
        rezervacija1.setRezervisanaSedista(new HashSet<RezervisanaSedista>());
        rezervacija1.setRezervacijaId(123L);

        ArrayList<Rezervacija> rezervacijaList = new ArrayList<Rezervacija>();
        rezervacijaList.add(rezervacija1);
        when(this.rezervacijaRepository.findAll()).thenReturn(rezervacijaList);
        when(this.projekcijaRepository.findAll()).thenReturn(new ArrayList<Projekcija>());

        Bioskop bioskop2 = new Bioskop();
        bioskop2.setGrad("Grad");
        bioskop2.setAdresa("Adresa");
        bioskop2.setSale(new HashSet<Sala>());
        bioskop2.setNaziv("Naziv");
        bioskop2.setBioskopId(123L);
        doNothing().when(this.bioskopRepository).delete((Bioskop) any());
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/brisanjeBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

}

