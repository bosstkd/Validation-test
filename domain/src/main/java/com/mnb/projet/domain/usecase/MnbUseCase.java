package com.mnb.projet.domain.usecase;

import com.mnb.projet.domain.common.DomainService;
import com.mnb.projet.domain.common.aspect.LogExecutionMarker;
import com.mnb.projet.domain.common.exceptions.DomainBadRequestCommandException;
import com.mnb.projet.domain.common.exceptions.DomainResourceNotFoundException;
import com.mnb.projet.domain.model.MnbModel;
import com.mnb.projet.domain.service.MnbService;
import com.mnb.projet.domain.validation.exception.Validator;
import com.mnb.projet.domain.validation.message.GenericValidatorMessages;
import com.mnb.projet.domain.validation.message.GenericValidatorPatterns;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@DomainService
@RequiredArgsConstructor
public class MnbUseCase {

    private final MnbService service;

    @LogExecutionMarker
    public MnbModel createMnb(MnbModel mnbModel) {
      Validator.validate(mnbModel);
        service.createOrUpdate(mnbModel);
        return mnbModel;
    }

    public MnbModel findMnb(String email) {
        if (StringUtils.isBlank(email) || !email.matches(GenericValidatorPatterns.REG_EMAIL)) {
            throw new DomainBadRequestCommandException("L'adresse mail" + GenericValidatorMessages.ERR_NON_VALIDE);
        }
        return service.findByEmail(email).orElseThrow(() -> new DomainResourceNotFoundException("Utilisateur non existant !"));
    }
}
