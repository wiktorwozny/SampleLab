package agh.edu.pl.slpbackend.repository.Specification;

import agh.edu.pl.slpbackend.model.Sample;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SampleSpecification {

    public static Specification<Sample> fuzzySearch(String term) {
        return (Root<Sample> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String likePattern = "%" + term.toLowerCase() + "%";

            Predicate codeIdPredicate = cb.like(cb.lower(root.get("code").get("id")), likePattern);
            Predicate assortmentNamePredicate = cb.like(cb.lower(root.get("assortment").get("name")), likePattern);
            Predicate assortmentGroupNamePredicate = cb.like(cb.lower(root.get("assortment").get("group").get("name")), likePattern);
            Predicate clientNamePredicate = cb.like(cb.lower(root.get("client").get("name")), likePattern);

            return cb.or(codeIdPredicate, assortmentNamePredicate, assortmentGroupNamePredicate, clientNamePredicate);
        };
    }
}
