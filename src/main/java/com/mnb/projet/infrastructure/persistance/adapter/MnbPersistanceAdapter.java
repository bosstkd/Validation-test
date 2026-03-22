package com.mnb.projet.infrastructure.persistance.adapter;

import com.mnb.projet.domain.common.tools.LocalObjectMapper;
import com.mnb.projet.domain.model.MnbModel;
import com.mnb.projet.infrastructure.persistance.model.MnbEntity;
import org.springframework.stereotype.Component;

@Component
public class MnbPersistanceAdapter {

    public MnbEntity MnbModelToEntity(MnbModel MnbModel) {

        return LocalObjectMapper.convertObjectToObject(MnbModel, MnbEntity.class);
    }

    public MnbModel MnbEntityToModel(MnbEntity MnbEntity) {
        MnbEntity.setId(null);
        return LocalObjectMapper.convertObjectToObject(MnbEntity, MnbModel.class);
    }
}
