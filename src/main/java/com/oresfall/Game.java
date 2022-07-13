package com.oresfall;

import com.oresfall.db.Database;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

import java.util.ArrayList;

public class Game {
    private String name;
    private ServerWorld world;
    private MinecraftServer server;
    private ArrayList<ServerPlayerEntity> players = new ArrayList<>();
    private final int maxPlayers = 20;
    private final int maxPlayersForTeam = maxPlayers/4;
    private final int plotXbyZ = 60;
    public Game(String name, ServerWorld world) {
        this.name = name;
        this.world = world;
        this.server = world.getServer();
    }

    public int joinPlayer(ServerPlayerEntity player) {
        if(players.size() == maxPlayers) return -1;

        players.add(player);
        return 0;
        //TODO: Teleport to start
    }

    public int leavePlayer(ServerPlayerEntity player) {
        if(!players.contains(player)) return -1;
        players.remove(player);
        return 0;
        //TODO: Teleport to lobby
    }


    public int removeGame() {
        if(!Database.ifGameExist(this)) return -1;
        Database.removeGame(this);
        players.forEach(this::leavePlayer);
        return 0;
    }

    public int startGame() {
        //TODO: teleport players to game
        //TODO: start game event
        return -1;
    }

    public int stopGame() {
        //TODO: teleport players to spawn
        //TODO: stop game event
        return -1;
    }

    public int killPlayer(ServerPlayerEntity player) {
        if(!players.contains(player)) return -1;
        player.changeGameMode(GameMode.getOrNull(3));
        //TODO: Spawn them on middle of map
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
