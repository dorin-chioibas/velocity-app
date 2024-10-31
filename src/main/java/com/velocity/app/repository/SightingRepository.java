package com.velocity.app.repository;

import com.velocity.app.model.Sighting;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SightingRepository extends ListCrudRepository<Sighting, Long>, JpaSpecificationExecutor<Sighting> {
    List<Sighting> findByBirdId(Long birdId);
}
