package com.maksim.diplom.controller;

import com.maksim.diplom.entity.Tags;
import com.maksim.diplom.entity.UsersTags;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.UsersTagsRepo;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Класс-контроллер для отображения страниц.
 */
@Controller
public class WebController {
    /**
     * Поле объявления переменной для логгирования
     */
    private static final Logger LOG = Logger.getLogger(WebController.class.getName());
    /**
     * Поля подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdvertRepo advertRepo;
    @Autowired
    private UsersTagsRepo usersTagsRepo;


    /**
     * Отображение первой страницы.
     *
     * @param model    to view page
     * @param response to add Cookie
     * @return view hello.html
     */
    @RequestMapping("/")
    public String helloView(Model model, HttpServletResponse response) {
        return "redirect:/viewAdverts";
    }

    /**
     * Отображение домашней страницы.
     * Так же осущствляет проверку на то, авторизировался ли пользователь:
     * - если да:  отображает домашнюю страницу [home.html];
     * - если нет: возвращает на страницу авторизации [redirect:/signup].
     *
     * @param model   to view page
     * @param request to get Cookies
     * @return view home.html or redirect:/signup
     */
    @RequestMapping("/home")
    public String homePageView(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        User userRepoById = userRepo.findById(Long.parseLong(cookies[0].getValue()));
        userRepoById.setPassword(null);
        model.addAttribute("user", userRepoById);
        return "redirect:/viewAuthorisedAdverts";
    }




    @RequestMapping("/addAdvertFromHello")
    public String addAdvertFromHelloPageView(Model model, HttpServletRequest request) {return "redirect:/authorization";}

    @RequestMapping("/addAdvertFromHome")
    public String addAdvertFromHomePageView(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        return "addAdvert.html";
    }


    /**
     * Отображение страницы со списком комнат.
     * @return view viewAdvert.html
     */
    @GetMapping("/viewAdverts")
    public String playroom() {
        return "viewAdverts.html";
    }

    /**
     * Отображение страницы со списком комнат.
     * @return view viewAdvert.html
     */
    @GetMapping("/viewAuthorisedAdverts")
    public String viewAuAdverts() {
        return "viewAuthorisedAdverts.html";
    }

    /**
     * Отображение игровой комнаты.
     * Так же осущствляет проверку на то, авторизировался ли пользователь, а также есть ли он в этой комнате:
     * - если да:  отображает страницу игровой комнаты [letsPlay.html];
     * - если нет: возвращает на страницу авторизации (пользователь не авторизован) [redirect:/signup]
     * или на страницу со списком комнат [redirect:/playrooms].
     *
     * @param model      to view page
     * @param advertId the room number
     * @param request    to get Cookies
     * @return view letsPlay.html redirect:/signup or redirect:/playrooms
     */
    @RequestMapping("/viewAdverts/{advertId}")
    public String singleAdvert(Model model, @PathVariable(value = "advertId") Long advertId, HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("userId")) {
//                cookies[0] = cookie;
//                break;
//            }
//        }
//        if (request.getCookies() == null)
//            return "redirect:/signup";
//        else if (!cookies[0].getName().equals("userId"))
//            return "redirect:/signup";
        if (advertRepo.findById(advertId).isPresent()) {
            return "singleAdvert.html";
        } else {
            LOG.error("Advert does not exist");
            return "redirect:/viewAdverts";
        }
    }

    /**
     * Отображение игровой комнаты.
     * Так же осущствляет проверку на то, авторизировался ли пользователь, а также есть ли он в этой комнате:
     * - если да:  отображает страницу игровой комнаты [letsPlay.html];
     * - если нет: возвращает на страницу авторизации (пользователь не авторизован) [redirect:/signup]
     * или на страницу со списком комнат [redirect:/playrooms].
     *
     * @param model      to view page
     * @param advertId the room number
     * @param request    to get Cookies
     * @return view letsPlay.html redirect:/signup or redirect:/playrooms
     */
    @RequestMapping("/viewAuthorisedAdverts/{advertId}")
    public String singleAuthorisedAdvert(Model model, @PathVariable(value = "advertId") Long advertId, HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("userId")) {
//                cookies[0] = cookie;
//                break;
//            }
//        }
//        if (request.getCookies() == null)
//            return "redirect:/signup";
//        else if (!cookies[0].getName().equals("userId"))
//            return "redirect:/signup";
        if (advertRepo.findById(advertId).isPresent()) {
            return "singleAuthorisedAdvert.html";
        } else {
            LOG.error("Advert does not exist");
            return "redirect:/viewAuthorisedAdverts";
        }
    }

    @RequestMapping("/profile")
    public String viewProfilePage(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        return "profile.html";
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
    @PostMapping("/changeUserData")
    public String changeUserData(User user, @RequestParam("tags[]") String[] tags, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userNow = userRepo.findById(Long.parseLong(cookies[0].getValue()));
        System.out.println(user);
        User userFromDb = userRepo.findByLogin(user.getLogin());
        if (Objects.equals(userFromDb.getId(), userNow.getId())) userFromDb = null;
        if (userFromDb != null) {
//            LOG.error("Login \"" + user.getName() + "\" already exist.");
            return "redirect:/profile";
        }
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (Objects.equals(userFromDb.getId(), userNow.getId())) userFromDb = null;
        if (userFromDb != null) {
//            LOG.error("Email \"" + user.getEmail() + "\" already exist.");
            return "redirect:/profile";
        }
        userFromDb = userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (Objects.equals(userFromDb.getId(), userNow.getId())) userFromDb = null;
        if (userFromDb != null) {
//            LOG.error("Phone number \"" + user.getPhoneNumber() + "\" already exist.");
            return "redirect:/profile";
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(user.getEmail())){
//            LOG.error("Wrong email address.");
            return "redirect:/profile";
        }
//        LOG.info("User " + user.getLogin() + " is registered.");
        userRepo.updateDate(user.getName(), user.getLogin(), user.getEmail(), user.getPhoneNumber(), userNow.getId());
        usersTagsRepo.deleteAllByUsersId(userNow.getId());
        if (!tags[0].isEmpty()) {
            for (String s : tags) {
                UsersTags tagsEnt = new UsersTags();
                tagsEnt.setUsersId(userNow.getId());
                tagsEnt.setTagName(s);
                usersTagsRepo.save(tagsEnt);
            }
        }
        return "redirect:/profile";
    }

}