package com.bvdv.bvdvapi.service;

import com.bvdv.bvdvapi.models.BvdvRequest;
import com.bvdv.bvdvapi.models.ConsoleResult;
import com.bvdv.bvdvapi.models.UserDetail;
import com.bvdv.bvdvapi.repository.BvdvRequestRepository;
import com.bvdv.bvdvapi.repository.ConsoleResultRepository;
import com.bvdv.bvdvapi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BvdvService {
    @Autowired
    BvdvRabbitmqProducer bvdvRabbitmqProducer;
    @Autowired
    ConsoleResultRepository consoleResultRepository;
    @Autowired
    BvdvRequestRepository bvdvRequestRepository;

    public BvdvRequest bvdvRun(BvdvRequest request) {
        String sessionId = StringUtil.randomSessionId();
        log.info("New request " + sessionId);

        ConsoleResult consoleResult = consoleResultRepository.save(sessionId, request.toConsoleResult());
        request.setSessionId(consoleResult.getId());
        request.setUserId(UserDetail.getAuthorizedUser().map(UserDetail::getId).orElse(null));
        bvdvRabbitmqProducer.produce(request);
        bvdvRequestRepository.save(request);
        return request;

    }

    public List<BvdvRequest> listBvdvRequests() {
        return UserDetail.getAuthorizedUser().map(user -> bvdvRequestRepository.findByUserIdOrderByCreatedAtDesc(user.getId())).orElse(null);
    }

    public Optional<BvdvRequest> getBvdvRequest(String sessionId) {
        return bvdvRequestRepository.findBySessionId(sessionId);
    }
}
