package com.oresfall.db;

import com.oresfall.Game;

import java.util.ArrayList;

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
