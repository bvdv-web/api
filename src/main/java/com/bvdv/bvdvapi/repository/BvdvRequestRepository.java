package com.bvdv.bvdvapi.repository;

import com.bvdv.bvdvapi.models.BvdvRequest;
import com.bvdv.bvdvapi.models.PtResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BvdvRequestRepository extends JpaRepository<BvdvRequest, Long> {
    List<BvdvRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<BvdvRequest> findBySessionId(String sessionId);

}
