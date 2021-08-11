package projekat.bioskop.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projekat.bioskop.model.Sala;
import projekat.bioskop.repository.SalaRepository;

@ContextConfiguration(classes = {SalaService.class})
@ExtendWith(SpringExtension.class)
public class SalaServiceTest
{
    @MockBean
    private SalaRepository salaRepository;

    @Autowired
    private SalaService salaService;

    @Test
    public void testSveSale()
    {
        ArrayList<Sala> salaList = new ArrayList<Sala>();
        when(this.salaRepository.findAll()).thenReturn(salaList);
        List<Sala> actualSveSaleResult = this.salaService.sveSale();
        assertSame(salaList, actualSveSaleResult);
        assertTrue(actualSveSaleResult.isEmpty());
        verify(this.salaRepository).findAll();
    }
}

