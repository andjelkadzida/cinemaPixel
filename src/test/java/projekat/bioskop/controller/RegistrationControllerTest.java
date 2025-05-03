package projekat.bioskop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projekat.bioskop.services.KorisnikService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {RegistrationController.class})
@ExtendWith(SpringExtension.class)
class RegistrationControllerTest {
    private static final String REGISTRATION_PATH = "/registracija";
    private static final String REGISTRATION_VIEW = "registracija";
    private static final String LOGIN_VIEW = "login";
    private static final String KORISNIK_ATTRIBUTE = "korisnik";

    @MockitoBean
    private KorisnikService korisnikService;

    @Autowired
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void whenGetRegistration_thenShowRegistrationForm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REGISTRATION_PATH);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attributeExists(KORISNIK_ATTRIBUTE))
                .andExpect(view().name(REGISTRATION_VIEW))
                .andExpect(forwardedUrl(REGISTRATION_VIEW));
    }

    @Test
    void whenPostValidRegistration_thenRedirectToLogin() throws Exception {
        doNothing().when(korisnikService).sacuvajKorisnika(any());
        when(korisnikService.postojiKorisnik(any())).thenReturn(false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(REGISTRATION_PATH);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attributeExists(KORISNIK_ATTRIBUTE))
                .andExpect(view().name(LOGIN_VIEW))
                .andExpect(forwardedUrl(LOGIN_VIEW));
    }
}