package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PicturesRepo extends JpaRepository<Picture, Long> {
    Picture findTopByAdvertId(Long advertId);

    List<Picture> findAllByAdvertId(Long advertId);
}
