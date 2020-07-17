package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.repository.FilmRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class DodavanjeFilmController
{
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    ProjekcijaRepository projekcijaRepository;
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @RequestMapping(value = "/dodavanjeFilmova", method = RequestMethod.GET)
    public ModelAndView noviFilmView()
    {
        ModelAndView modelAndView = new ModelAndView();
        Film film = new Film();
        modelAndView.addObject("film", film);
        modelAndView.setViewName("noviFilm");
        return modelAndView;
    }
    @RequestMapping(value = "/dodavanjeFilmova", method = RequestMethod.POST)
    public ModelAndView dodavanjeNovogFilma(Film film, BindingResult bindingResult, ModelMap modelMap)
    {
        Film f = filmRepository.nadjiPoNazivuFilma(film.getNazivFilma());
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors())
        {
            modelAndView.addObject("poruka", "Molimo Vas da ispravite greške");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if (f!=null)
        {
            modelAndView.addObject("poruka", "Film sa unetim naslovom već postoji!");
        }
        else
        {
            filmRepository.save(film);
            modelAndView.addObject("poruka", "Uspešno ste dodali novi film!");
            modelAndView.setViewName("noviFilm");
        }
        modelAndView.addObject("film", new Film());
        modelAndView.setViewName("noviFilm");
        return modelAndView;
    }
    @RequestMapping(value = "/izmenaFilmova/{film_id}", method = RequestMethod.GET)
    public String izmenaFilmovaView(Model model, @PathVariable("film_id") Long film_id)
    {
        Film film = filmRepository.getOne(film_id);
        model.addAttribute("film", film);
        return "izmenaFilmova";
    }
    @RequestMapping(value = "/izmenaFilmova/{film_id}", method = RequestMethod.POST)
    public String izmenaFilmova(Model model, @PathVariable("film_id") Long film_id, @Valid Film film)
    {
        film.setFilmId(film_id);
        Film f = filmRepository.nadjiPoNazivuFilma(film.getNazivFilma());
        Film f1 = filmRepository.nadjiPoNazivuFilmaId(film.getNazivFilma(), film.getFilmId());
        String msg;
        if(f1!=null)
        {
            filmRepository.save(film);
            return "redirect:/pregledFilmovaAdmin";

        }
        else if(f!=null)
        {
            msg = "Film sa ovim nazivom već postoji";
            model.addAttribute("msg", msg);
            return "izmenaFilmova";
        }
        else
        {
            filmRepository.save(film);
            return "redirect:/pregledFilmovaAdmin";
        }
    }
    @GetMapping(value = "/brisanjeFilmova/{film_id}")
    public String brisanjeFilmova(@PathVariable("film_id") Long film_id)
    {
        Film f = filmRepository.getOne(film_id);
        Set<Projekcija> projekcije = projekcijaRepository.nadjiPoIdFilma(film_id);
        List<Rezervacija> rezervacije = rezervacijaRepository.findAll();
        List<RezervisanaSedista> rezervisanaSedistas = rezervisanaSedistaRepository.findAll();
        for(Rezervacija r: rezervacije)
        {
            for(RezervisanaSedista rs : rezervisanaSedistas)
            {
                if(rs.getRezervacija().getProjekcija().getFilm().getFilmId()==film_id)
                {
                    rezervisanaSedistaRepository.delete(rs);
                }
            }
            if(r.getProjekcija().getFilm().getFilmId()==film_id)
            {
                rezervacijaRepository.delete(r);
            }
        }
        for(Projekcija p: projekcije)
        {
            projekcijaRepository.delete(p);
        }
        filmRepository.delete(f);
        return "redirect:/pregledFilmovaAdmin";
    }
}
