package projekat.bioskop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.bioskop.model.Sala;
import projekat.bioskop.repository.SalaRepository;

import java.util.List;

@Service
public class SalaService
{
    @Autowired
    SalaRepository salaRepository;

    public List<Sala> sveSale()
    {
       return this.salaRepository.findAll();
    }
}
