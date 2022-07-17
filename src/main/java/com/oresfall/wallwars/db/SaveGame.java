package com.oresfall.wallwars.db;

import com.oresfall.wallwars.Game;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class for saving games to json file
 */
class SaveGame {

    /**
     * Class for json file templace
     */
    public static class Template {
        /**
         * For "name" key in json
         */
        public String name = "";
        /**
         * For "world" key in json
         */
        public String world = "";
        /**
         * For "spawnCoords" key in json
         * 3 items big array
         */
        public double[] spawnCoords = new double[3];
    }

    /**
     * List of Template
     */
    public Template[] games;

    /**
     * Saves list of games
     * @param gamesList List of games to be saved
     */
    public SaveGame(@NotNull ArrayList<Game> gamesList) {
        this.games = new Template[gamesList.size()];
        for(int i = 0; i < gamesList.size(); i++) {
            games[i] = new Template();
            games[i].name = gamesList.get(i).toString();
            ServerWorld world = gamesList.get(i).getWorld();
            games[i].world = world.getRegistryKey().getValue().toString();
            games[i].spawnCoords = new double[]{
                    gamesList.get(i).getSpawnCoords().x,
                    gamesList.get(i).getSpawnCoords().y,
                    gamesList.get(i).getSpawnCoords().z,
            };
        }
    }
}
