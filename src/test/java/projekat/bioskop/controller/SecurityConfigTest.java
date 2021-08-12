package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;

public class SecurityConfigTest
{
    @Test
    public void testAuthenticationProvider()
    {
        (new SecurityConfig()).authenticationProvider();
    }
}

