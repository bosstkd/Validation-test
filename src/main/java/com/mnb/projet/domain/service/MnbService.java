package com.mnb.projet.domain.service;

import com.mnb.projet.domain.model.MnbModel;

import java.util.Optional;

public interface MnbService {

    void createOrUpdate(MnbModel MnbModel);
    Optional<MnbModel> findByEmail(String email);
}
