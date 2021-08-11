package projekat.bioskop.controller;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import projekat.bioskop.model.Korisnik;
import projekat.bioskop.services.KorisnikService;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RegistrationController.class})
@ExtendWith(SpringExtension.class)
class RegistrationControllerTest
{

    @MockBean
    private KorisnikService korisnikService;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void testModelAndViewRegistracija() throws Exception
    {
        RegistrationController registrationController = new RegistrationController();
        ModelAndView modelAndView = registrationController.registracija();
        Assertions.assertEquals("registracija", modelAndView.getViewName());
    }

    @Test
    public void testRegistracija() throws Exception
    {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.registrationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testRegistracijaKorisnika() throws Exception
    {
        doNothing().when(this.korisnikService).sacuvajKorisnika((projekat.bioskop.model.Korisnik) any());
        when(this.korisnikService.postojiKorisnik((projekat.bioskop.model.Korisnik) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registracija");
        MockMvcBuilders.standaloneSetup(this.registrationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("login"));
    }

}