package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.commands.game.admin.settings.Settings;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Admin {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("admin")
                .requires(src -> src.hasPermissionLevel(3))
                .then(
                        CommandManager.literal("creategame")
                                .then(
                                        CommandManager.argument("name", StringArgumentType.word())
                                                .then(
                                                        CommandManager.argument("world", DimensionArgumentType.dimension())
                                                                .executes(CreateGame::run)
                                                )
                                )
                ).then(
                        CommandManager.literal("removegame")
                                .then(
                                        CommandManager.argument("game", GameArgumentType.game())
                                                .executes(RemoveGame::run)
                                )
                ).then(
                        CommandManager.literal("gamesinfo")
                                .executes(GamesInfo::run)
                ).then(Settings.register());
    }
}
