package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Bean koji konfigurira filter lanac za Spring Security
    // Ovdje definišemo koji URL-ovi su javni, koji zahtijevaju autentifikaciju,
    // kako se radi login/logout i kako se rukuje CSRF zaštitom.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // dozvoli pristup statičkim resursima i javnim stranicama bez prijave
                        .requestMatchers("/css/**", "/images/**", "/", "/games", "/games/**", "/login").permitAll()
                        // zahtijevaj autentifikaciju za sve /api/** pozive (npr. REST operacije)
                        .requestMatchers("/api/**").authenticated()
                        // sve ostalo zahtijeva autentifikaciju
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // vlastita stranica za login (umjesto default Spring stranice)
                        .loginPage("/login")
                        // defaultSuccessUrl sa 'false' znači: ako postoji spremljeni zahtjev
                        // (saved request), korisnik će biti vraćen na njega; inače ide na /games
                        .defaultSuccessUrl("/games", false)
                        .permitAll()
                )
                .logout(logout -> logout
                        // URL za logout (može se pozvati POST/GET zavisno od konfiguracije forme)
                        .logoutUrl("/logout")
                        // nakon logout-a redirektuj na početnu stranicu
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // Napomena: ovde izuzimamo CSRF provjeru za /api/** rute.
                // To je često potrebno kada klijentski JS radi fetch/POST/DELETE prema REST API-ju
                // bez CSRF tokena. Ovo predstavlja sigurnosni kompromis i treba ga koristiti
                // samo ako je namjera omogućiti takav pristup (npr. API koji koristi token-based auth).
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }

    // Bean koji pruža korisničke podatke za autentifikaciju.
    // Ovde koristimo InMemoryUserDetailsManager sa dva korisnika (admin i user) radi jednostavnosti.
    // U produkciji biste obično koristili bazu podataka ili eksterni provider.
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Kreiramo admin korisnika sa ulogom ADMIN
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN")
                .build();

        // Kreiramo običnog korisnika sa ulogom USER
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("userpass"))
                .roles("USER")
                .build();

        // Vraćamo InMemoryUserDetailsManager koji drži ova dva korisnika
        return new InMemoryUserDetailsManager(admin, user);
    }

    // Bean za enkodiranje lozinki. BCrypt je dobar izbor - sporije je, ali sigurnije.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
