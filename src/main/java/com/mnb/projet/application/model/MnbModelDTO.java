package com.mnb.projet.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MnbModelDTO {

    private String nom;
    private String prenom;
    private String email;
    private AdresseExampleDTO adresse;
}