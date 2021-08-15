package projekat.bioskop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.model.Bioskop;
import projekat.bioskop.model.Film;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.model.Projekcija;
import projekat.bioskop.model.Rezervacija;
import projekat.bioskop.model.RezervisanaSedista;
import projekat.bioskop.model.Sala;
import projekat.bioskop.model.Sediste;
import projekat.bioskop.repository.BioskopRepository;
import projekat.bioskop.repository.ProjekcijaRepository;
import projekat.bioskop.repository.RezervacijaRepository;
import projekat.bioskop.repository.RezervisanaSedistaRepository;
import projekat.bioskop.repository.SalaRepository;
import projekat.bioskop.repository.SedisteRepository;

@ContextConfiguration(classes = {BioskopController.class})
@ExtendWith(SpringExtension.class)
public class BioskopControllerTest
{
    @Autowired
    private BioskopController bioskopController;

    @MockBean
    private BioskopRepository bioskopRepository;

    @Test
    public void testDodavanjeNovogBioskopa() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Valjevo");
        bioskop.setAdresa("Vlade Danilovica 15");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("New Pixel");
        bioskop.setBioskopId(174L);
        when(this.bioskopRepository.nadjiPoAdresi(anyString(), anyString())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/dodavanjeBioskopa");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop", "poruka"))
                .andExpect(MockMvcResultMatchers.view().name("noviBioskop"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviBioskop"));
    }

    @Test
    public void testIzmenaBioskopa() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);

        Bioskop bioskop1 = new Bioskop();
        bioskop1.setGrad("Novi Beograd");
        bioskop1.setAdresa("Bulevar Milutina Milankovica 13");
        bioskop1.setSale(new HashSet<Sala>());
        bioskop1.setNaziv("Pixel");
        bioskop1.setBioskopId(234L);

        when(this.bioskopRepository.save((Bioskop) any())).thenReturn(bioskop1);
        when(this.bioskopRepository.nadjiPoAdresiId(anyString(), anyString(), (Long) any())).thenReturn(bioskop1);
        when(this.bioskopRepository.nadjiPoAdresi(anyString(), anyString())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/izmenaBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/pregledBioskopaAdmin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/pregledBioskopaAdmin"));
    }

    @Test
    public void testIzmenaBioskopaView() throws Exception
    {
        Bioskop bioskop = new Bioskop();
        bioskop.setGrad("Novi Beograd");
        bioskop.setAdresa("Arsenija Carnojevica 45");
        bioskop.setSale(new HashSet<Sala>());
        bioskop.setNaziv("Pixel");
        bioskop.setBioskopId(234L);
        when(this.bioskopRepository.findByBioskopId((Long) any())).thenReturn(bioskop);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/izmenaBioskopa/{bioskop_id}", 1L);
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("izmenaBioskopa"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("izmenaBioskopa"));
    }

    @Test
    public void testNoviBioskopView() throws Exception
    {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/dodavanjeBioskopa");
        getResult.contentType("Novi bioskop view");
        MockMvcBuilders.standaloneSetup(this.bioskopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bioskop"))
                .andExpect(MockMvcResultMatchers.view().name("noviBioskop"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("noviBioskop"));
    }
}

