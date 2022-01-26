package com.maksim.diplom.controller;

import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.UserRepo;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Класс-контроллер для регистрации.
 */
@Controller
public class RegistrationController {
    /**
     * Поле объявления переменной для логгирования
     */
    private static final Logger LOG = Logger.getLogger(RegistrationController.class.getName());
    /**
     * Поле подключения PasswordEncoder, для храниения и сравнения паролей в неявном виде (кодировка BCrypt).
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Поле подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * Авторизация пользователя в нашей системе. Выполняет поиск пользователя в БД, проверяет, совпадают ли логин и пароль:
     * - если да:  добавляет cookie с именем пользователя и его идентификатором переходит на домашнюю страницу [/home];
     * - если нет: возвращает на страницу авторизации [/authorization.html].
     *
     * @param user     it receives data from forms
     * @param model    to view page
     * @param response to add Cookie
     * @return /authorization.html or redirect:/home
     */
    @PostMapping("/")
    public String signIn(User user, Model model, HttpServletResponse response) {
        User userRepoByLoginOrEmail = userRepo.findByLoginOrEmail(user.getLogin(), user.getEmail());
        if (userRepoByLoginOrEmail == null || !passwordEncoder.matches(user.getPassword(), userRepoByLoginOrEmail.getPassword())) {
            LOG.error("Wrong username or password.");
            return "/authorization.html";
        }
        LOG.info("User " + user.getLogin() + " authorized.");
        Cookie cookie = new Cookie("userId", userRepoByLoginOrEmail.getId().toString());
        response.addCookie(cookie);
        return "redirect:/home";
    }

    /**
     * Отображение страницы регистрации.
     *
     * @param model to view page
     * @return view signup.html
     */
    @GetMapping("/signup")
    public String signUp(Model model) {
        return "signup.html";
    }

    /**
     * Регистрация пользователя в нашей системе. Выполняет поиск пользователя по логину в БД, проверяет, есть ли уже такое:
     * - если нет:  добавляет пользователя в БД и переходит на страницу авторизации [/authorization];
     * - если да: возвращает на страницу регистрации [signup.html].
     *
     * @param user  it receives data from forms
     * @param model to view page
     * @return view signup.html or redirect:/authorization
     */
    @PostMapping("/signup")
    public String signUpNewUser(User user, Model model) {
        User userFromDb = userRepo.findByLogin(user.getLogin());
        if (userFromDb != null) {
            LOG.error("Login \"" + user.getName() + "\" already exist.");
            return "signup.html";
        }
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (userFromDb != null) {
            LOG.error("Email \"" + user.getName() + "\" already exist.");
            return "signup.html";
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(user.getEmail())){
            LOG.error("Wrong email address.");
            return "signup.html";
        }
        LOG.info("User " + user.getLogin() + " is registered.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/authorization";
    }


//    @PostMapping("/signOut")
//    public String signOut(HttpServletRequest request, HttpServletResponse response){
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null)
//            for (Cookie cookie : cookies) {
//                cookie.setValue("");
//                cookie.setPath("/");
//                cookie.setMaxAge(0);
//                response.addCookie(cookie);
//            }
//        return "hello.html";
//    }
}
