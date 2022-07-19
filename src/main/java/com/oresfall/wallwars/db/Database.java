package com.oresfall.wallwars.db;

import com.google.gson.Gson;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Class for saving stuff
 */
public class Database {
    /**
     * List of games
     */
    private static ArrayList<Game> games = new ArrayList<>();
    /**
     * Place of lobby
     */
    private static Vec3d lobbyCoords = new Vec3d(0, 60, 0);
    private static ServerWorld lobbyWorld;

    public static ArrayList<Game> getGames() {
        return games;
    }

    /**
     * @return Lobby coordinates
     */
    public static Vec3d getLobbyCoords() {
        return lobbyCoords;
    }
    public static ServerWorld getLobbyWorld() {
        return lobbyWorld;
    }
    /**
     * Gets game by its name
     * @param gameName Name of game
     * @return game
     */
    @Nullable
    public static Game getGameByName(String gameName) {
        int i = Database.getGamesByName().indexOf(gameName);
        if(i >= 0) {
            return games.get(i);
        }
        return null;
    }

    /**
     * Returns list of games by name
     * @return Games by name
     */
    public static @NotNull ArrayList<String> getGamesByName() {
        ArrayList<String> gamesNames = new ArrayList<>();
        for(Game game : games) {
            gamesNames.add(game.toString());
        }
        return gamesNames;
    }
    /**
     * Adds game to list
     * @param game Game to add
     */
    public static void addGame(Game game) {
        games.add(game);
    }

    /**
     * Sets coordinates for lobby
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return 0 if good, -1 if it's already set
     */
    public static boolean setLobby(ServerWorld world,double x, double y, double z) {
        Vec3d coords = new Vec3d(x,y,z);
        if(coords == lobbyCoords) return false;
        lobbyCoords = coords;
        lobbyWorld = world;
        return true;
    }

    /**
     * Checks if game is in database
     * @param game Game to check
     * @return false/true
     */
    public static boolean ifGameExist(Game game) {
        if(game == null) {
            return false;
        }
        System.out.println(Database.getGamesByName());
        System.out.println(game);
        return games.contains(game);
    }

    /**
     * Checks if game by name is in database
     * @param name Name of game to check
     * @return false/true
     */
    public static boolean ifGameExist(String name) {
        Game game = getGameByName(name);
        if(game == null) {
            return false;
        }
        System.out.println(Database.getGamesByName());
        System.out.println(game);
        return games.contains(game);
    }

    /**
     * Removes game from list
     * @param game Game to remove
     */
    public static void removeGame(Game game) {
        games.remove(game);
    }

    /**
     * Saves games to json file
     */
    public static void saveGames() {
        Utils util = new Utils();
        Gson gson = new Gson();
        String json = gson.toJson(new Config());
        util.writeJsonFile(Main.configFile, json);
    }

    /**
     * Reads games from json file and adds them to database
     * @param server Game server
     */
    public static void readGames(MinecraftServer server) {
        Utils util = new Utils();
        setLobby(server.getOverworld(), 0, 60, 0);
        Config configData = util.readJsonFile(Main.configFile, Config.class);
        if(configData.global == null) {
            setLobby(server.getOverworld(), 0,60,0);
        } else {
            setLobby(Utils.getWorldByName(server,configData.global.world),
                    configData.global.coords[0],
                    configData.global.coords[1],
                    configData.global.coords[2]
            );
        }
        if(configData.games == null) return;
        for(Config.GamesTemplate gameData : configData.games) {
            if(getGameByName(gameData.name) != null) continue;
            Game game = new Game(gameData.name, Utils.getWorldByName(server, gameData.world));
            game.setSpawnCoords(gameData.spawnCoords[0], gameData.spawnCoords[1], gameData.spawnCoords[2]);
            Database.addGame(game);
        }
    }
}
