package projekat.bioskop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
class LoginControllerTest {
    private static final String LOGIN_PATH = "/login";
    private static final String LOGOUT_PATH = "/logout";
    private static final String LOGIN_VIEW = "login";
    private static final String LOGOUT_REDIRECT = "redirect:/login?logout";

    @Autowired
    private LoginController loginController;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void shouldRedirectToLoginPageOnLogout() throws Exception {
        // when
        performGet(LOGOUT_PATH)
            // then
            .andExpect(status().isFound())
            .andExpect(model().size(0))
            .andExpect(view().name(LOGOUT_REDIRECT))
            .andExpect(redirectedUrl(LOGIN_PATH + "?logout"));
    }

    @Test
    void shouldDisplayLoginPage() throws Exception {
        // given
        StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(viewResolver)
                .build();

        // when
        performGet(LOGIN_PATH)
            // then
            .andExpect(status().isOk())
            .andExpect(model().size(0))
            .andExpect(view().name(LOGIN_VIEW))
            .andExpect(forwardedUrl(LOGIN_VIEW));
    }

    private ResultActions performGet(String path) throws Exception {
        MockHttpServletRequestBuilder request = get(path);
        return mockMvc.perform(request);
    }
}