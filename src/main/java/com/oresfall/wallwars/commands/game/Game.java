package com.oresfall.wallwars.commands.game;

import com.mojang.brigadier.CommandDispatcher;
import com.oresfall.wallwars.commands.game.admin.Admin;
import com.oresfall.wallwars.commands.game.player.Player;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

/**
 * Class for game command
 */
public class Game {
    /**
     * Register all commands
     * @param dispatcher
     * @param commandRegistryAccess
     * @param registrationEnvironment
     */
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("game")
                        .then(Player.register())
                        .then(Admin.register())
        );
    }
}

