package com.maksim.diplom.controller;

import com.maksim.diplom.entity.*;
import com.maksim.diplom.repos.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
//@RequestMapping("/profile")
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


    @GetMapping("/profile/update")
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


}
