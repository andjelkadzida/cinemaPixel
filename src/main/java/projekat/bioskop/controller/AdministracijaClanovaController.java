package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projekat.bioskop.model.*;
import projekat.bioskop.repository.KorisnikRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.services.KorisnikService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdministracijaClanovaController
{
    private KorisnikRepository korisnikRepository;
    @Autowired
    private KorisnikService korisnikService;
    @Autowired
    private ProjekcijaRepository projekcijaRepository;
    @Autowired
    private RezervisanaSedistaRepository rezervisanaSedistaRepository;
    @Autowired
    RezervacijaRepository rezervacijaRepository;
    @Autowired
    public AdministracijaClanovaController(KorisnikRepository korisnikRepository)
    {
        this.korisnikRepository = korisnikRepository;
    }

    @GetMapping("/korisnici/update/{korisnik_id}")
    public String formaZaUpdate(@PathVariable("korisnik_id") Long korisnik_id, Model model)
    {
        Korisnik korisnik = korisnikRepository.findById(korisnik_id)
                .orElseThrow(() -> new IllegalArgumentException("Neispravan ID korisnika:" + korisnik_id));
        model.addAttribute("korisnik", korisnik);
        return "updateKorisnika";
    }


    @PostMapping("/korisnici/update/{korisnik_id}")
    public String updateKorisnika(@PathVariable("korisnik_id") Long korisnik_id, @Valid Korisnik korisnik, Model model)
    {
        korisnik.setKorisnikId(korisnik_id);
        korisnikRepository.save(korisnik);
        model.addAttribute("korisnici", korisnikRepository.findAll());
        return "updateKorisnika";
    }

    @GetMapping("/korisnici/delete/{korisnik_id}")
    public String brisanjeKorisnika(@PathVariable("korisnik_id") long korisnik_id, Model model)
    {
        Korisnik korisnik = korisnikRepository.getOne(korisnik_id);
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
        model.addAttribute("korisnik", korisnikRepository.findAll());
        return "administriranjeClanova";
    }
    @RequestMapping("/administriranjeClanova/korisnici")
    public String spisakKorisnika(Model model)
    {
        List<Korisnik> korisnik = this.korisnikService.findAll();
        model.addAttribute("korisnik", korisnik);
        return "administriranjeClanova";
    }
}
