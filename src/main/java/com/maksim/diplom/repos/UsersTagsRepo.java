package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Tags;
import com.maksim.diplom.entity.UsersTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsersTagsRepo extends JpaRepository<UsersTags, Long> {
    List<UsersTags> findAllByUsersId(Long usersId);

}
