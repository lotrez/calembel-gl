package com.molkky.molkky.domain.rounds;

import Type.RoundType;
import com.molkky.molkky.domain.Round;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@Setter
@DiscriminatorValue("Pool")
public class Pool extends Round{
    public Pool(){
//        this.setType(RoundType.POOL);
    }

    public Pool(Integer nbTeams){
        super(RoundType.POOL, nbTeams);
    }
}
