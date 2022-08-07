package com.oresfall.wallwars.db;

import com.oresfall.wallwars.utls.Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;

/**
 * Class for saving games to json file
 */
class Config {

    /**
     * Class for json file templace
     */
    public static class GamesTemplate {
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

    public static class DefaultTeamTemplate {
        public String name = "";
        public String prefix = "";
        public Formatting color = Formatting.WHITE;
        public boolean pvp = false;
    }

    public static class PlayerData {
        public String ID = "";
    }

    public static class GlobalTemplate {
        public String world = "";
        public double[] coords = new double[3];
    }

    /**
     * List of Template
     */
    public GamesTemplate[] games;
    public GlobalTemplate global;
    public PlayerData[] playerData;

    private transient MinecraftServer server;

    public Config(MinecraftServer server) {
        this.server = server;
        gameConfig();
        globalConfig();
        playerData();
    }
    /**
     * Saves list of games
     */
    private void gameConfig() {
        var gamesList = Database.getGames();
        this.games = new GamesTemplate[gamesList.size()];
        for(int i = 0; i < gamesList.size(); i++) {
            games[i] = new GamesTemplate();
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

    private void globalConfig() {
        this.global = new GlobalTemplate();
        global.coords[0] = Database.getLobbyCoords().x;
        global.coords[1] = Database.getLobbyCoords().y;
        global.coords[2] = Database.getLobbyCoords().z;
        global.world = Database.getLobbyWorld(server).getRegistryKey().getValue().toString();
    }

    private void playerData() {
        this.playerData = new PlayerData[Database.getPlayersSize()+1];
        for(int i = 0; i < Database.getPlayersSize(); i++) {
            this.playerData[i] = new PlayerData();
            this.playerData[i].ID = Database.getPlayers().get(i).getPlayerEntity().getUuid().toString();
        }
        Utils.removeDuplicates(this.playerData);
    }
}
