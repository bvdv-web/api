package com.bvdv.bvdvapi.repository;

import com.bvdv.bvdvapi.models.PtResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtResultRepository extends JpaRepository<PtResult, Long> {
    PtResult findBySessionIdAndType(String sessionId, PtResult.Type type);

}
