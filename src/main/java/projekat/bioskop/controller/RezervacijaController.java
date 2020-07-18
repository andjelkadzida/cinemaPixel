package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RezervacijaController
{
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @Autowired
    KorisnikRepository korisnikRepository;
    @Autowired
    ProjekcijaRepository projekcijaRepository;

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void automatskoOtkazivanjeRezervacije()
    {
        Set<Projekcija>projekcije = projekcijaRepository.sveProjekcije();
        Set<Korisnik> korisnici = korisnikRepository.sviKorisnici();
        Set<Rezervacija> rezervacije = rezervacijaRepository.sveRezervacije();
        LocalDateTime trenutno = LocalDateTime.now();
        for(Rezervacija r:rezervacije)
        {
            Set<RezervisanaSedista> rezervisanaSedistas = rezervacijaRepository.nadjiPoIdRezervacijeSet(r.getRezervacijaId());
            Projekcija p = projekcijaRepository.getOne(r.getProjekcija().getProjekcijaId());
            Set<Sediste> sedisteSet = p.getRasporedSedista();
            Set<Sediste> nov = new HashSet<>();
            long minutes = Duration.between(trenutno, r.getProjekcija().getPocetakProjekcije()).toMinutes();
            if(minutes<=30)
            {
                if(r.getPotvrdjena()==null)
                {
                    for(Korisnik k:korisnici)
                    {
                        for(Sediste s:sedisteSet)
                        {
                            for(RezervisanaSedista rs: rezervisanaSedistas)
                            {
                                if(rs.getSediste().getTipSedista().equals("Specijalno"))
                                {
                                    if(k.getPoeni()>0)
                                    {
                                        int poeni = k.getPoeni() - 40;
                                        k.setPoeni(poeni);
                                    }
                                    else
                                    {
                                        k.setPoeni(0);
                                    }
                                    korisnikRepository.save(k);
                                }
                                else
                                {
                                    if(k.getPoeni()>0)
                                    {
                                        int poeni = k.getPoeni() - 20;
                                        k.setPoeni(poeni);
                                    }
                                    else
                                    {
                                        k.setPoeni(0);
                                    }
                                    korisnikRepository.save(k);
                                }
                                nov.add(s);
                                rezervisanaSedistaRepository.delete(rs);
                            }
                        }
                    }
                    sedisteSet.removeAll(nov);
                    p.setRasporedSedista(sedisteSet);
                    projekcijaRepository.save(p);
                    r.setPotvrdjena(false);
                    rezervacijaRepository.save(r);
                }
            }
        }
    }

    @RequestMapping(value = "/mojeRezervacije/delete/{rezervacija_id}", method = RequestMethod.GET)
    public String otkazivanjeRezervacije(@PathVariable("rezervacija_id") Long rezervacija_id, Authentication auth)
    {
        Korisnik k = korisnikRepository.findByEmail(auth.getName());
        Set<RezervisanaSedista> rs = rezervacijaRepository.nadjiPoIdRezervacijeSet(rezervacija_id);
        Rezervacija rezervacija = rezervacijaRepository.findByRezervacijaId(rezervacija_id);
        Long idProjekcije = rezervacija.getProjekcija().getProjekcijaId();
        Projekcija p = projekcijaRepository.getOne(idProjekcije);
        Set<RezervisanaSedista> rezervisanaSedista = rezervacija.getRezervisanaSedista();
        Set<Sediste> sedista = p.getRasporedSedista();
        Set<Sediste> xy = new HashSet<>();
        for(Sediste s: sedista)
        {
            for(RezervisanaSedista rss:rezervisanaSedista)
            {
                if(s.getSedisteId()==rss.getSediste().getSedisteId())
                {

                    xy.add(s);
                    if(s.getTipSedista().equals("Specijalno"))
                    {
                        if(k.getPoeni()>0)
                        {
                            int poeni = k.getPoeni() - 40;
                            k.setPoeni(poeni);
                        }
                        else
                        {
                            k.setPoeni(0);
                        }
                        korisnikRepository.save(k);
                    }
                    else
                    {
                        if(k.getPoeni()>0)
                        {
                            int poeni = k.getPoeni() - 20;
                            k.setPoeni(poeni);
                        }
                        else
                        {
                            k.setPoeni(0);
                        }
                        korisnikRepository.save(k);
                    }
                }
            }
        }
        sedista.removeAll(xy);
        p.setRasporedSedista(sedista);
        rezervacija.setPotvrdjena(false);
        rezervacijaRepository.save(rezervacija);
        projekcijaRepository.save(p);
        for(RezervisanaSedista rezervisanaSedistaa:rs)
        {
            rezervisanaSedistaRepository.delete(rezervisanaSedistaa);
        }
        return "redirect:/mojeRezervacije";
    }
    @GetMapping(value = "/mojeRezervacije/potvrdi/{rezervacija_id}")
    public String potvrdaRezervacije(Model model, @PathVariable("rezervacija_id") Long rezervacija_id)
    {
        Rezervacija rezervacija = rezervacijaRepository.findByRezervacijaId(rezervacija_id);
        rezervacija.setPotvrdjena(true);
        rezervacijaRepository.save(rezervacija);
        return "redirect:/mojeRezervacije";
    }

}
