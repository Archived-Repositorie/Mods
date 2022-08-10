package com.oresfall.wallwars.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import com.oresfall.wallwars.playerclass.Player;
import com.oresfall.wallwars.utls.Utils;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

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

    private static TeamBase defaultTeam;

    private static TeamBase spectatorTeam;
    private static ArrayList<Player> players = new ArrayList<>();

    private static Config config;

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static Config getConfig() {
        return config;
    }


    /**
     * @return Lobby coordinates
     */
    public static Vec3d getLobbyCoords() {
        return lobbyCoords;
    }
    public static ServerWorld getLobbyWorld(MinecraftServer server) {
        if(lobbyWorld == null) return server.getOverworld();
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

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static @Nullable Player getPlayer(PlayerEntity playerEntity) {
        if(players.size() <= 0) return null;
        for(Player player : players) {
            if(player.getPlayerEntity() == playerEntity) {
                return player;
            }
        }
        return null;
    }

    public static @Nullable Player getPlayer(String name) {
        if(players.size() <= 0) return null;
        for(Player player : players) {
            if(Objects.equals(player.getName(), name)) {
                return player;
            }
        }
        return null;
    }

    public static @Nullable Player getPlayer(UUID ID) {
        if(players.size() <= 0) return null;
        for(Player player : players) {
            if(Objects.equals(player.getID(), ID)) {
                return player;
            }
        }
        return null;
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
        if(coords.equals(lobbyCoords)) return false;
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
    public static void saveGames(MinecraftServer server) {
        Utils util = new Utils();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new Config(server));
        util.writeJsonFile(Main.configFile, json);
    }

    /**
     * Reads games from json file and adds them to database
     * @param server Game server
     */
    public static void readConfigs(MinecraftServer server) {
        Utils util = new Utils();
        setLobby(server.getOverworld(), 0, 60, 0);
        Config configData = util.readJsonFile(Main.configFile, Config.class);
        if(Objects.equals(configData.global, new Config.GlobalTemplate())) {
            setLobby(server.getOverworld(), 0,60,0);
        } else {
            Main.LOGGER.info(configData.global.world);
            setLobby(Utils.getWorldByName(server,configData.global.world),
                    configData.global.coords[0],
                    configData.global.coords[1],
                    configData.global.coords[2]
            );
        }
        if(configData.games == null) return;
        for(Config.GamesTemplate gameData : configData.games) {
            if(getGameByName(gameData.name) != null) continue;
            Game game = new Game(gameData.name, server);
            game.setWorld(gameData.world);
            Config.GamesTemplate.StartSpawn instance = new Config.GamesTemplate.StartSpawn();
            game.setSpawnStart(
                    Utils.getWorldByName(server,gameData.world),
                    instance.place[1],
                    instance.place[1],
                    instance.place[2]
            );
            Database.addGame(game);
        }
        if(configData.playerData == null) return;
        for(Config.PlayerData playerData : configData.playerData) {
            if(
                    playerData != null
                    &&getPlayer(playerData.name) != null
                    &&getPlayer(UUID.fromString(playerData.ID)) != null
            ) continue;
            if(playerData == null) continue;
            Player player = new Player(playerData.name, UUID.fromString(playerData.ID));
            Database.addPlayer(player);
        }
        config = configData;

    }

    public static void tpPlayerToLobby(Player player, MinecraftServer server) {
        if(lobbyWorld == null) lobbyWorld = server.getOverworld();
        FabricDimensions.teleport(
                player.getPlayerEntity(),
                lobbyWorld,
                new TeleportTarget(lobbyCoords, player.getPlayerEntity().getVelocity(), player.getPlayerEntity().getYaw(), player.getPlayerEntity().prevPitch)
        );
    }

    private static int time;

    public static void onEveryTick(MinecraftServer server) {
        for(Game game : games) {
            game.onTick(server);
        }
    }

    public static void setDefaultTeam(MinecraftServer server, String teamName) {
        defaultTeam = new TeamBase(server,teamName);
        defaultTeam.setColor(Formatting.WHITE);
        defaultTeam.enablePvp(false);
        defaultTeam.setPrefix(Text.literal("PLAYER ").formatted(Formatting.BOLD));
    }

    public static void setSpectatorTeam(MinecraftServer server, String teamName) {
        spectatorTeam = new TeamBase(server,teamName);
        spectatorTeam.setColor(Formatting.GRAY);
        spectatorTeam.enablePvp(false);
        spectatorTeam.setPrefix(Text.literal("DEAD ").formatted(Formatting.BOLD));
    }

    public static void addPlayerToDefaultTeam(Player player) {
        defaultTeam.addPlayer(player);
    }

    public static void addSpecPlayer(Player player) {
        spectatorTeam.addPlayer(player);
    }

    public static int getPlayersSize() {
        if(players == null) return 0;
        return players.size();
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }
    public static TeamBase getDefaultTeam() {
        return defaultTeam;
    }
    public static void removePlayerToDefaultTeam(Player player) {
        defaultTeam.removePlayer(player);
    }
}
