package com.velocity.app.repository.specification;

import com.velocity.app.model.Bird;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class BirdSpecification {
    public Specification<Bird> getByNameAndColor(String name, String color) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                Stream.of(
                                createPredicate(criteriaBuilder, root.get("name"), name),
                                createPredicate(criteriaBuilder, root.get("color"), color)
                        )
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new)
        );
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Path<String> path, String value) {
        return value != null ? criteriaBuilder.equal(path, value) : null;
    }
}
