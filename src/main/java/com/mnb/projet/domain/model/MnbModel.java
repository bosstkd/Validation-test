package com.mnb.projet.domain.model;

import com.mnb.projet.domain.validation.message.GenericValidatorMessages;
import com.mnb.projet.domain.validation.message.GenericValidatorPatterns;
import com.mnb.projet.domain.validation.rule.annotation.NotEmpty;
import com.mnb.projet.domain.validation.rule.annotation.NotNull;
import com.mnb.projet.domain.validation.rule.annotation.Pattern;
import com.mnb.projet.domain.validation.rule.annotation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MnbModel {

    @NotEmpty(message = "Le nom" + GenericValidatorMessages.ERR_OBLIGATOIRE_SINGULIER)
    @Pattern(regexp = GenericValidatorPatterns.REG_NAME, message = "Le nom" + GenericValidatorMessages.ERR_NON_VALIDE)
    private String nom;

    @NotEmpty(message = "Le prenom" + GenericValidatorMessages.ERR_OBLIGATOIRE_SINGULIER)
    @Pattern(regexp = GenericValidatorPatterns.REG_NAME, message = "Le prenom" + GenericValidatorMessages.ERR_NON_VALIDE)
    private String prenom;

    @NotEmpty(message = "L'email" + GenericValidatorMessages.ERR_OBLIGATOIRE_SINGULIER)
    @Pattern(regexp = GenericValidatorPatterns.REG_EMAIL, message = "L'email" + GenericValidatorMessages.ERR_NON_VALIDE)
    private String email;

    @NotNull(message = "L'adresse" + GenericValidatorMessages.ERR_NULL)
    @Valid
    private AdresseExample adresse;
}
