package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.utls.Utils;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class to create instances of games
 */
public class Game {
    /**
     * Name of game
     */
    private String name;
    /**
     * World of game
     */
    private ServerWorld world;
    /**
     * Server of game
     */
    private MinecraftServer server;
    /**
     * List of players that joined game
     */
    private ArrayList<ServerPlayerEntity> players = new ArrayList<>();
    /**
     * Max number of players that can join game
     */
    private int maxPlayers = 20;
    /**
     * Max number of players in team
     */
    private final int maxPlayersForTeam = maxPlayers/4;
    /**
     * Max size of plot (X,Z)
     */
    private int plotXbyZ = 60 * 6;
    /**
     * Spawn place (the place before staring of game) X,Y,Z
     */
    private Vec3d spawnPlace = new Vec3d(0, 60, 0);

    /**
     * @param name Name of game
     * @param world World of game
     */
    public Game(String name, @NotNull ServerWorld world) {
        this.name = name;
        this.world = world;
        this.server = world.getServer();
    }

    /**
     * @return Value of spawnPlace
     */
    public Vec3d getSpawnCoords() {
        return spawnPlace;
    }

    /**
     * @return Value of world
     */
    public ServerWorld getWorld() {
        return world;
    }

    /**
     * @return Value of players
     */
    public ArrayList<ServerPlayerEntity> getPlayers() {
        return players;
    }

    /**
     * @return List of players by their name
     */
    public ArrayList<String> getPlayersByName() {
        ArrayList<String> playersByName = new ArrayList<String>();
        for(ServerPlayerEntity player : players) {
            if(player == null) continue;
            playersByName.add(player.getEntityName());
        }
        return playersByName;
    }

    /**
     * Sets world of game
     * @param world World to set for game
     * @return 0 if good, -1 if it's already set
     */
    public boolean setWorld(ServerWorld world) {
        if(getWorld() == world) return false;
        this.world = world;
        return true;
    }

    /**
     * Sets coordinates of spawn place (place before game)
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return 0 if good, -1 if it's already set
     */
    public boolean setSpawnCoords(double x, double y, double z) {
        var place = new Vec3d(x,y,z);
        if(place.equals(spawnPlace)) return false;
        spawnPlace = place;
        return true;
    }

    /**
     * Adds player to list of players and teleports them to spawnPlace
     * @param player Player to join
     * @return 0 if good, -1 if there is too many players
     */
    public boolean joinPlayer(ServerPlayerEntity player) {
        if(players.size() == maxPlayers) return false;
        IEntityDataSaver playerData = (IEntityDataSaver)player;

        playerData.getPersistentData().putLongArray("LocationBefore", new long[]{
                (long)player.getX(),
                (long)player.getY(),
                (long)player.getZ()
        });
        playerData.getPersistentData().putString("DimBefore", player.getEntityWorld().getRegistryKey().getValue().toString());
        players.add(player);
        return true;
    }

    /**
     * Removes player from list of players and teleports to lobby
     * @param player Player to leave
     * @return 0 if good, -1 if player doesn't exist
     */
    public boolean leavePlayer(ServerPlayerEntity player) {
        if(!players.contains(player)) return false;
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
        return true;
    }

    /**
     * Removes game from Database and runs leavePlayer for each player
     * @return 0 if good, -1 if game doesn't exist
     */
    public boolean removeGame() {
        if(!Database.ifGameExist(this)) return false;
        Database.removeGame(this);
        players.forEach(this::leavePlayer);
        return true;
    }

    /**
     * Starts game event
     * @return TODO
     */
    public int startGame() {
        //TODO: teleport players to game
        //TODO: start game event
        return -1;
    }

    /**
     * Starts end game event
     * @return TODO
     */
    public int endGame() {
        //TODO: teleport players to spawn
        //TODO: stop game event
        return -1;
    }

    /**
     * Teleport player to middle of map and makes them spectator
     * @param player Player that died
     */
    public void killPlayer(@NotNull ServerPlayerEntity player) {
        player.changeGameMode(GameMode.getOrNull(3));
        leavePlayer(player);
        //TODO: Spawn them on middle of map
    }

    /**
     * @return Name of game
     */
    @Override
    public String toString() {
        return name;
    }
}
