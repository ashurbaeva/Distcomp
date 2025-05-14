package com.example.rv1.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "tbl_creator")  // use the required "tbl_" prefix
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 64)
    @Column(nullable = false, length = 64, unique = true)
    private String login;

    @Size(min = 8, max = 128)
    @Column(nullable = false, length = 128)
    private String password;

    @Size(min = 2, max = 64)
    @Column(nullable = false, length = 64)
    private String firstname;

    @Size(min = 2, max = 64)
    @Column(nullable = false, length = 64)
    private String lastname;
}
