package com.mnb.projet.application;

import com.mnb.projet.domain.common.aspect.LogExecutionMarker;
import com.mnb.projet.domain.common.exceptions.MnbBadRequestException;
import com.mnb.projet.domain.common.exceptions.MnbNotFoundException;
import com.mnb.projet.domain.model.MnbModel;
import com.mnb.projet.domain.service.MnbService;
import com.mnb.projet.domain.validation.exception.Validator;
import com.mnb.projet.domain.validation.message.GenericValidatorMessages;
import com.mnb.projet.domain.validation.message.GenericValidatorPatterns;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
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
            throw new MnbBadRequestException("L'adresse mail" + GenericValidatorMessages.ERR_NON_VALIDE);
        }
        return service.findByEmail(email).orElseThrow(() -> new MnbNotFoundException("Utilisateur non existant !"));
    }
}
