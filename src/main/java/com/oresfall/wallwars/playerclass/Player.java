package com.oresfall.wallwars.playerclass;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class Player {
    private final ServerPlayerEntity playerEntity;
    private Game game;
    private TeamBase team;
    private boolean inGame = false;
    private String name;
    public Player(ServerPlayerEntity player) {
        playerEntity = player;
        inGame = false;
        name = player.getEntityName();
        Database.addPlayer(this);
    }

    public Player(String playerUUID, MinecraftServer server) {
        playerEntity = server.getPlayerManager().getPlayer(UUID.fromString(playerUUID));
        inGame = false;
        name = playerEntity.getEntityName();
        Database.addPlayer(this);
    }

    public boolean joinGame(Game game) {
        if(game == this.game && inGame) return false;
        this.game = game;
        this.inGame = true;

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
        playerEntity.changeGameMode(GameMode.SPECTATOR);
        Database.addSpecPlayer(this);
    }


    public String getName() {
        return name;
    }

    public TeamBase getTeam() {
        return team;
    }

    public boolean leaveGame() {
        if(game == null && !inGame) return false;
        this.inGame = false;
        return game.leavePlayer(this);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean getInGame() {
        return inGame;
    }

    public void setTeam(TeamBase team) {
        this.team = team;
    }
}
