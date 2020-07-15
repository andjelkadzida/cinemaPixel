package projekat.bioskop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.*;
import projekat.bioskop.services.FilmService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class FilmController
{
    @Autowired
    FilmService filmService;
    @Autowired
    ProjekcijaRepository projekcijaRepository;
    @Autowired
    KorisnikRepository korisnikRepository;
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    SedisteRepository sedisteRepository;
    @Autowired
    RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @RequestMapping("/pregledFilmova")
    public String spisakFilmova(Model model)
    {
        List<Film> filmovi = this.filmService.sviFilmovi();
        model.addAttribute("film", filmovi);
        return "pregledFilmova";
    }
    @RequestMapping("/pregledProjekcija/{film}")
    public String spisakProjekcija(Model model, @PathVariable("film") String izabranFilm)
    {
        Set<Projekcija> projekcija = projekcijaRepository.projekcijaPoFilmu(izabranFilm);
        model.addAttribute("projekcija", projekcija);
        LocalDateTime danas = LocalDateTime.now();
        model.addAttribute("danas", danas);
        return "pregledProjekcija";
    }
    @RequestMapping(value = "/izborSedista", method = RequestMethod.POST)
    public String izaberiSedista(Model model, @RequestParam("projekcijaId") Long projekcijaId, Authentication authentication)
    {
        Korisnik k = korisnikRepository.findByEmail(authentication.getName());
        model.addAttribute("korisnik", k.getClanKluba());
        model.addAttribute("projekcijaId", projekcijaId);
        Set<Sediste> sedista =  sedisteRepository.pronadjiSva();
        model.addAttribute( "sedista", sedista);
        Set<Sediste> rs = projekcijaRepository.nadjiSva(projekcijaId);
        model.addAttribute("rs", rs);
        return "izborSedista";
    }
    @RequestMapping(value = "/selektovanaSedista", method = RequestMethod.POST)
    public String izabranaSedista(Model model, @RequestParam("projekcijaId") Long projekcijaId, Authentication authentication, @RequestParam(name = "sediste") Set<Long>selektovanaSedista)
    {
        Korisnik k = korisnikRepository.findByEmail(authentication.getName());
        Projekcija p = projekcijaRepository.getOne(projekcijaId);
        Set<Sediste> zauzeto = p.getRasporedSedista();
        Set<RezervisanaSedista> rezervisano = new HashSet<>();
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setKorisnik(k);
        rezervacija.setProjekcija(p);
        Set<Sediste> novi = new HashSet<>();
        for(Long l: selektovanaSedista)
        {
            novi.add(sedisteRepository.getOne(l));
        }
        for(Sediste s : novi)
        {
            RezervisanaSedista rezervisanaSedista = new RezervisanaSedista(rezervacija, s);
            rezervisano.add(rezervisanaSedista);
            zauzeto.add(s);
        }
        p.setRasporedSedista(zauzeto);
        projekcijaRepository.save(p);
        rezervacija.setRezervisanaSedista(rezervisano);
        rezervacijaRepository.save(rezervacija);
        for(RezervisanaSedista rezervisanaSedista : rezervisano)
        {
            rezervisanaSedistaRepository.save(rezervisanaSedista);
        }
        //Dodavanje poena korisniku prema tome da li je clan kluba
        if(k.getClanKluba()==true)
        {
            int poeni = 40;
            int noviP = k.getPoeni() + poeni;
            k.setPoeni(noviP);
            rezervacija.setCenaKarte(1000);
            korisnikRepository.save(k);
        }
        else
        {
            int poeni = 20;
            int noviP = k.getPoeni() + poeni;
            k.setPoeni(noviP);
            rezervacija.setCenaKarte(500);
            korisnikRepository.save(k);
        }
        return  "redirect:/mojeRezervacije";
    }
}
