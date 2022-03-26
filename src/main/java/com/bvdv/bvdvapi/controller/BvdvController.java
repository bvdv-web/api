package com.bvdv.bvdvapi.controller;

import com.bvdv.bvdvapi.models.BvdvRequest;
import com.bvdv.bvdvapi.service.BvdvService;
import com.bvdv.bvdvapi.service.GoogleDriveFileService;
import com.bvdv.bvdvapi.service.GoogleDriveFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"bvdv", "bvdv/"})
public class BvdvController {
    @Autowired
    GoogleDriveFileService googleDriveFileService;

    @Autowired
    GoogleDriveFolderService googleDriveFolderService;

    @Autowired
    BvdvService bvdvService;

    @PostMapping({"/", ""})
    public ResponseEntity<?> runBvdv(@RequestBody BvdvRequest request) {
        return ResponseEntity.ok().body(bvdvService.bvdvRun(request));
    }

    @GetMapping({"/", ""})
    public ResponseEntity<?> listSimulations() {
        return ResponseEntity.ok().body(bvdvService.listBvdvRequests());
    }

    @GetMapping({"/{sessionId}", "{sessionId}"})
    public ResponseEntity<?> getSimulation(@PathVariable("sessionId") String sessionId) {
        Optional<BvdvRequest> bvdvRequest = bvdvService.getBvdvRequest(sessionId);
        if (bvdvRequest.isPresent()) {
            return ResponseEntity.ok().body(bvdvRequest.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
