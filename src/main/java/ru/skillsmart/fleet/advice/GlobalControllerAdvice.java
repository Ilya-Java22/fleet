package ru.skillsmart.fleet.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

//управление глобальными аспектами веб-приложения
//конкретно тут получаем список ролей авторизованного пользователя,
// чтобы потом в хедере понять, показывать ему ссылку Отчеты или нет
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUserRoles")
    public List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> userRoles = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            userRoles.add(authority.getAuthority());
        }
        return userRoles;
    }
}