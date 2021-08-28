package com.example.renyanyu.server.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.renyanyu.server.entity.Exercise;
import com.example.renyanyu.server.entity.User;

public interface ExerciseDao extends JpaRepository<Exercise, Long> {
	Page<Exercise> findByUser(User user, Pageable pageable);
	Page<Exercise> findByUserAndIsWrong(User user, boolean isWrong, Pageable pageable);
	
}
