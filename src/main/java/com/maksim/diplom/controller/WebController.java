package com.maksim.diplom.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * Поле подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;


    /**
     * Отображение первой страницы.
     *
     * @param model    to view page
     * @param response to add Cookie
     * @return view hello.html
     */
    @RequestMapping("/")
    public String helloView(Model model, HttpServletResponse response) {
        return "hello.html";
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
        return "home.html";
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

}