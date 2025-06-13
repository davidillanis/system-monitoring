package org.system.monitoring.infrastructure.firebase.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initFirebase() throws IOException {
        //FileInputStream serviceAccount = new FileInputStream("src/main/resources/credential-firebase.json");
        FileInputStream serviceAccount = new FileInputStream("/etc/secrets/credential-firebase.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }


    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
