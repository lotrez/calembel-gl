package com.molkky.molkky.domain;

import com.molkky.molkky.model.TournamentModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Setter
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private Date date;

    @Column(name = "cutOffDate")
    private Date cutOffDate;

    @Column(name = "minTeam")
    private Integer minTeam;

    @Column(name = "maxTeam")
    private Integer maxTeam;

    @Column(name = "visible")
    private boolean visible;

    @Column(name = "nbRounds")
    private Integer nbRounds;

    //XXX
    @Column(name = "nbCourts")
    private Integer nbCourts;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "started")
    private boolean started = false;

    @OneToMany(mappedBy="tournament")
    private Set<User> users;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="tournament")
    private List<Round> rounds;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="tournament")
    private List<Team> teams;

    @Column(name = "indexPhase")
    private Integer indexPhase = 0;

    @Column(name = "finished")
    private boolean finished;

    public Tournament(String name, String location, Date date, Date cutOffDate, Integer minTeam, Integer maxTeam, boolean visible, Integer nbRounds, Integer nbCourts) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.cutOffDate = cutOffDate;
        this.minTeam = minTeam;
        this.maxTeam = maxTeam;
        this.visible = visible;
        this.nbRounds = nbRounds;
        this.nbCourts = nbCourts;
    }

    public Tournament(TournamentModel tournamentModel) {
        this.name = tournamentModel.getName();
        this.location = tournamentModel.getLocation();
        this.date = tournamentModel.getDate();
        this.cutOffDate = tournamentModel.getCutOffDate();
        this.minTeam = tournamentModel.getMinTeam();
        this.maxTeam = tournamentModel.getMaxTeam();
        this.visible = tournamentModel.isVisible();
        this.nbRounds = tournamentModel.getNbRounds();
        this.nbCourts = tournamentModel.getNbCourts();
    }

 
    public Tournament() {
    }
}
