package com.oresfall.wallwars.db;

import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
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

        public double[] mapplace_place = new double[3];
        public double[] wait_place = new double[3];
        public String wait_world = "";

        public double[][][] walls = new double[2][2][3];

        public double[][] teams_place = new double[4][3];

        public String map = "";
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
            Game gameBase = gamesList.get(i);

            games[i].name = gameBase.toString();
            ServerWorld world = gameBase.getWorld();
            games[i].world = world.getRegistryKey().getValue().toString();
            games[i].mapplace_place = new double[]{
                    gameBase.getSPlace().x,
                    gameBase.getSPlace().y,
                    gameBase.getSPlace().z,
            };
            games[i].wait_place = new double[]{
                    gameBase.getWaitPlace().x,
                    gameBase.getWaitPlace().y,
                    gameBase.getWaitPlace().z
            };
            ServerWorld waitWorld = gameBase.getWaitWorld() ;
            games[i].wait_world = waitWorld.getRegistryKey().getValue().toString();
            games[i].map = gamesList.get(i).getMapFile();
            for(int j = 0; j < gameBase.getTeams().size(); j++) {
                TeamBase teamBase = gameBase.getTeams().get(j);
                games[i].teams_place[j] = teamBase.getTpPlace();
            }
            for(int j = 0; j < gameBase.getWalls().length; j++) {
                for(int h = 0; h < gameBase.getWalls()[j].length; h++) {
                    if(gameBase.getWalls()[j][h] == null) continue;
                    games[i].walls[j][h] = new double[] {
                            gameBase.getWalls()[j][h].getX(),
                            gameBase.getWalls()[j][h].getY(),
                            gameBase.getWalls()[j][h].getZ()
                    };
                }
            }
        }
    }

    private void globalConfig() {
        this.global = new GlobalTemplate();
        this.global.coords[0] = Database.getLobbyCoords().x;
        this.global.coords[1] = Database.getLobbyCoords().y;
        this.global.coords[2] = Database.getLobbyCoords().z;
        this.global.world = Database.getLobbyWorld(server).getRegistryKey().getValue().toString();
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
