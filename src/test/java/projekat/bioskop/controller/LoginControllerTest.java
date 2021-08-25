package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
public class LoginControllerTest
{
    @Autowired
    private LoginController loginController;

    @Test
    public void testLogoutPage() throws Exception
    {
        MockHttpServletRequestBuilder requestBuilder = get("/logout");
        MockMvcBuilders.standaloneSetup(this.loginController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(view().name("redirect:/login?logout"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?logout"));
    }

    @Test
    public void testLoginPage() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = get("/login");
        MockMvcBuilders.standaloneSetup(this.loginController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(view().name("login"))
                .andExpect(forwardedUrl("login"));
    }
}

