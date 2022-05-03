package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PicturesRepo extends JpaRepository<Picture, Long> {
    /**
     * Поиск первого изображения принадлежащего определенному объявлению
     * @param advertId id объявления
     * @return Picture
     */
    Picture findTopByAdvertId(Long advertId);

    /**
     * Поиск всех изображений объявления
     * @param advertId id объявления
     * @return List<Picture>
     */
    List<Picture> findAllByAdvertId(Long advertId);

    /**
     * Удаление всех изображений объявления
     * @param advertId id объявления
     */
    @Modifying
    @Transactional
    void deleteAllByAdvertId(@Param("advert_id") Long advertId);
}
