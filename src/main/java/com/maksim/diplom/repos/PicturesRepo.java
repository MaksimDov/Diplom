package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PicturesRepo extends JpaRepository<Picture, Long> {

}
