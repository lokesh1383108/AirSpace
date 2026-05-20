package com.example.airspace.AirSpace.models.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    protected ObjectId id;

    @NotBlank(message = "Name is required")
    protected String name;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    protected String email;

    @NotBlank(message = "Password is required")
    protected String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number should only contain digits and an optional leading '+'")
    protected String phoneNumber;
    protected UserType type;
    private Gender gender;

    @CreatedDate
    private LocalDateTime createdAt; // No @Setter annotation; prevents Lombok from creating a setter

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    private String fcmToken;
    protected String profilePicture;

    private boolean isVerified;




    protected UserType role;

    // Prepare the user for saving (e.g., setting createdAt, hashing password)
    public void prepareForSave() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
            this.lastLogin=this.createdAt;
            this.updatedAt=this.createdAt;
        }
    }




}


