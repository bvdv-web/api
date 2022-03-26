package com.bvdv.bvdvapi.controller;

import com.bvdv.bvdvapi.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"result", "result/"})
public class ResultController {
    @Autowired
    ResultService resultService;


    @GetMapping({"totalAnimals/{sessionId}"})
    public ResponseEntity<?> totalAnimals(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok().body(resultService.getTotalAnimalPtResults(sessionId));
    }

    @GetMapping({"totalHerds/{sessionId}"})
    public ResponseEntity<?> totalHerds(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok().body(resultService.getTotalHerdsPtResults(sessionId));
    }

    @GetMapping({"immune/{sessionId}"})
    public ResponseEntity<?> getImmuneResults(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok().body(resultService.getRResults(sessionId));
    }
}
