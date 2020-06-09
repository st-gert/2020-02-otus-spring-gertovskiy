package ru.otus.job12.security.training;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Фильтр для просмотра свойств пользователя.
 * Работает после окончания аутентификации, перед авторизацией.
 *
 * Добавлен в учебных целях.
 */
public class UserExtractorFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        StringBuilder sb = new StringBuilder("+++++++   Filter   +++ ");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
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
        } else {
            sb.append("NULL");
        }

        System.out.println(sb);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
