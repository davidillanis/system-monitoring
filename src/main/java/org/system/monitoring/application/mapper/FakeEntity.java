package org.system.monitoring.application.mapper;

import com.github.javafaker.Faker;
import org.system.monitoring.application.util.EStatus;
import org.system.monitoring.domain.collection.dto.request.*;
import org.system.monitoring.infrastructure.firebase.util.ERole;

import java.util.Random;

public class FakeEntity {
    public static final Faker instance = new Faker();

    public static BonusRequestDTO bonusRequestDTO(){
        return new BonusRequestDTO(
                instance.commerce().productName(),
                instance.lorem().sentence(),
                instance.number().numberBetween(1, 100),
                instance.number().randomDouble(2, 0, 100)
        );
    }

    public static ResidentRequestDTO residentRequestDTO(){
        return new ResidentRequestDTO(
                instance.number().numberBetween(0, 100),
                instance.bool().bool(),
                instance.internet().uuid()
        );
    }

    public static TicketRequestDTO ticketRequestDTO(){
        return new TicketRequestDTO(
                instance.date().birthday(),
                instance.bool().bool(),
                instance.name().firstName(),
                instance.name().lastName()
        );
    }

    public static UserRequestDTO userRequestDTO(){
        return new UserRequestDTO(
                instance.name().firstName(),
                instance.name().lastName(),
                instance.number().digits(8),
                instance.internet().emailAddress(),
                instance.phoneNumber().phoneNumber(),
                instance.address().fullAddress(),
                instance.internet().image(),
                ERole.RECYCLER,
                EStatus.ACTIVE
        );
    }

    public static WorkerRequestDTO workerRequestDTO() {
        return new WorkerRequestDTO(
                instance.bool().bool(),
                instance.number().randomDouble(2, 1000, 5000),
                instance.date().birthday(),
                instance.idNumber().valid()
        );
    }

}
