package com.velocity.app.repository.specification;

import com.velocity.app.model.Sighting;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class SightingSpecification {

    public Specification<Sighting> getByBirdLocationAndTime(Long birdId, String location, LocalDateTime startTime, LocalDateTime endTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                Stream.of(
                                createPredicate(criteriaBuilder, root.get("bird").get("id"), birdId),
                                createPredicate(criteriaBuilder, root.get("location"), location),
                                createDateTimePredicate(criteriaBuilder, root.get("dateTime"), startTime, endTime)
                        )
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new)
        );
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, Object value) {
        return value != null ? criteriaBuilder.equal(path, value) : null;
    }

    private Predicate createDateTimePredicate(CriteriaBuilder criteriaBuilder, Path<LocalDateTime> path, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null) {
            return criteriaBuilder.between(path, startTime, endTime);
        } else if (startTime != null) {
            return criteriaBuilder.greaterThanOrEqualTo(path, startTime);
        } else if (endTime != null) {
            return criteriaBuilder.lessThanOrEqualTo(path, endTime);
        }
        return null;
    }
}
