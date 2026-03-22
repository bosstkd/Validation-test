package com.mnb.projet.infrastructure.persistance.repository;

import com.mnb.projet.infrastructure.persistance.model.MnbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MnbRepository extends JpaRepository<MnbEntity, Integer> {
    @Query("select u from mnb u where u.email like :email")
    List<MnbEntity> findByEmail(String email);
}
