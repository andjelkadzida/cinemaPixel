package projekat.bioskop.services;

import projekat.bioskop.model.Korisnik;

import java.util.Optional;

public interface IKorisnikService
{
    Optional<Korisnik> findById(Long id);

    void sacuvajKorisnika(Korisnik korisnik);

    boolean postojiKorisnik(Korisnik korisnik);
}