package com.oresfall.wallwars.utls;

import com.google.gson.Gson;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Utilities to make life easier
 */
public class Utils {
    private static final Gson gson = new Gson();
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
            if(serverWorld.getRegistryKey().getValue().toString().contains(worldName)) {
                return serverWorld;
            }
        }
        return null;
    }

    public static Text defaultMsg(@NotNull String text) {
        return Text.literal(text).formatted(Formatting.GREEN, Formatting.BOLD);
    }

    public static Text errorMsg(@NotNull String text) {
        return Text.literal(text).formatted(Formatting.RED, Formatting.BOLD);
    }

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

    public void writeJsonFile(String fileDir, String json) {
        try {
            PrintWriter writer = new PrintWriter(fileDir, StandardCharsets.UTF_8);
            writer.println(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
