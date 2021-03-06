package com.molkky.molkky.domain;

import com.molkky.molkky.model.phase.PhaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import type.PhaseStatus;
import type.PhaseType;
import type.ScoreMode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "phase")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="typeDiscriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PHASE")
public class Phase  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status")
    private PhaseStatus status;

    @Column(name = "isTerrainAffectation")
    private Boolean terrainAffectation = false;

    @Column(name = "nbCourts")
    private Integer nbCourts;

    @Column(name = "nbPhase")
    private Integer nbPhase;

    @Column(name = "numStartCourt")
    private Integer numStartCourt;

    @Column(name = "isManagePlanning")
    private boolean managePlanning;

    @Column(name = "hourPhaseStart")
    private Time hourPhaseStart;

    @Column(name = "maxTimePhase")
    private Time timePhase;

    @Column(name = "scoreMode")
    private ScoreMode scoreMode;

    @Column(name = "nbSets")
    private Integer nbSets;

    @Column(name = "isSeedingSystem")
    private Boolean seedingSystem = true;

    @Column(name = "isTopSeeds")
    private Boolean topSeeds;

    @Column(name = "isRanking")
    private Boolean ranking = false;

    @Column(name = "notifBeginningPhase")
    private boolean notifBeginningPhase;

    @Column(name = "nbTeamsQualified")
    private Integer nbTeamsQualified;

    @Column(name = "isConsolation")
    private boolean consolation;

    @Column(name = "numberConsolationQualify")
    private Integer numberConsolationQualify;

    @Column(name = "isPlayoff")
    private boolean playoff;

    @Column(name = "numberPlayoffQualify")
    private Integer numberPlayoffQualify;


    @Column(name = "victoryValue")
    private Integer victoryValue = 1;

    @Column(name = "isFinished")
    private Boolean finished = false;

    @Column(name = "isStaffRandom")
    private Boolean randomStaff = false;

    @Column(name = "avoidConfrontationClub")
    private Boolean avoidConfrontationClub = false;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "phase_id")
    private List<Round> rounds = new ArrayList<>();

    @Transient
    public String getDiscriminatorValue(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        return val == null ? null : val.value();
    }

    public void setHourPhaseStart(String hourPhaseStart) {
        if(hourPhaseStart == null){
            this.hourPhaseStart = null;
        }
        else if(!hourPhaseStart.equals("")){
            hourPhaseStart = hourPhaseStart + ":00";
            this.hourPhaseStart = Time.valueOf(hourPhaseStart);
        }
        else{
            this.hourPhaseStart = null;
        }
    }

    public void setTimePhase(String timePhase) {
        if(timePhase == null){
            this.timePhase = null;
        }
        else if(!timePhase.equals("")){
            timePhase = timePhase + ":00";
            this.timePhase = Time.valueOf(timePhase);
        }
        else{
            this.timePhase = null;
        }
    }

    @Transient
    public PhaseType getDecriminatorValue() {
        String type = this.getClass().getAnnotation(DiscriminatorValue.class).value();
        return PhaseType.valueOf(type);
    }

    public void editGlobalInfo(PhaseModel phaseModel){
        this.setStatus(phaseModel.getStatus());
        this.setTerrainAffectation(phaseModel.getTerrainAffectation());
        this.setNbCourts(phaseModel.getNbCourts());
        this.setNbPhase(phaseModel.getNbPhase());
        this.setNumStartCourt(phaseModel.getNumStartCourt());
        this.setManagePlanning(phaseModel.isManagePlanning());

        this.setScoreMode(phaseModel.getScoreMode());
        this.setNbSets(phaseModel.getNbSets());
        this.setSeedingSystem(phaseModel.getSeedingSystem());
        this.setTopSeeds(phaseModel.getTopSeeds());
        this.setRanking(phaseModel.getRanking());
        this.setNotifBeginningPhase(phaseModel.isNotifBeginningPhase());
        this.setNbTeamsQualified(phaseModel.getNbTeamsQualified());
        this.setConsolation(phaseModel.isConsolation());
        this.setNumberConsolationQualify(phaseModel.getNumberConsolationQualify());
        this.setPlayoff(phaseModel.isPlayoff());
        this.setNumberPlayoffQualify(phaseModel.getNumberPlayoffQualify());
        this.setVictoryValue(phaseModel.getVictoryValue());
    }
}
