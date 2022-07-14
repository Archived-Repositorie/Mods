package com.oresfall.db;

import com.google.gson.Gson;
import com.oresfall.Game;
import com.oresfall.Main;
import com.oresfall.utls.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {
    private static ArrayList<Game> games = new ArrayList<>();

    public static void addGame(Game game) {
        games.add(game);
    }

    public static boolean ifGameExist(Game game) {
        if(game == null) {
            return false;
        }
        System.out.println(Database.getGamesByName());
        System.out.println(game);
        return games.contains(game);
    }

    public static void saveGames() {
        Gson gson = new Gson();
        String json = gson.toJson(new SaveGame(games));
        String configDir = FabricLoader.getInstance().getConfigDir().toString();
        try {
            PrintWriter writer = new PrintWriter(configDir+"/"+Main.modid+"/data.json", StandardCharsets.UTF_8);
            writer.println(json);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Main.LOGGER.info(json);
    }

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
        SaveGameBase gamesData = gson.fromJson(data,SaveGameBase.class);
        for(String[] gameData : gamesData.games) {
            if(gameData == null) continue;
            Game game = new Game(gameData[0], Utils.getWorldByName(server, gameData[1]));
            Database.addGame(game);
        }
    }

    public static void removeGame(Game game) {
        games.remove(game);
    }

    public static Game getGameByName(String gameName) {
        int i = Database.getGamesByName().indexOf(gameName);
        if(i >= 0) {
            return games.get(i);
        }
        return null;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static ArrayList<String> getGamesByName() {
        ArrayList<String> gamesNames = new ArrayList<>();
        for(Game game : games) {
            gamesNames.add(game.toString());
        }
        return gamesNames;
    }
}
