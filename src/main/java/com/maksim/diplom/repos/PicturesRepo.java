package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PicturesRepo extends JpaRepository<Picture, Long> {
    Picture findTopByAdvertId(Long advertId);

    List<Picture> findAllByAdvertId(Long advertId);

    @Modifying
    @Transactional
    void deleteAllByAdvertId(@Param("advert_id") Long advertId);
}
