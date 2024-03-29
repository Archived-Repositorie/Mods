package com.oresfall.wallwars.playerclass;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class Player {
    private ServerPlayerEntity playerEntity;
    private Game game;
    private TeamBase team;
    private boolean inGame = false;
    private String name;
    private UUID ID;

    private boolean dead = false;

    private int phase = -2;
    public Player(ServerPlayerEntity player) {
        playerEntity = player;
        name = player.getEntityName();
        ID = player.getUuid();
    }

    public Player(String name,UUID ID) {
        this.ID = ID;
        this.name = name;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Game getGame() {
        return game;
    }

    public int getPhase() {
        return phase;
    }

    public void loadPlayerEntity(MinecraftServer server) {
        this.playerEntity = server.getPlayerManager().getPlayer(ID);
    }

    public void setPlayerEntity(ServerPlayerEntity player) {
        this.playerEntity = player;
    }

    public void setDead(boolean isDeath) {
        dead = isDeath;
    }

    public boolean joinGame(Game game) {
        if(game == this.game) return false;
        this.game = game;
        this.inGame = true;

        return game.joinPlayer(this);
    }

    public UUID getID() {
        return ID;
    }

    public boolean party() {
        //TODO: party
        return false;
    }

    public ServerPlayerEntity getPlayerEntity(MinecraftServer server) {
        if(playerEntity == null)loadPlayerEntity(server);
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
        if(game == null || !inGame) return false;
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
