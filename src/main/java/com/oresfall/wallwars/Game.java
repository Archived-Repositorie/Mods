package com.oresfall.wallwars;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.utls.Utils;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private String name;
    private ServerWorld world;
    private MinecraftServer server;
    private ArrayList<ServerPlayerEntity> players = new ArrayList<>();
    private final int maxPlayers = 20;
    private final int maxPlayersForTeam = maxPlayers/4;
    private final int plotXbyZ = 60;
    private final Vec3d lobbyPlace = new Vec3d(0, 60, 0);
    private Vec3d spawnPlace = new Vec3d(0, 60, 0);
    private final ArrayList<Vec3d> teleportPlaces = new ArrayList<Vec3d>(Arrays.asList(
            new Vec3d(0, 60, 0),
            new Vec3d(0, 60, 0),
            new Vec3d(0, 60, 0)
    )); //TODO: Configurable through command
    public Game(String name, ServerWorld world) {
        this.name = name;
        this.world = world;
        this.server = world.getServer();
    }

    public int joinPlayer(ServerPlayerEntity player) {
        if(players.size() == maxPlayers) return -1;
        IEntityDataSaver playerData = (IEntityDataSaver)player;

        playerData.getPersistentData().putLongArray("LocationBefore", new long[]{
                (long)player.getX(),
                (long)player.getY(),
                (long)player.getZ()
        });
        playerData.getPersistentData().putString("DimBefore", player.getEntityWorld().getRegistryKey().getValue().toString());
        players.add(player);
        return 0;
        //TODO: Teleport to start
    }

    public int leavePlayer(ServerPlayerEntity player) {
        if(!players.contains(player)) return -1;
        players.remove(player);

        IEntityDataSaver playerData = (IEntityDataSaver)player;
        long[] playerBeforePos = playerData.getPersistentData().getLongArray("LocationBefore");
        ServerWorld playerBeforeDim = Utils.getWorldByName(player.getServer(), ((IEntityDataSaver)player).getPersistentData().getString("DimBefore"));
        FabricDimensions.teleport(player, playerBeforeDim, new TeleportTarget(
                new Vec3d(
                        playerBeforePos[0],
                        playerBeforePos[1],
                        playerBeforePos[2]
                ),
                player.getVelocity(),
                player.getYaw(),
                player.getPitch()
        ));
        return 0;
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

    public ServerWorld getWorld() {
        return world;
    }

    public ArrayList<ServerPlayerEntity> getPlayers() {
        return players;
    }

    public ArrayList<String> getPlayersByName() {
        ArrayList<String> playersByName = new ArrayList<String>();
        for(ServerPlayerEntity player : players) {
            if(player == null) continue;
            playersByName.add(player.getEntityName());
        }
        return playersByName;
    }

    public int setWorld(ServerWorld world) {
        if(getWorld() == world) return -1;
        this.world = world;
        return 0;
    }

    public int setSpawnCoords(int x, int y, int z) {
        var place = new Vec3d(x,y,z);
        if(place.equals(spawnPlace)) return -1;
        spawnPlace = place;
        return 0;
    }

    public int[] getSpawnCoords() {
        return new int[]{(int) spawnPlace.x, (int) spawnPlace.y, (int) spawnPlace.z};
    }
}
