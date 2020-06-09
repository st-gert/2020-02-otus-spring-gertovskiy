package ru.otus.job12.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import ru.otus.job12.security.training.UserExtractorFilter;

import java.security.SecureRandom;

/**
 * Настройки безопасности.
 *
 *  Права (на развитие)
 *    Роли           Чтение      Добавление,    Чтение       Добавление,      Чтение,         Удаление
 *                   списков     изменение,     списков      изменение,       добавление,     отзывов
 *                   книг        удаление       жанров,      удаление         изменение
 *                               книг           авторов      жанров, авт.     отзывов
 *   ANONYMOUS         +           -               -             -               -               -
 *   USER              +           -               +             -               +               -
 *   ADMIN             +           +               +             +               -               -
 *   REVIEW            +           -               -             -               +               +
 *
 *  Пользователи     Роли
 *    owner         ADMIN, REVIEW
 *    admin         ADMIN
 *    user          USER
 *    friend        USER
 *    anonymous     ANONYMOUS
 *
 *    Пароли всех пользователей, кроме anonymous - "123".
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/")
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                // Требование: Авторизация на всех страницах - для всех аутентифицированных
                .and()
                .authorizeRequests().antMatchers("/**").authenticated()

                .and()
                .formLogin()

                .and()
                .logout()
                .logoutUrl("/logout")

                .and()
                .rememberMe()
                .key("Q5y7b9*/")
                .tokenValiditySeconds(31_536_000)   // год

                .and()
                .anonymous().authorities("ROLE_ANONYMOUS").principal(new AnonymousUserDetails())

                .and()
                .exceptionHandling().accessDeniedPage("/access-denied.html")

                //Фильтр для просмотра свойств пользователя. Добавлен в учебных целях.
                .and()
                .addFilterBefore(new UserExtractorFilter(), FilterSecurityInterceptor.class)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Очень сложный расчет, работает долго. Оставлен в экспериментальных целях.
        return new  BCryptPasswordEncoder(15, new SecureRandom("qwerty".getBytes()));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
