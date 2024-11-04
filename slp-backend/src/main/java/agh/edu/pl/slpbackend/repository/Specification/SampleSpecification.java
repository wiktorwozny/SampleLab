package agh.edu.pl.slpbackend.repository.Specification;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.Sample;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class SampleSpecification {

    public static Specification<Sample> fuzzySearch(String term) {
        return (Root<Sample> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String likePattern = "%" + term.toLowerCase() + "%";

            Predicate codeIdPredicate = cb.like(cb.lower(root.get("code").get("id")), likePattern);
            Predicate assortmentNamePredicate = cb.like(cb.lower(root.get("assortment").get("name")), likePattern);
            Predicate assortmentGroupNamePredicate = cb.like(cb.lower(root.get("assortment").get("group").get("name")), likePattern);
            Predicate clientNamePredicate = cb.like(cb.lower(root.get("client").get("name")), likePattern);

            Join<Sample, Examination> examinationJoin = root.join("examinations", JoinType.LEFT);
            Join<Examination, Indication> indicationJoin = examinationJoin.join("indication", JoinType.LEFT);

            Predicate indicationNamePredicate = cb.like(cb.lower(indicationJoin.get("name")), likePattern);
            Predicate indicationMethosPredicate = cb.like(cb.lower(indicationJoin.get("method")), likePattern);


            return cb.or(codeIdPredicate, assortmentNamePredicate, assortmentGroupNamePredicate, clientNamePredicate, indicationNamePredicate, indicationMethosPredicate);
        };
    }
}
