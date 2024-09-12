package io.altr.ticTacToe.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer gameId;
    @JsonIgnore
    private Integer tileMask;

    public GameEntity(Integer gameId, Integer tileMask){
        this.gameId = gameId;
        this.tileMask = tileMask;
    }

    public GameEntity(Integer tileMask){
        this.tileMask = tileMask;
    }

    public GameEntity(){
        this.tileMask = 0;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getTileMask(){
        return tileMask;
    }
}
