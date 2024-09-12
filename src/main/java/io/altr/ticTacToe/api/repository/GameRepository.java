package io.altr.ticTacToe.api.repository;

import io.altr.ticTacToe.api.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameEntity, Integer> {
}
