package com.oresfall.wallwars.playerclass;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import net.minecraft.server.network.ServerPlayerEntity;

public class Player {
    private final ServerPlayerEntity playerEntity;
    private Game game;
    private TeamBase team;
    private boolean inGame = false;
    private String name;
    public Player(ServerPlayerEntity player, TeamBase team) {
        playerEntity = player;
        inGame = false;
        this.team = team;
        name = player.getEntityName();
        Database.addPlayer(this);
    }

    public boolean joinGame(Game game) {
        if(game == this.game && inGame) return false;
        this.game = game;

        return game.joinPlayer(this);
    }

    public boolean party() {
        //TODO: party
        return false;
    }

    public ServerPlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void onDeath() {
        if(game == null) return;
        //TODO: GameMode to spectator
    }

    public String getName() {
        return name;
    }

    public boolean leaveGame() {
        if(game == null && !inGame) return false;
        this.game = null;
        this.inGame = false;
        return game.leavePlayer(this);
    }

    public boolean getInGame() {
        return inGame;
    }

    public void setTeam(TeamBase team) {
        this.team = team;
    }
}
