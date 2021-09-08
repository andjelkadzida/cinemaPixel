package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @Autowired
    RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @RequestMapping("/mojeRezervacije")
    public String rezervacijeKorisnika(Model model, Authentication authentication)
    {
        LocalDateTime trenutno = LocalDateTime.now();
        model.addAttribute("trenutno",trenutno);
        Korisnik k = korisnikRepository.findByEmail(authentication.getName());
        model.addAttribute("k", k);
        Set<RezervisanaSedista> rezervisanaSedistaSet = rezervacijaRepository.nadjiPoEmailu(authentication.getName());
        for(RezervisanaSedista rs: rezervisanaSedistaSet)
        {
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
        Set<Rezervacija> rezervacije = korisnik.getRezervacije();
        for(Rezervacija r : rezervacije)
        {
            Projekcija projekcija = r.getProjekcija();
            Set<RezervisanaSedista> rezervisanaSedista = r.getRezervisanaSedista();
            Set<Sediste> sedista = new HashSet<>();
            for(RezervisanaSedista rs : rezervisanaSedista)
            {
                sedista.add(rs.getSediste());
            }
            projekcija.getRasporedSedista().removeAll(sedista);
            projekcijaRepository.save(projekcija);
            rezervisanaSedistaRepository.deleteAll(rezervisanaSedista);
        }
        rezervacijaRepository.deleteAll(rezervacije);
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
