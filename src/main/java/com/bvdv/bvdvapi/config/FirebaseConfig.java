package com.bvdv.bvdvapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.project.id}")
    private String FIREBASE_PROJECT_ID;

    @Value("${firebase.config}")
    private String FIREBASE_CONFIG_FILE;

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_FILE).getInputStream());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setDatabaseUrl("http://localhost:8080?ns=bvdv-us")
                .setProjectId(FIREBASE_PROJECT_ID)
                .setConnectTimeout(3000)
                .setReadTimeout(3000)
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
}
