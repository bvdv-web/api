package com.bvdv.bvdvapi.repository;

import com.bvdv.bvdvapi.models.RResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RResultRepository extends JpaRepository<RResult, Long> {
    List<RResult> findBySessionId(String sessionId);
}
