package projekat.bioskop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BCrypt.class})
@ExtendWith(SpringExtension.class)
public class BCryptTest
{
    @Autowired
    private BCrypt bCrypt;

    @Test
    public void testPasswordEncoder()
    {
        this.bCrypt.passwordEncoder();
    }
}

