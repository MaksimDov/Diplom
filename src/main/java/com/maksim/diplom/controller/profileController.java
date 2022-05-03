package com.maksim.diplom.controller;

import com.maksim.diplom.entity.User;
import com.maksim.diplom.entity.UsersTags;
import com.maksim.diplom.repos.UserRepo;
import com.maksim.diplom.repos.UsersTagsRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Класс-контроллер для профиля пользователя.
 */
@Controller
@RestController
public class profileController {

    /**
     * Поля подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UsersTagsRepo usersTagsRepo;

    /**
     * Метод формирования данных пользователя в JSON для отображения пользователя
     * @param request получает данные сессии
     * @return JSONObject с данными пользователя
     */
    @GetMapping("/profile/update")
    public String profileDataToJson(HttpServletRequest request){
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


}
