package com.bvdv.bvdvapi.repository;

import com.bvdv.bvdvapi.models.ConsoleResult;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsoleResultRepository {
    @Autowired
    Firestore firestore;

    private static final String COLLECTION = "consoleResults";

    public ConsoleResult save(String id, ConsoleResult consoleResult) {
        consoleResult.setId(id);
        try {
            DocumentReference docRef = firestore.collection(COLLECTION).document(id);
            docRef.set(consoleResult);
        } catch (Exception e) {
            log.error("Cannot write to firebase");
        }
        return consoleResult;
    }

    @SneakyThrows
    public ConsoleResult get(String id) {
        DocumentReference document = firestore.collection(COLLECTION).document(id);
        return document.get().get().toObject(ConsoleResult.class);
    }
}
