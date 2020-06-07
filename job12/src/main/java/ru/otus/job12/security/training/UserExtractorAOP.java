package ru.otus.job12.security.training;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Просмотр свойств пользователя.
 * Работает перед методами контроллеров.
 *
 * Добавлен в учебных целях.
 */
@Aspect
@Component
@Order(0)
public class UserExtractorAOP {

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void extractUser() {
        StringBuilder sb = new StringBuilder("******* Controller *** ");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        sb.append(authentication.getName()).append(" ").append(authentication.getAuthorities()).append(" |  ");

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            sb.append("String: ").append(principal);
            sb.append(" ").append(authentication.getAuthorities());
        } else if (principal instanceof UserDetails) {
            sb.append(principal.getClass().getSimpleName());
            UserDetails ud = (UserDetails) principal;
            sb.append(": ").append(ud.getUsername())
                    .append(" ").append(ud.getAuthorities());
        } else {
            sb.append(principal.getClass().getName());
        }

        System.out.println(sb);
    }

}
