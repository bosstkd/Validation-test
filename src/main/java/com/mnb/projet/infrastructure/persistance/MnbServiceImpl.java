package com.mnb.projet.infrastructure.persistance;

import com.mnb.projet.domain.model.MnbModel;
import com.mnb.projet.domain.service.MnbService;
import com.mnb.projet.infrastructure.persistance.adapter.MnbPersistanceAdapter;
import com.mnb.projet.infrastructure.persistance.model.MnbEntity;
import com.mnb.projet.infrastructure.persistance.repository.MnbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MnbServiceImpl implements MnbService {

    private final MnbRepository repository;
    private final MnbPersistanceAdapter adapter;

    @Override
    public void createOrUpdate(MnbModel MnbModel) {

        repository.save(adapter.MnbModelToEntity(MnbModel));
    }

    @Override
    public Optional<MnbModel> findByEmail(String email) {
        List<MnbEntity> MnbEntities = repository.findByEmail(email);

        return CollectionUtils.isEmpty(MnbEntities) ?
                Optional.empty() :
                Optional.ofNullable(MnbEntities.get(0)).map(adapter::MnbEntityToModel);
    }
}
