package com.oresfall.wallwars.db;

import com.oresfall.wallwars.Game;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;

class SaveGame {

    public static class Base {
        public String name = "";
        public String world = "";
        public int[] spawnCoords = new int[3];
    }
    public Base[] games;
    public SaveGame(ArrayList<Game> gamesList) {
        this.games = new Base[gamesList.size()];
        for(int i = 0; i < gamesList.size(); i++) {
            games[i] = new Base();
            games[i].name = gamesList.get(i).toString();
            ServerWorld world = gamesList.get(i).getWorld();
            games[i].world = world.getRegistryKey().getValue().toString();
            games[i].spawnCoords = gamesList.get(i).getSpawnCoords();
        }
    }
}
