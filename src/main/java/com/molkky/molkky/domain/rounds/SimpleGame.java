package com.molkky.molkky.domain.rounds;

import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.model.phase.PhaseModel;
import lombok.Getter;
import lombok.Setter;
import type.PhaseStatus;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("SIMPLEGAME")
public class SimpleGame extends Phase {

    public SimpleGame(PhaseModel simpleModel, Tournament tournament) {
        this.setStatus(PhaseStatus.NOTSTARTED);
        this.setRandomStaff(simpleModel.getRandomStaff());
        this.setNbTeamsQualified(simpleModel.getNbTeamsQualified());
        this.setNbSets(simpleModel.getNbSets());
        this.setRanking(simpleModel.getRanking());
        this.setTopSeeds(simpleModel.getTopSeeds());
        this.setTerrainAffectation(simpleModel.getTerrainAffectation());
        this.setNbCourts(simpleModel.getNbCourts());
        this.setNumStartCourt(simpleModel.getNumStartCourt());
        this.setManagePlanning(simpleModel.isManagePlanning());
        this.setHourPhaseStart(simpleModel.getHourPhaseStart());
        this.setTimePhase(simpleModel.getTimePhase());
        this.setScoreMode(simpleModel.getScoreMode());
        this.setNotifBeginningPhase(simpleModel.isNotifBeginningPhase());
        this.setConsolation(simpleModel.isConsolation());
        this.setAvoidConfrontationClub(simpleModel.getAvoidConfrontationClub());
        this.setTournament(tournament);
    }

    public void editInfoSimple(PhaseModel simpleModel){
        this.editGlobalInfo(simpleModel);
    }

    public SimpleGame() {

    }
}
