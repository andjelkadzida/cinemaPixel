package projekat.bioskop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BioskopasApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BioskopasApplication.class, args);
    }
}
