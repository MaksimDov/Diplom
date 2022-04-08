package com.maksim.diplom.controller;

import com.maksim.diplom.entity.*;
import com.maksim.diplom.repos.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
@RequestMapping("/viewAuthorisedAdverts")
public class ViewAuthorisedController {
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
        String adCost;

        JSONArray resultJson = new JSONArray();
        for (Advert advert : adverts) {
            JSONObject tempJson = new JSONObject();
            Picture picture = picturesRepo.findTopByAdvertId(advert.getId());
            picPath = picture.getPicture();
            User userNow = userRepo.findById(advert.getUserId()).orElseThrow(() -> new NoEntityException(advert.getUserId()));
            userName = userNow.getName();
            adName = advert.getName();
            adDescription = advert.getDescription();
            adCost = advert.getCost();
            List<Tags> tagsList = tagsRepo.findAllByAdvertId(advert.getId());
            JSONArray jsonArray = new JSONArray();
            for (int t = 0; t < tagsList.size(); ++t){
                jsonArray.add(tagsList.get(t).getName());
            }
            tempJson.put("adId", advert.getId());
            tempJson.put("userName", userName);
            tempJson.put("adName", adName);
            tempJson.put("adDescription", adDescription);
            tempJson.put("adCost", adCost);
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
        return "/viewAuthorisedAdverts/" + advertId;
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
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByLoginOrId(null, Long.parseLong(cookies[0].getValue()));
        Advert advert = advertRepo.findById(advertId).orElseThrow(() -> new NoEntityException(advertId));
        String owner = "false";
        String userName = "";
        String adName = "";
        String adDescription;
        User userNow = userRepo.findById(advert.getUserId()).orElseThrow(() -> new NoEntityException(advert.getUserId()));
        if (userRepoById.getId() == userNow.getId()){
            owner = "true";
        }
        if (userRepoById.getRole() != null){
            owner = "true";
        }
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
        resultJson.put("owner", owner);
        resultJson.put("userEmail", userNow.getEmail());
        resultJson.put("userPhoneNumber", userNow.getPhoneNumber());
        resultJson.put("adCost", advert.getCost());
        resultJson.put("tags", jsonArray);
        resultJson.put("pictures", picArray);
//        System.out.println(resultJson.toString());
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
//        System.out.println(resultJson.toString());
        return resultJson.toString();
    }

    /**
     * Метод удаления всех объектов комнат по номеру из БД, где пользователь является хостом
     * (в противном случае не работает [/playrooms/ + roomNumber]).
     * Удалить комнату возможно только в момент регистрации.
     * После начала игры эта возможность пропадает до следующего времени регистрации после окончания текущей игры.
     *
     * @param advertId the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms or /playrooms/ + roomNumber
     */
    @GetMapping("/{advertId}/delete")
    public String deleteAdvert(@PathVariable("advertId") Long advertId, HttpServletRequest request, Model model) {
        List<Picture> picList = picturesRepo.findAllByAdvertId(advertId);
        for(Picture pic : picList){
            File file = new File("./src/main/resources/static" + pic.getPicture());
            file.delete();
        }
        tagsRepo.deleteAllByAdvertId(advertId);
        picturesRepo.deleteAllByAdvertId(advertId);
        advertRepo.deleteAllById(advertId);
        return "/viewAuthorisedAdverts";

    }

    /**
     * Обновляет список доступных (находящихся на стадии регистрации) комнат.
     * Передает сформированный лист объектов через json пользователю.
     *
     * @param request to get Cookies [to find user by id]
     * @param model   the model
     * @return json (list rooms)
     */
    @GetMapping("/viewMyAd")
    public String viewMyAd(HttpServletRequest request, Model model) throws NoEntityException {
        if (advertRepo.findAll() == null)
            return "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        List<Advert> adverts = advertRepo.findAllByUserId(Long.parseLong(cookies[0].getValue())).stream().sorted(Comparator.comparing(Advert::getId)).collect(Collectors.toList());
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
//        System.out.println(resultJson);
        return resultJson.toString();
    }

    /**
     * Обновляет список доступных (находящихся на стадии регистрации) комнат.
     * Передает сформированный лист объектов через json пользователю.
     *
     * @param request to get Cookies [to find user by id]
     * @param model   the model
     * @return json (list rooms)
     */
    @GetMapping("/viewRecomend")
    public String viewRecomend(HttpServletRequest request, Model model) throws NoEntityException {
        if (advertRepo.findAll() == null)
            return "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        List<UsersTags> usersTags = usersTagsRepo.findAllByUsersId(Long.parseLong(cookies[0].getValue()));
        List<Advert> adverts = advertRepo.findAll();
        String userName;
        String adName;
        String adDescription;
        String picPath;
        JSONArray resultJson = new JSONArray();
        for (Advert advert : adverts) {
            boolean isAd = false;
            List<Tags> tagsList = tagsRepo.findAllByAdvertId(advert.getId());
            for (UsersTags ut : usersTags){
                for (Tags at : tagsList){
                    if (Objects.equals(ut.getTagName(), at.getName())){
                        isAd = true;
                        JSONObject tempJson = new JSONObject();
                        Picture picture = picturesRepo.findTopByAdvertId(advert.getId());
                        picPath = picture.getPicture();
                        User userNow = userRepo.findById(advert.getUserId()).orElseThrow(() -> new NoEntityException(advert.getUserId()));
                        userName = userNow.getName();
                        adName = advert.getName();
                        adDescription = advert.getDescription();
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
                    if(isAd) break;
                }
                if (isAd) break;
            }
        }
        System.out.println(resultJson);
//        System.out.println(resultJson);
        return resultJson.toString();
    }
}
