package com.mnb.projet.application.service;

import com.mnb.projet.application.mapper.MnbModelMapper;
import com.mnb.projet.application.model.MnbModelDTO;
import com.mnb.projet.domain.usecase.MnbUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MnbApplicationService {

    private final MnbUseCase mnbUseCase;

    public MnbModelDTO createMnb(MnbModelDTO mnbModelDTO) {
        return MnbModelMapper.fromDomain(
                mnbUseCase.createMnb(MnbModelMapper.toDomain(mnbModelDTO))
        );
    }

    public MnbModelDTO findMnb(String email) {
        return MnbModelMapper.fromDomain(
                mnbUseCase.findMnb(email)
        );
    }
}