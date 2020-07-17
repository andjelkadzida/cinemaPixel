package projekat.bioskop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    JavaMailSender javaMailSender;
    @Autowired
    public FilmController(JavaMailSender javaMailSender)
    {
        this.javaMailSender=javaMailSender;
    }
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
            //Dodavanje poena korisniku prema tome da li je rezervisano specijalno sediste i setovanje cene karte prema sedistima
            if(rezervisanaSedista.getSediste().getTipSedista().equals("Specijalno"))
            {
                int poeni = 40;
                int noviP = k.getPoeni() + poeni;
                k.setPoeni(noviP);
                rezervisanaSedista.setCenaKarte(1000);
                korisnikRepository.save(k);
            }
            else
            {
                int poeni = 20;
                int noviP = k.getPoeni() + poeni;
                k.setPoeni(noviP);
                rezervisanaSedista.setCenaKarte(500);
                korisnikRepository.save(k);
            }
            rezervisanaSedistaRepository.save(rezervisanaSedista);
            try
            {
                //slanje maila
                String mailKorisnika = k.getEmail();
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(mailKorisnika);
                mail.setSubject("Nova rezervacija");
                mail.setText("Zdravo " + k.getIme() + "!" + "\n" + "U naš bioskop je pristigla tvoja rezervacija!" + "\n"  +  "Podaci o bioskopu: " + "\n" +
                "Naziv bioskopa: " +  p.getSala().getBioskop().getNaziv() + ", adresa: " + p.getSala().getBioskop().getAdresa() + ", grad: " + p.getSala().getBioskop().getGrad() + "\n" + "Podaci o filmu:" + "\n" + "Naziv filma: " + p.getFilm().getNazivFilma() + ", zanr: " + p.getFilm().getZanr() + ", tehnologija: " +  p.getFilm().getTehnologija() + ", trajanje: " + p.getFilm().getTrajanje() + " minuta" + "\n" + "Podaci o projekciji: " + "\n" + "Datum projekcije: " + p.getPocetakProjekcije().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ", početak projekcije: " + p.getPocetakProjekcije().toLocalTime() + "h" + ", kraj projekcije " +p.getKrajProjekcije().toLocalTime() + "h" + "\n" + "Broj sale: " + p.getSala().getBrojSale() + "\n" + "Sedišta: " + rezervisanaSedista.getSediste().getSedisteId() + "\n" + "Cena karte: " + rezervisanaSedista.getCenaKarte() + " RSD");
                javaMailSender.send(mail);
            }
            catch (MailException exception)
            {

            }
        }
        return  "redirect:/mojeRezervacije";
    }
}
