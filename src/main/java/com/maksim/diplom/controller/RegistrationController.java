package com.maksim.diplom.controller;

import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.UserRepo;
import com.maksim.diplom.service.EmailService;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
     * Метод авторизации пользователя в системе.
     * Авторизация пользователя системе выполняет поиск пользователя в БД, проверяет, совпадают ли логин и пароль:
     * - если да:  добавляет cookie с именем пользователя и его идентификатором переходит на домашнюю страницу [/home];
     * - если нет: возвращает на страницу авторизации [/authorization.html].
     *
     * @param user     получает данные из формы
     * @param response добавляет cookies
     * @return /authorization.html или redirect:/home
     */
    @PostMapping("/")
    public String signIn(User user, HttpServletResponse response) {
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
     * @return отображает signup.html
     */
    @GetMapping("/signup")
    public String signUp() {
        return "signup.html";
    }

    /**
     * Метод регистрации пользователя.
     * Регистрация пользователя системе выполняет поиск пользователя по логину в БД, проверяет, есть ли уже такое:
     * - если нет:  добавляет пользователя в БД и переходит на страницу подтверждения почты [/emailConfirm];
     * - если да: возвращает на страницу регистрации [signup.html].
     *
     * @param user  получает данные из формы
     * @return отображает signup.html или redirect:/authorization
     */
    @PostMapping("/signup")
    public String signUpNewUser(User user) {
        User userFromDb = userRepo.findByLogin(user.getLogin());
        if (userFromDb != null) {
            LOG.error("Login \"" + user.getName() + "\" already exist.");
            return "signup.html";
        }
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (userFromDb != null) {
            LOG.error("Email \"" + user.getEmail() + "\" already exist.");
            return "signup.html";
        }
        userFromDb = userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (userFromDb != null) {
            LOG.error("Phone number \"" + user.getPhoneNumber() + "\" already exist.");
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
        return "redirect:/emailConfirm";
    }

    /**
     * Поле подключения сервиса почты
     */
    @Autowired
    private EmailService emailService;

    /**
     * Метод генерации кода подтверждения и отправки его на почту.
     * Генерирует четырехзначный код, сохраняет его в базу данных в закодированном виде.
     * Отправляет код на почту с помощью сервиса почты(emailService).
     * @return отображает emailConfirm.html
     */
    @GetMapping("/emailConfirm")
    public String sendEmail(){
        String subject = "Подтверждение почты";
        int minCode = 1000;
        int maxCode = 9999;
        int code = minCode + (int)(Math.random() * ((maxCode - minCode) + 1));
        Long lastUserId = userRepo.getMaxId();
        User user = userRepo.findById(lastUserId).orElse(new User());
        userRepo.updateConfirm(passwordEncoder.encode(Integer.toString(code)), user.getId());
        emailService.sendSimpleMessage(subject, "Код подтверждения: " + code, user.getEmail());
        return "emailConfirm.html";
    }

    /**
     * Метод проверки кода подтверждения.
     * Сравнивает полученный код с отправленным.
     *      -Если совпадают отмечает в базе данных, что почта подтверждена и переходит на страницу авторизации[/authorization]
     *      -Если не совпадают возвращает на страницу регистрации[/signup]
     *
     * @param codeFromForm  получает данные из формы
     * @return отображает signup.html или redirect:/authorization
     */
    @PostMapping("/emailConfirm")
    public String emailConfirm(@RequestParam("code") String codeFromForm) {
        Long lastUserId = userRepo.getMaxId();
        User user = userRepo.findById(lastUserId).orElse(new User());
        if(!passwordEncoder.matches(codeFromForm, user.getConfirm())){
            userRepo.deleteAllById(user.getId());
            return "/signup.html";
        }
        else {
            userRepo.updateConfirm("true", user.getId());
            return "redirect:/authorization";
        }
    }
}
