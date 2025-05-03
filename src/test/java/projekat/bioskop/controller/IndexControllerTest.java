package projekat.bioskop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {IndexController.class})
@ExtendWith(SpringExtension.class)
class IndexControllerTest {
    private static final String URL_HOME = "/pocetna";
    private static final String URL_TERMS = "/usloviKoriscenja";
    private static final String URL_PRIVACY = "/politikaPrivatnosti";
    private static final String URL_ABOUT = "/oNama";

    @Autowired
    private IndexController indexController;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(indexController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        performGetRequestAndVerifyResponse(URL_HOME, "pocetna");
    }

    @Test
    void shouldDisplayTermsOfService() throws Exception {
        performGetRequestAndVerifyResponse(URL_TERMS, "usloviKoriscenja");
    }

    @Test
    void shouldDisplayPrivacyPolicy() throws Exception {
        performGetRequestAndVerifyResponse(URL_PRIVACY, "politikaPrivatnosti");
    }

    @Test
    void shouldDisplayAboutPage() throws Exception {
        performGetRequestAndVerifyResponse(URL_ABOUT, "oNama");
    }

    private void performGetRequestAndVerifyResponse(String url, String viewName) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(model().size(0))
                .andExpect(view().name(viewName))
                .andExpect(forwardedUrl(viewName));
    }
}