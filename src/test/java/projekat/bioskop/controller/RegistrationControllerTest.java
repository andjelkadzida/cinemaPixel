package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
    public void testRegistracija() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/registracija");
        MockMvcBuilders.standaloneSetup(this.registrationController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("registracija"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("registracija"));
    }

    @Test
    public void testRegistracijaKorisnika() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        doNothing().when(this.korisnikService).sacuvajKorisnika((projekat.bioskop.model.Korisnik) any());
        when(this.korisnikService.postojiKorisnik((projekat.bioskop.model.Korisnik) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registracija");
        MockMvcBuilders.standaloneSetup(this.registrationController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("korisnik"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("login"));
    }
}