package com.example.renyanyu.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.renyanyu.server.entity.History;

public interface HistoryDao extends JpaRepository<History, Long> {
}
