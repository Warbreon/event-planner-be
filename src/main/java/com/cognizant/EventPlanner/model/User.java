package com.cognizant.EventPlanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "job_title")
    private String jobTitle;

    @Pattern(regexp = "^(?:\\+\\d{1,3} ?\\d{1,14}|[1-9]\\d{1,14})$", message = "Invalid phone number")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @NotBlank(message = "Password cannot be empty")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Event> createdEvents;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attendee> attendees;
}
