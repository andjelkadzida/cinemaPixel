package projekat.bioskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/pocetna", "/error", "/pregledFilmova/**", "/pregledProjekcija/**", "/pregledBioskopa/**", "/static/**", "/images/**", "/usloviKoriscenja/**", "/politikaPrivatnosti/**", "/oNama/**", "/favicon.ico").permitAll()
                        .requestMatchers("/administracijaClanova/**", "updateKorisnika/**", "/dodavanjeFilmova/**", "/dodavanjeBioskopa/**", "/novaProjekcija/**", "/pregledBioskopaAdmin/**", "/izmenaBioskopa/**", "/administriranjeClanova/**", "/updateKorisnika/**", "/pregledProjekcijaAdmin/**", "/pregledFilmovaAdmin/**", "/izmenaProjekcija/**", "/otkazivanjeProjekcija/**", "/izmenaFilmova/**","/brisanjeFilmova/**").hasAuthority("ADMIN")
                        .requestMatchers("/izborSedista/**","/selektovanaSedista/**", "/mojeRezervacije/**", "/korisnickiProfil/**").hasAuthority("KORISNIK")
                        .requestMatchers("/izvestajRezervacija/**").hasAuthority("MENADŽER")
                        .requestMatchers("/registracija").anonymous()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/pocetna", true)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }
}