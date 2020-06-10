package ru.otus.job13.security;

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

import java.security.SecureRandom;

/**
 * Настройки безопасности.
 *
 *  Права
 *    Роли           Чтение      Чтение       Добавление,      Работа
 *                   списков     списков      изменение,       с отзывами
 *                   книг        жанров,      удаление книг    (CRUD) ***
 *                               авторов      жанров, авт.
 *   ANONYMOUS         +           -              -               -
 *   USER              +           +              -               +
 *   ADMIN             +           +              +               -
 *
 *   *** - Каждый пользователь, имеющий права на отзывы, корректирует и удаляет только свои отзывы (ACL).
 *
 *  Пользователи     Роли
 *    owner         ADMIN, USER
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
                .antMatchers("/h2-console/**")  // для визуальной проверки добавления, удаления прав ACL
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                // Читают списки книг все, включая ANONYMOUS
                .and()
                .authorizeRequests().antMatchers("/book/list/**").permitAll()
                // Читают списки жанров и авторов ADMIN и USER
                .and()
                .authorizeRequests().antMatchers("/author/list", "/genre/list").hasAnyRole("ADMIN", "USER")
                // Все изменения, кроме отзывов - ADMIN
                .and()
                .authorizeRequests().antMatchers("/book/**", "/author/**", "/genre/**").hasRole("ADMIN")
                // Работа с отзывами - USER
                .and()
                .authorizeRequests().antMatchers("/review/**").hasAnyRole("USER")

                .and()
                .formLogin()

                .and()
                .logout()
                .logoutUrl("/logout")

                .and()
                .rememberMe()
                .key("Q5y7b9*/")
                .tokenValiditySeconds(365 * 24 * 60 * 60)

                .and()
                .anonymous().authorities("ROLE_ANONYMOUS").principal(new AnonymousUserDetails())

                .and()
                .exceptionHandling().accessDeniedPage("/access-denied.html")
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Очень сложный расчет, работает долго
        return new  BCryptPasswordEncoder(15, new SecureRandom("qwerty".getBytes()));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
