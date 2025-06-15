package org.system.monitoring;

import com.google.cloud.firestore.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.system.monitoring.domain.collection.dto.response.TicketResponseDTO;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;

@SpringBootApplication
public class SystemMonitoringCollectorApplication implements CommandLineRunner {
    @Autowired
    private DocumentService documentService;

    public static void main(String[] args) {
        SpringApplication.run(SystemMonitoringCollectorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DefaultResponse defaultResponse=new DefaultResponse(null, TicketResponseDTO.class);
        //Filter filter = Filter.equalTo("valid", false);
        Filter filter = Filter.equalTo("residentRef.uidRef", "RO3N5HC9kxZnFEbJQkU4");
        documentService.list(defaultResponse, filter, TicketResponseDTO.class).get().forEach(System.out::println);
    }
}
