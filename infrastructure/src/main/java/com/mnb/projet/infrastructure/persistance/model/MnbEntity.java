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
@Entity(name = "mnb")
@Table(name = "mnb")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MnbEntity implements Serializable {

    @Id
    @Column(name = "mnb_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "mnbEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private AdresseExampleEntity adresse;
}
