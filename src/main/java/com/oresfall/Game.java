package com.oresfall;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class Game {
    private static String nameGame;
    private static int maxPeopleInGame;
    private static double startTime;
    private static ArrayList<ServerPlayerEntity> players;

    public static void createGame(String name, int maxPeople, double startTicks) {
        nameGame = name;
        maxPeopleInGame = maxPeople;
        startTime = startTicks;
        //Info.games.add(Game);
    }


    public static void start() {
        startTime = 5 * 20;
    }

    public static boolean join(ServerPlayerEntity target) {
        if(players.size() >= maxPeopleInGame) {
            return false;
        }
        players.add(target);
        return true;
    }

    public static boolean leave(ServerPlayerEntity target) {
        players.remove(target);
        return true;
    }

    public static ArrayList<ServerPlayerEntity> getPlayers() {
        return players;
    }

    public static String getName() {
        return nameGame;
    }

    public static double getStartTime() {
        return startTime;
    }
}
