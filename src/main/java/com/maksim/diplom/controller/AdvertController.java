package com.maksim.diplom.controller;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.User;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.UserRepo;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Base64;

/**
 * Класс-контроллер для объявлений.
 */
@Controller
public class AdvertController {
    /**
     * Поле подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private AdvertRepo advertRepo;

    /**
     * @param advert  it receives data from forms
     * @param model to view page
     * @return view signup.html or redirect:/authorization
     */
    @PostMapping("/saveAdvert")
    public String saveAdvert(Advert advert, Model model, @RequestParam("picture") MultipartFile[] file, HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        advert.setUserId(Long.parseLong(cookies[0].getValue()));
        System.out.println(advert);
        advertRepo.save(advert);
        if(!file[0].isEmpty()) {
            for (MultipartFile m : file) {
                write(m, Path.of("src/main/resources/tempPicture"));
            }
        }
        return "redirect:/home";
        //выбор объявления по макс id для добавления id в таблицу изображений.
    }

    public void write(MultipartFile file, Path dir) throws IOException {
        Path filepath = Paths.get(dir.toString(), file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        }
        String encoded = Base64.getEncoder().encodeToString(file.getBytes());
        System.out.println(encoded);
    }
}
