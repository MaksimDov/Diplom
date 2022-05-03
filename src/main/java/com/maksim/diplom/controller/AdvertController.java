package com.maksim.diplom.controller;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.Picture;
import com.maksim.diplom.entity.Tags;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.PicturesRepo;
import com.maksim.diplom.repos.TagsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс-контроллер для объявлений.
 */
@Controller
public class AdvertController {
    /**
     * Поля подключения репозиториев для взамимодействия пользвателя с БД.
     */
    @Autowired
    private AdvertRepo advertRepo;
    @Autowired
    private PicturesRepo picturesRepo;
    @Autowired
    private TagsRepo tagsRepo;

    /**
     * Метод сохранения объявлений в базу данных.
     * @param advert получает данные из формы
     * @return отобразить signup.html or redirect:/authorization
     */
    @PostMapping("/saveAdvert")
    public String saveAdvert(Advert advert, @RequestParam("picture") MultipartFile[] file, @RequestParam("tags[]") String[] tags,  HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        advert.setUserId(Long.parseLong(cookies[0].getValue()));
        advertRepo.save(advert);
        Long advertId = advertRepo.getMaxAdvertId();
        if (!file[0].isEmpty()) {
            Integer count = 0;
            for (MultipartFile m : file) {
                writeFile(advertId, m, count);
                ++count;
            }
        }
        if (!tags[0].isEmpty()) {
            for (String s : tags) {
                Tags tagsEnt = new Tags();
                tagsEnt.setAdvertId(advertId);
                tagsEnt.setName(s);
                tagsRepo.save(tagsEnt);
            }
        }
        return "redirect:/home";
    }

    /**Метод сохранения изображений в базу данных и в хранилище.
     * @param adId          идентификатор объявления, к которому относится изображение
     * @param multipartFile файл, получаемый из формы
     */
    public void writeFile(Long adId, MultipartFile multipartFile, Integer count) throws IOException {
        String type = multipartFile.getContentType().substring(6);
        String name = adId + "-" + count + "." + type;
        Path filepath = Paths.get("./src/main/resources/static/tempPicture", name);
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(multipartFile.getBytes());
        }

        Picture pic = new Picture();
        pic.setAdvertId(adId);
        pic.setPicture(String.valueOf(filepath).substring(27));
        picturesRepo.save(pic);
    }
}
