package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

import java.time.LocalDateTime;
import java.util.List;

//Kontoler Menadzer
@Controller
public class MenadzerController
{
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    RezervisanaSedistaRepository rezervisanaSedistaRepository;

    @RequestMapping(value = "/izvestajRezervacija")
    public String listaSvihRezervacija(Model model)
    {
        LocalDateTime datum = LocalDateTime.now();
        int potvrdjene=0;
        int otkazane=0;
        int bezStatusa = 0;
        double zarada=0;
        List<Rezervacija> rezervacijas = rezervacijaRepository.findAll();
        List<RezervisanaSedista> rezervisanaSedistaSet = rezervisanaSedistaRepository.findAll();
        for(RezervisanaSedista rs: rezervisanaSedistaSet)
        {
            zarada+=rs.getCenaKarte();
            rs.getRezervacija().getProjekcija().getSala().getBioskop().getGrad();
            rs.getRezervacija().getProjekcija().getFilm().getNazivFilma();
            rs.getRezervacija().getProjekcija().getFilm().getZanr();
            rs.getRezervacija().getProjekcija().getFilm().getTehnologija();
            rs.getRezervacija().getProjekcija().getFilm().getTrajanje();
            rs.getRezervacija().getProjekcija().getPocetakProjekcije();
            rs.getRezervacija().getProjekcija().getSala().getBrojSale();
            rs.getSediste().getBrojSedista();
            model.addAttribute("rezervisanaSedistaSet", rezervisanaSedistaSet);
        }
        for(Rezervacija r: rezervacijas)
        {
            if(r.getPotvrdjena()==null)
            {
                bezStatusa++;
            }
            else if(r.getPotvrdjena())
            {
                potvrdjene++;
            }
            else
            {
                otkazane++;
            }
            r.getProjekcija().getSala().getBioskop().getGrad();
            r.getProjekcija().getFilm().getNazivFilma();
            r.getProjekcija().getFilm().getZanr();
            r.getProjekcija().getFilm().getTehnologija();
            r.getProjekcija().getFilm().getTrajanje();
            r.getProjekcija().getPocetakProjekcije();
            r.getProjekcija().getSala().getBrojSale();
            model.addAttribute("rezervacijas", rezervacijas);
            model.addAttribute("otkazane", otkazane);
            model.addAttribute("potvrdjene", potvrdjene);
            model.addAttribute("bezStatusa", bezStatusa);
            model.addAttribute("zarada", zarada);
            model.addAttribute("datum", datum);
        }
        return "izvestajRezervacija";
    }
}
