package com.mnb.projet.infrastructure.persistance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "adresse")
@Table(name = "adresse")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdresseExampleEntity implements Serializable {

    @Id
    @Column(name = "adresse_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "voie")
    private String voie;

    @Column(name = "codePostal")
    private String codePostal;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private MnbEntity mnbEntity;
}
