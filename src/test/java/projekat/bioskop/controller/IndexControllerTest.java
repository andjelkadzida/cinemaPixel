package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {IndexController.class})
@ExtendWith(SpringExtension.class)
public class IndexControllerTest {
    @Autowired
    private IndexController indexController;

    @Test
    public void indexTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pocetna");
        MockMvcBuilders.standaloneSetup(this.indexController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("pocetna"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("pocetna"));
    }

    @Test
    public void usloviKoriscenjaTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/usloviKoriscenja");
        MockMvcBuilders.standaloneSetup(this.indexController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("usloviKoriscenja"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("usloviKoriscenja"));
    }

    @Test
    public void politikaPrivatnostiTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/politikaPrivatnosti");
        MockMvcBuilders.standaloneSetup(this.indexController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("politikaPrivatnosti"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("politikaPrivatnosti"));
    }

    @Test
    public void oNamaTest() throws Exception
    {
        final StandaloneMvcTestViewResolver viewResolver = new StandaloneMvcTestViewResolver();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/oNama");
        MockMvcBuilders.standaloneSetup(this.indexController)
                .setViewResolvers(viewResolver)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("oNama"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("oNama"));
    }
}
