package com.example.airspace.AirSpace.models.user;

import com.example.airspace.AirSpace.models.Property;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "hosts")
@Getter
@Setter
public class Host  {

    @Id
    private ObjectId id;
    @NotNull
    private ObjectId userId;
    private String organizationName;

    private String aboutUs;

    private List<ObjectId> propertyList;


    public LocalDateTime getHousingSince() {
        //need to think about it
//        Duration duration = Duration.between(this.getCreatedAt(), LocalDateTime.now());
//        return LocalDateTime.now().minus(duration);
        return  LocalDateTime.now();
    }

}
