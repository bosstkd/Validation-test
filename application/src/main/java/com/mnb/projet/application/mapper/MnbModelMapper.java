package com.mnb.projet.application.mapper;

import com.mnb.projet.application.model.AdresseExampleDTO;
import com.mnb.projet.application.model.MnbModelDTO;
import com.mnb.projet.domain.model.AdresseExample;
import com.mnb.projet.domain.model.MnbModel;

public class MnbModelMapper {

    private MnbModelMapper() {
    }

    public static MnbModelDTO fromDomain(MnbModel mnbModel) {
        if (mnbModel == null) {
            return null;
        }
        return MnbModelDTO.builder()
                .nom(mnbModel.getNom())
                .prenom(mnbModel.getPrenom())
                .email(mnbModel.getEmail())
                .adresse(fromDomain(mnbModel.getAdresse()))
                .build();
    }

    public static MnbModel toDomain(MnbModelDTO mnbModelDTO) {
        if (mnbModelDTO == null) {
            return null;
        }
        return MnbModel.builder()
                .nom(mnbModelDTO.getNom())
                .prenom(mnbModelDTO.getPrenom())
                .email(mnbModelDTO.getEmail())
                .adresse(toDomain(mnbModelDTO.getAdresse()))
                .build();
    }

    public static AdresseExampleDTO fromDomain(AdresseExample adresse) {
        if (adresse == null) {
            return null;
        }
        return AdresseExampleDTO.builder()
                .voie(adresse.getVoie())
                .codePostal(adresse.getCodePostal())
                .ville(adresse.getVille())
                .pays(adresse.getPays())
                .build();
    }

    public static AdresseExample toDomain(AdresseExampleDTO adresseDTO) {
        if (adresseDTO == null) {
            return null;
        }
        return AdresseExample.builder()
                .voie(adresseDTO.getVoie())
                .codePostal(adresseDTO.getCodePostal())
                .ville(adresseDTO.getVille())
                .pays(adresseDTO.getPays())
                .build();
    }
}