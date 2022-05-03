package com.maksim.diplom.controller;

import com.maksim.diplom.entity.User;
import com.maksim.diplom.entity.UsersTags;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.UserRepo;
import com.maksim.diplom.repos.UsersTagsRepo;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * @return view hello.html
     */
    @RequestMapping("/")
    public String helloView() {
        return "redirect:/viewAdverts";
    }

    /**
     * Отображение главной страницы.
     * Так же осущствляет проверку на то, авторизировался ли пользователь:
     * @param model   для отображения
     * @param request получение Cookies
     * @return отобразить home.html или redirect:/signup
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




    /**
     * Переход на страницу авторизации, если неавторизованный пользователь хочет добавить объявление
     * @return redirect:/authorization
     */
    @RequestMapping("/addAdvertFromHello")
    public String addAdvertFromHelloPageView() {return "redirect:/authorization";}

    /**
     * Отображение страницы добавления объявлений
     * @param request получение id пользователя
     * @return addAdvert.html или redirect:/signup
     */
    @RequestMapping("/addAdvertFromHome")
    public String addAdvertFromHomePageView(HttpServletRequest request) {
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
     * Отображение объявления.
     * @param advertId id объявления
     * @return отображает singleAdvert.html или redirect:/viewAdverts
     */
    @RequestMapping("/viewAdverts/{advertId}")
    public String singleAdvert(@PathVariable(value = "advertId") Long advertId) {
        if (advertRepo.findById(advertId).isPresent()) {
            return "singleAdvert.html";
        } else {
            LOG.error("Advert does not exist");
            return "redirect:/viewAdverts";
        }
    }

    /**
     * Отображение объявления.
     * @param advertId id объявления
     * @return отображает singleAdvert.html или redirect:/viewAdverts
     */
    @RequestMapping("/viewAuthorisedAdverts/{advertId}")
    public String singleAuthorisedAdvert(@PathVariable(value = "advertId") Long advertId) {
        if (advertRepo.findById(advertId).isPresent()) {
            return "singleAuthorisedAdvert.html";
        } else {
            LOG.error("Advert does not exist");
            return "redirect:/viewAuthorisedAdverts";
        }
    }

    /**
     * Отображение профиля.
     * @param request получение id пользователя
     * @return отображает profile.html или redirect:/signup
     */
    @RequestMapping("/profile")
    public String viewProfilePage(HttpServletRequest request) {
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
     * Изменение данных профиля пользователя
     *
     * @param user  получает данные из форм
     * @param tags список интересных пользователю категорий
     * @return redirect:/profile
     */
    @PostMapping("/changeUserData")
    public String changeUserData(User user, @RequestParam("tags[]") String[] tags, HttpServletRequest request) {
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
            return "redirect:/profile";
        }
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (Objects.equals(userFromDb.getId(), userNow.getId())) userFromDb = null;
        if (userFromDb != null) {
            return "redirect:/profile";
        }
        userFromDb = userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (Objects.equals(userFromDb.getId(), userNow.getId())) userFromDb = null;
        if (userFromDb != null) {
            return "redirect:/profile";
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(user.getEmail())){
            return "redirect:/profile";
        }
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