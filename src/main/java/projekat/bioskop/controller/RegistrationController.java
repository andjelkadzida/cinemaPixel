package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import projekat.bioskop.model.Korisnik;
import org.springframework.web.servlet.ModelAndView;
import projekat.bioskop.services.KorisnikService;

import javax.validation.Valid;

@Controller
public class RegistrationController
{
    @Autowired
    KorisnikService korisnikService;

    @RequestMapping(value = "/registracija", method = RequestMethod.GET)
    public ModelAndView registracija()
    {
        ModelAndView modelAndView = new ModelAndView();
        Korisnik korisnik = new Korisnik();
        modelAndView.addObject("korisnik", korisnik);
        modelAndView.setViewName("registracija");
        return modelAndView;
    }

    @RequestMapping(value = "/registracija", method = RequestMethod.POST)
    public ModelAndView registracijaKorisnika(@Valid Korisnik korisnik, BindingResult bindingResult, ModelMap modelMap)
    {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors())
        {
            modelAndView.addObject("poruka", "Molimo Vas da ispravite greške");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if (korisnikService.postojiKorisnik(korisnik))
        {
            modelAndView.addObject("poruka", "Korisnik sa ovim emailom je već registrovan!");
        }
        else
        {
            korisnikService.sacuvajKorisnika(korisnik);
            //modelAndView.addObject("poruka", "Registrovali ste se");
            modelAndView.setViewName("login");
        }
        modelAndView.addObject("korisnik", new Korisnik());
        modelAndView.setViewName("registracija");
        return modelAndView;
    }

}
