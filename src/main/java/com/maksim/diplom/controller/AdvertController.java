package com.maksim.diplom.controller;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.Picture;
import com.maksim.diplom.entity.Tags;
import com.maksim.diplom.repos.AdvertRepo;
import com.maksim.diplom.repos.PicturesRepo;
import com.maksim.diplom.repos.TagsRepo;
import com.mortennobel.imagescaling.ResampleOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Класс-контроллер для объявлений.
 */
@Controller
public class AdvertController {
    /**
     * Поля подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private AdvertRepo advertRepo;
    @Autowired
    private PicturesRepo picturesRepo;
    @Autowired
    private TagsRepo tagsRepo;


    /**
     * @param advert it receives data from forms
     * @param model  to view page
     * @return view signup.html or redirect:/authorization
     */
    @PostMapping("/saveAdvert")
    public String saveAdvert(Advert advert, Model model, @RequestParam("picture") MultipartFile[] file, @RequestParam("tags[]") String[] tags,  HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
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

    /**
     * @param adId          ID of the ad that owns the image
     * @param multipartFile multipart file received from form
     */
    public void writeFile(Long adId, MultipartFile multipartFile, Integer count) throws IOException {
        String type = multipartFile.getContentType().substring(6);
        String name = adId + "-" + count + "." + type;
        Path filepath = Paths.get("./src/main/resources/tempPicture", name);
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(multipartFile.getBytes());
        }

        File file = new File("./src/main/resources/tempPicture/" + name);
        BufferedImage originalBufferedImage = ImageIO.read(file);
        ResampleOp resamOp = new ResampleOp(100, 100);
        BufferedImage modifiedImage = resamOp.filter(originalBufferedImage, null);
        ImageIO.write(modifiedImage, type, file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(modifiedImage, multipartFile.getContentType().substring(6), baos);


        Picture pic = new Picture();
        pic.setAdvertId(adId);
        pic.setPicture(Base64.getEncoder().encodeToString(baos.toByteArray()));
        picturesRepo.save(pic);

        delAllFile("./src/main/resources/tempPicture/");


        //разобраться с очисткой папки


        //Декодирование с сохранением в папку(для другого метода)
//        byte[] decoded = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(file.getBytes()));
//        Path filepath = Paths.get("/home/maksim/Documents/Diplom/diplom/src/main/resources/tempPicture", file.getOriginalFilename());
//        try (OutputStream os = Files.newOutputStream(filepath)) {
//            os.write(decoded);
//        }
    }

    /**
     * @param path is path to dir
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]); // Сначала удалите файлы в папке
            }
        }

    }
}
