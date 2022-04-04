package com.maksim.diplom.controller;

import com.maksim.diplom.entity.*;
import com.maksim.diplom.repos.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/profile")
public class profileController {

    /**
     * Поля подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private AdvertRepo advertRepo;
    @Autowired
    private PicturesRepo picturesRepo;
    @Autowired
    private TagsRepo tagsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UsersTagsRepo usersTagsRepo;


    @GetMapping("/update")
    public String profileDataToJson(HttpServletRequest request, Model model) throws NoEntityException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User user = userRepo.findById(Long.parseLong(cookies[0].getValue()));
        String userName = user.getName();
        String userEmail = user.getEmail();
        String userPhoneNumber = user.getPhoneNumber();
        String userLogin = user.getLogin();
        List<UsersTags> list = usersTagsRepo.findAllByUsersId(user.getId());
        System.out.println(list);
        JSONArray jsonArray = new JSONArray();
        for (UsersTags tag : list){
            jsonArray.add(tag.getTagName());
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("userName", userName);
        resultJson.put("userEmail", userEmail);
        resultJson.put("userPhoneNumber", userPhoneNumber);
        resultJson.put("userLogin", userLogin);
        resultJson.put("tags", jsonArray);

        return resultJson.toString();
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
    public String changeUserData(User user, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userNow = userRepo.findById(Long.parseLong(cookies[0].getValue()));

        User userFromDb = userRepo.findByLogin(user.getLogin());
        if (userFromDb != null) {
//            LOG.error("Login \"" + user.getName() + "\" already exist.");
            return "profile.html";
        }
        userFromDb = userRepo.findByEmail(user.getEmail());
        if (userFromDb != null) {
//            LOG.error("Email \"" + user.getEmail() + "\" already exist.");
            return "profile.html";
        }
        userFromDb = userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (userFromDb != null) {
//            LOG.error("Phone number \"" + user.getPhoneNumber() + "\" already exist.");
            return "profile.html";
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(user.getEmail())){
//            LOG.error("Wrong email address.");
            return "profile.html";
        }
//        LOG.info("User " + user.getLogin() + " is registered.");
        userRepo.updateDate(user.getName(), user.getLogin(), user.getEmail(), user.getPhoneNumber(), userNow.getId());
        return "redirect:/profile";
    }
}
