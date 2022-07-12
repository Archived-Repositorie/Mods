package com.oresfall.commands.game;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.context.CommandContext;

public class Game {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("game")
                        .then(
                                CommandManager.argument("game", StringArgumentType.word())
                                        .executes(TestGame::run)
                                        .then(
                                                CommandManager.literal("join")
                                                        .then(
                                                                CommandManager.argument("target",EntityArgumentType.player())
                                                                        .executes(Join::run)
                                                        )
                                        ).then(
                                                CommandManager.literal("leave")
                                                        .then(
                                                                CommandManager.argument("target",EntityArgumentType.player())
                                                                        .executes(Leave::run)
                                                        )
                                        )
                        )
        );
    }
}

