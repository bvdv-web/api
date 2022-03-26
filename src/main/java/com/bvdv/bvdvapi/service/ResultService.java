package com.bvdv.bvdvapi.service;

import com.bvdv.bvdvapi.models.PtResult;
import com.bvdv.bvdvapi.models.RResult;
import com.bvdv.bvdvapi.repository.PtResultRepository;
import com.bvdv.bvdvapi.repository.RResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultService {
    @Autowired
    RResultRepository rResultRepository;

    @Autowired
    PtResultRepository ptResultRepository;

    public PtResult getTotalAnimalPtResults(String sessionId) {
        return ptResultRepository.findBySessionIdAndType(sessionId, PtResult.Type.ANIMAL);
    }

    public PtResult getTotalHerdsPtResults(String sessionId) {
        return ptResultRepository.findBySessionIdAndType(sessionId, PtResult.Type.HERD);
    }

    public List<RResult> getRResults(String sessionId) {
        return rResultRepository.findBySessionId(sessionId);
    }
}
