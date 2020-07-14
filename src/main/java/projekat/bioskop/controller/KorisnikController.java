package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.SedisteRepository;

import javax.validation.Valid;
import java.util.Set;


@Controller
public class KorisnikController
{
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    ProjekcijaRepository projekcijaRepository;
    @Autowired
    KorisnikRepository korisnikRepository;
    @Autowired
    SedisteRepository sedisteRepository;

    @RequestMapping("/mojeRezervacije")
    public String rezervacijeKorisnika(Model model, Authentication authentication)
    {
        Korisnik k = korisnikRepository.findByEmail(authentication.getName());
        model.addAttribute("k", k);
        Set<RezervisanaSedista> rezervisanaSedistaSet = rezervacijaRepository.nadjiPoEmailu(authentication.getName());
        for(RezervisanaSedista rs: rezervisanaSedistaSet)
        {
          rs.getRezervacija().getProjekcija().getSala().getBioskop().getGrad();
            rs.getRezervacija().getProjekcija().getFilm().getNazivFilma();
            rs.getRezervacija().getProjekcija().getFilm().getZanr();
            rs.getRezervacija().getProjekcija().getFilm().getTehnologija();
            rs.getRezervacija().getProjekcija().getFilm().getTrajanje();
            rs.getRezervacija().getProjekcija().getPocetakProjekcije();
            rs.getRezervacija().getProjekcija().getSala().getBrojSale();
            rs.getSediste().getSedisteId();
            model.addAttribute("rezervisanaSedistaSet", rezervisanaSedistaSet);
        }
        return "mojeRezervacije";
    }

    @RequestMapping("/korisnickiProfil")
    public String korisnickiProfil(Model model, Authentication authentication)
    {
        Korisnik korisnik = korisnikRepository.findByEmail(authentication.getName());
        model.addAttribute("korisnik", korisnik);
        return "korisnickiProfil";
    }
    @PostMapping("/korisnickiProfil/delete")
    public String brisanjeProfila(@Valid Korisnik korisnik, Authentication authentication, Model model)
    {
        String ulogovaniKorisnik = authentication.getName();
        korisnik = korisnikRepository.findByEmail(ulogovaniKorisnik);
        korisnikRepository.delete(korisnik);
        return "redirect:/logout";
    }
    @RequestMapping("/korisnickiProfil/update")
    public String updateProfilaView(Model model, Authentication authentication)
    {
        Korisnik korisnik = korisnikRepository.findByEmail(authentication.getName());
        model.addAttribute("korisnik", korisnik);
        return "izmenaKorisnickogProfila";
    }
    @PostMapping("/korisnickiProfil/update")
    public String updateKorisnickogProfila(@Valid Korisnik korisnik, Model model, Authentication authentication)
    {
        korisnik.setEmail(authentication.getName());
        korisnikRepository.save(korisnik);
        model.addAttribute("korisnici", korisnikRepository.findAll());
        return "korisnickiProfil";
    }
}
