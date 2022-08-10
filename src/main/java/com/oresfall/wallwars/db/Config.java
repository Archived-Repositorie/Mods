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

        static class StartSpawn {
            public double[] place = new double[3];
            public String world = "";
        }
    }

    public static class DefaultTeamTemplate {
        public String name = "";
        public String prefix = "";
        public Formatting color = Formatting.WHITE;
        public boolean pvp = false;
    }

    public static class PlayerData {
        public String ID = "";
        public String name = "";
    }

    public static class GlobalTemplate {
        public static String world = "";
        public static double[] coords = new double[3];
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
            GamesTemplate.StartSpawn instance = new GamesTemplate.StartSpawn();
            instance.place = new double[]{
                    gamesList.get(i).getSPlace().x,
                    gamesList.get(i).getSPlace().y,
                    gamesList.get(i).getSPlace().z,
            };
        }
    }

    private void globalConfig() {
        GlobalTemplate.coords[0] = Database.getLobbyCoords().x;
        GlobalTemplate.coords[1] = Database.getLobbyCoords().y;
        GlobalTemplate.coords[2] = Database.getLobbyCoords().z;
        GlobalTemplate.world = Database.getLobbyWorld(server).getRegistryKey().getValue().toString();
    }

    private void playerData() {
        this.playerData = new PlayerData[Database.getPlayersSize()+1];
        for(int i = 0; i < Database.getPlayersSize(); i++) {
            this.playerData[i] = new PlayerData();
            this.playerData[i].ID = Database.getPlayers().get(i).getID().toString();
            this.playerData[i].name = Database.getPlayers().get(i).getName();
        }
        playerData = Utils.removeDuplicates(this.playerData);
    }
}
