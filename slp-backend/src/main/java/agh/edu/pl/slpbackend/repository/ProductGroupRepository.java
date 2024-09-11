package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
}
