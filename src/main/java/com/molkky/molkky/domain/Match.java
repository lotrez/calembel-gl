package com.molkky.molkky.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "molkky_match")
public class Match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Court.class)
    @JoinColumn(name = "idCourt")
    private Court court;

    @ManyToMany
    @JoinTable(
            name = "match_team",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams;

    @ManyToOne(optional = true)
    @JoinColumn(name="idRound", nullable = true)
    private Round round;

    @OneToMany(mappedBy = "match")
    private List<Shot> shots;

    @Column(name = "scoreTeam1")
    private Integer scoreTeam1 = 0;

    @Column(name = "scoreTeam2")
    private Integer scoreTeam2 = 0;

    @Column(name = "finished")
    private Boolean finished= false;

    public Match(Court court, List<Team> teams) {
        this.court = court;
        this.teams = teams;
    }

    public Match() {
    }
}
