package com.oresfall.wallwars.db;

import com.google.gson.Gson;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.utls.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

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
    public static ArrayList<Game> getGames() {
        return games;
    }

    /**
     * @return Lobby coordinates
     */
    public static Vec3d getLobbyCoords() {
        return lobbyCoords;
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
    public static int setLobby(double x, double y, double z) {
        Vec3d coords = new Vec3d(x,y,z);
        if(coords == lobbyCoords) return -1;
        lobbyCoords = coords;
        return 0;
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
        Gson gson = new Gson();
        String json = gson.toJson(new SaveGame(games));
        String configDir = FabricLoader.getInstance().getConfigDir().toString();
        try {
            PrintWriter writer = new PrintWriter(configDir+"/"+Main.modid+"/data.json", StandardCharsets.UTF_8);
            writer.println(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads games from json file and adds them to database
     * @param server Game server
     */
    public static void readGames(MinecraftServer server) {
        Gson gson = new Gson();
        String configDir = FabricLoader.getInstance().getConfigDir().toString();
        File myObj = new File(configDir+"/"+Main.modid+"/data.json");
        String data = "";
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data+=(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SaveGame gamesData = gson.fromJson(data,SaveGame.class);
        if(gamesData.games == null) return;
        for(SaveGame.Template gameData : gamesData.games) {
            if(getGameByName(gameData.name) != null) continue;
            Game game = new Game(gameData.name, Utils.getWorldByName(server, gameData.world));
            game.setSpawnCoords(gameData.spawnCoords[0], gameData.spawnCoords[1], gameData.spawnCoords[2]);
            Database.addGame(game);
        }
    }
}
