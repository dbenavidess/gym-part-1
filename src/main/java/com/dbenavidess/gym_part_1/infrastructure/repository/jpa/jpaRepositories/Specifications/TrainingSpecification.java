package com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.Specifications;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.TrainingEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.UUID;

public class TrainingSpecification {

    public static Specification<TrainingEntity> filter(
            UUID trainerId,
            Date fromDate,
            Date toDate,
            UUID traineeId,
            UUID trainingTypeId
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (trainerId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("trainer").get("id"), trainerId));
            }

            if (traineeId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("trainee").get("id"), traineeId));
            }

            if (trainingTypeId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type").get("id"), trainingTypeId));
            }

            if (fromDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), toDate));
            }

            return predicate;
        };
    }
}