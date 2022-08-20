package com.oresfall.wallwars.utls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.MapClass;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * Utilities to make life easier
 */
public class Utils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final int MIN = 60*20;
    public static final int SEC = 20;
    /**
     * Function to get world just by name
     *
     * @param server Server instance from where it will get worlds.
     * @param worldName Name of world (namespace:type)
     * @return Returns instance of ServerWords
     */
    @Nullable
    public static ServerWorld getWorldByName(@NotNull MinecraftServer server, String worldName) {
        for(ServerWorld serverWorld : server.getWorlds()) {
            if(serverWorld.getRegistryKey().getValue().toString().equals(worldName)) {
                return serverWorld;
            }
        }
        return null;
    }

    /**
     * Makes message green/bold
     * @param text Message
     * @return
     */
    public static Text defaultMsg(@NotNull String text) {
        return Text.literal(text).formatted(Formatting.GREEN, Formatting.BOLD);
    }
    /**
     * Makes message red/bold
     * @param text Message
     * @return
     */
    public static Text errorMsg(@NotNull String text) {
        return Text.literal(text).formatted(Formatting.RED, Formatting.BOLD);
    }

    /**
     * Reads file to json
     * @param fileDir File to read
     * @param object Template to generate json
     * @param <T>
     * @return
     */
    public <T> T readJsonFile(String fileDir, Class<T> object) {
        File file = new File(fileDir);
        StringBuilder data = new StringBuilder();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                data.append(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return gson.fromJson(data.toString(), object);
    }

    /**
     * Writes file with json
     * @param fileDir File to write
     */
    public void writeJsonFile(String fileDir, String json) {

        try {
            PrintWriter writer = new PrintWriter(fileDir, StandardCharsets.UTF_8);
            writer.println(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T[] removeDuplicates(T[] a) {

        LinkedHashSet<T> set
                = new LinkedHashSet<T>(Arrays.asList(a));
        return set.toArray(a);

    }

    public static @org.jetbrains.annotations.Nullable Clipboard readSchem(String fileName) {
        Main.LOGGER.info(fileName);
        String filePath = Main.schematics+fileName+".schem";
        File file = new File(filePath);
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if(format == null) return null;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return clipboard;
    }

    public static boolean ifSchemExist(String fileName) {
        String filePath = Main.schematics+fileName+".schem";
        File file = new File(filePath);
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        return format != null;
    }

    public static <S> Game getGameFromContext(CommandContext<S> context, String name) throws CommandSyntaxException {
        String gameString = StringArgumentType.getString(context,name);
        Game game = Database.getGameByName(gameString);
        if(game == null) {
            throw new SimpleCommandExceptionType(Text.literal("Game doesn't exist.")).create();
        }
        return game;
    }

    public static <S> MapClass getMapFromContext(CommandContext<S> context, String name) throws CommandSyntaxException {
        String mapString = StringArgumentType.getString(context,name);
        if(!Utils.ifSchemExist(mapString)) {
            throw new SimpleCommandExceptionType(Text.literal("Map doesn't exist.")).create();
        }
        return new MapClass(mapString);
    }
}
