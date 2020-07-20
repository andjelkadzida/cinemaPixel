package projekat.bioskop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController
{
    @RequestMapping("/pocetna")
    public String index (Model model)
    {
        return "pocetna";
    }

    @RequestMapping("/usloviKoriscenja")
    public String usloviKoriscenja (Model model)
    {
        return "usloviKoriscenja";
    }

    @RequestMapping("/politikaPrivatnosti")
    public String politikaPrivatnosti (Model model)
    {
        return "politikaPrivatnosti";
    }

    @RequestMapping("/oNama")
    public String oNama (Model model)
    {
        return "oNama";
    }
}
