package agh.edu.pl.slpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany
    private List<Indication> indications;
}
