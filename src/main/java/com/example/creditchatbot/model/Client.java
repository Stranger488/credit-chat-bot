package com.example.creditchatbot.model;

import lombok.*;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String generatedUniqueName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    private LocalDate birthDate;

    @Column
    private String phone;

    @Column
    private String passportNum;

    @Column
    private LocalDate passportDate;

    @Column
    private String passportOrg;

    @Column
    private String address;

    @Column
    private String jobPlace;

    @Column
    private String jobPosition;

    @Column
    private String jobExp;

    @Column
    private int creditAmount;

    @Column
    private int creditTerm;

    @Column
    private double creditRate;

    @Column
    private String city;

}
