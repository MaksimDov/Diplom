package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagsRepo extends JpaRepository <Tags, Long> {
    /**
     * Поиск всех тэгов объявления
     * @param advertId id объявдения
     * @return List<Tags>
     */
    List<Tags> findAllByAdvertId(Long advertId);

    /**
     * Удаление всех тэгов объявления
     * @param advertId id объявления
     */
    @Modifying
    @Transactional
    void deleteAllByAdvertId(@Param("advert_id") Long advertId);

}
