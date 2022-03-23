package com.molkky.molkky.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "knockout")
public class Knockout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nbSets")
    private Integer nbSets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idKnockout")
    private List<Match> matches = new ArrayList<>();

    public Knockout(){
    }

    public Knockout(Integer nbSets){
        this.nbSets = nbSets;
    }
}
