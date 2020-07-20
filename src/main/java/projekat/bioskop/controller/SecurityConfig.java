package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/pocetna", "/error", "/pregledFilmova/**", "/pregledProjekcija/**", "/pregledBioskopa/**", "/static/**", "/images/**", "/usloviKoriscenja/**", "/politikaPrivatnosti/**", "/oNama/**").permitAll()
                .antMatchers("/administracijaClanova/**", "updateKorisnika/**", "/dodavanjeFilmova/**", "/dodavanjeBioskopa/**", "/novaProjekcija/**", "/pregledBioskopaAdmin/**", "/izmenaBioskopa/**", "/administriranjeClanova/**", "/updateKorisnika/**", "/pregledProjekcijaAdmin/**", "/pregledFilmovaAdmin/**", "/izmenaProjekcija/**", "/otkazivanjeProjekcija/**", "/izmenaFilmova/**","/brisanjeFilmova/**").hasAuthority("ADMIN")
                .antMatchers("/izborSedista/**","/selektovanaSedista/**", "/mojeRezervacije/**", "/korisnickiProfil/**").hasAuthority("KORISNIK")
                .antMatchers("/izvestajRezervacija/**").hasAuthority("MENADŽER")
                .antMatchers("/registracija").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/pocetna", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");
    }
}
