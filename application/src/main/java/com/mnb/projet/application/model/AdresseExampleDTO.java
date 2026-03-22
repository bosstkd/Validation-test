package com.mnb.projet.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdresseExampleDTO {

    private String voie;
    private String codePostal;
    private String ville;
    private String pays;
}