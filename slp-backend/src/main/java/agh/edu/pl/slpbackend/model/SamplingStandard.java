package agh.edu.pl.slpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SamplingStandard {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
