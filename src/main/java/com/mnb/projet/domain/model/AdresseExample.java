package com.mnb.projet.domain.model;

import com.mnb.projet.domain.validation.message.GenericValidatorMessages;
import com.mnb.projet.domain.validation.message.GenericValidatorPatterns;
import com.mnb.projet.domain.validation.rule.annotation.AssertTrue;
import com.mnb.projet.domain.validation.rule.annotation.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdresseExample {

    @NotBlank(message = "La voie" + GenericValidatorMessages.ERR_BLANC)
    private String voie;

    @NotBlank(message = "Le code postal" + GenericValidatorMessages.ERR_BLANC)
    private String codePostal;

    private String ville;

    @NotBlank(message = "Le pays" + GenericValidatorMessages.ERR_BLANC)
    private String pays;

    @AssertTrue(message = "le code-postal ne correspond pas à la ville")
    public boolean hasCorrectVille() {
        if (StringUtils.isBlank(this.ville) || StringUtils.isBlank(this.codePostal)) {
            return true;
        }
        return !this.codePostal.equals("69001") || this.ville.equalsIgnoreCase("Lyon");
    }

    @AssertTrue(message = "Erreur validation adresse")
    public boolean hasValidCodePostalPourPays() {
        if (StringUtils.isBlank(this.pays) || StringUtils.isBlank(this.codePostal)) {
            return true;
        }
        if (this.pays.equalsIgnoreCase("France")) {
            return this.codePostal.matches(GenericValidatorPatterns.REG_FRANCE_CODE_POSTAL);
        }
        return true;
    }
}