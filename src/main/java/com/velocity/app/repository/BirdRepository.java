package com.velocity.app.repository;

import com.velocity.app.model.Bird;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

public interface BirdRepository extends ListCrudRepository<Bird, Long>, JpaSpecificationExecutor<Bird> {
}
