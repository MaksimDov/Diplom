package com.maksim.diplom.controller;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.Picture;
import com.maksim.diplom.entity.Tags;
import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.PicturesRepo;
import com.maksim.diplom.repos.TagsRepo;
import com.maksim.diplom.repos.UserRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;



/**
 * Класс - REST контроллер, основной класс, который отвечает за ход игры, создание, удаление комнат,
 * присоединения к ним пользователей, их взаимодействие в чате и отображение списка доступных комнат.
 * Обращение к методам происходит через ajax-запросы.
 *
 * @version v1.0
 */
@RestController
@RequestMapping("/viewAdverts")
public class ViewController {
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

    /**
     * Обновляет список доступных (находящихся на стадии регистрации) комнат.
     * Передает сформированный лист объектов через json пользователю.
     *
     * @param request to get Cookies [to find user by id]
     * @param model   the model
     * @return json (list rooms)
     */
    @GetMapping("/update")
    public String viewAllRooms(HttpServletRequest request, Model model) throws NoEntityException {
        if (advertRepo.findAll() == null)
            return "";
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies)
//            if (cookie.getName().equals("userId")) {
//                cookies[0] = cookie;
//                break;
//            }
        List<Advert> adverts = advertRepo.findAll().stream().sorted(Comparator.comparing(Advert::getId)).collect(Collectors.toList());
        String userName;
        String adName;
        String adDescription;
        String picPath;

        JSONArray resultJson = new JSONArray();
        for (Advert advert : adverts) {
            JSONObject tempJson = new JSONObject();
            Picture picture = picturesRepo.findTopByAdvertId(advert.getId());
            picPath = picture.getPicture();
            User userNow = userRepo.findById(advert.getUserId()).orElseThrow(() -> new NoEntityException(advert.getUserId()));
            userName = userNow.getName();
            adName = advert.getName();
            adDescription = advert.getDescription();
            List<Tags> tagsList = tagsRepo.findAllByAdvertId(advert.getId());
            JSONArray jsonArray = new JSONArray();
            for (int t = 0; t < tagsList.size(); ++t){
                jsonArray.add(tagsList.get(t).getName());
            }
            tempJson.put("adId", advert.getId());
            tempJson.put("userName", userName);
            tempJson.put("adName", adName);
            tempJson.put("adDescription", adDescription);
            tempJson.put("picPath", picPath);
            tempJson.put("tags", jsonArray);
            resultJson.add(tempJson);
        }
        return resultJson.toString();
    }

    /**
     * Отображение объявления.
     *
     * @param advertId the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms/ + roomNumber
     */
    @GetMapping("/{advertId}/watchAdvert")
    public String watchAd(@PathVariable("advertId") Long advertId, HttpServletRequest request, Model model) {
        if (advertRepo.findById(advertId) == null)
            return "This advert has been deleted.";
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies)
//            if (cookie.getName().equals("userId")) {
//                cookies[0] = cookie;
//                break;
//            }
        return "/viewAdverts/" + advertId;
    }

    /**
     * Метод формирования json содержимого комнат.
     *
     * @param advertId the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms/ + roomNumber
     */
    @GetMapping("/{advertId}/updateSingleAdvert")
    public String viewSingleAd(@PathVariable("advertId") Long advertId, HttpServletRequest request, Model model) throws NoEntityException {
        Advert advert = advertRepo.findById(advertId).orElseThrow(() -> new NoEntityException(advertId));
        String userName = "";
        String adName = "";
        String adDescription;
        User userNow = userRepo.findById(advert.getUserId()).orElseThrow(() -> new NoEntityException(advert.getUserId()));
        userName = userNow.getName();
        adName = advert.getName();
        adDescription = advert.getDescription();
        List<Tags> tagsList = tagsRepo.findAllByAdvertId(advert.getId());
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int t = 0; t < tagsList.size(); ++t){
            jsonArray.add(tagsList.get(t).getName());
        }
        List<Picture> picList = picturesRepo.findAllByAdvertId(advert.getId());
        JSONArray picArray = new JSONArray();
        for (int t = 0; t < picList.size(); ++t){
            picArray.add(picList.get(t).getPicture());
        }
        resultJson.put("adId", advertId);
        resultJson.put("userName", userName);
        resultJson.put("adName", adName);
        resultJson.put("adDescription", adDescription);
        resultJson.put("tags", jsonArray);
        resultJson.put("pictures", picArray);
        System.out.println(resultJson.toString());
        return resultJson.toString();
    }

    @GetMapping("/{advertId}/changePic")
    public String changePic(@PathVariable("advertId") Long advertId, HttpServletRequest request, Model model) throws NoEntityException {
        Advert advert = advertRepo.findById(advertId).orElseThrow(() -> new NoEntityException(advertId));
        JSONObject resultJson = new JSONObject();

        List<Picture> picList = picturesRepo.findAllByAdvertId(advert.getId());
        JSONArray picArray = new JSONArray();
        for (int t = 0; t < picList.size(); ++t){
            picArray.add(picList.get(t).getPicture());
        }
        resultJson.put("adId", advertId);
        resultJson.put("pictures", picArray);
        System.out.println(resultJson.toString());
        return resultJson.toString();
    }
}
