package com.oresfall.wallwars.commands.game;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.commands.game.admin.CreateGame;
import com.oresfall.wallwars.commands.game.admin.GamesInfo;
import com.oresfall.wallwars.commands.game.admin.RemoveGame;
import com.oresfall.wallwars.commands.game.admin.settings.ChangeWorld;
import com.oresfall.wallwars.commands.game.admin.settings.SetSpawnCoords;
import com.oresfall.wallwars.commands.game.player.Join;
import com.oresfall.wallwars.commands.game.player.Leave;
import com.oresfall.wallwars.commands.game.player.Random;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Game {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("game")
                        .then(
                                CommandManager.literal("player")
                                        .then(
                                                CommandManager.literal("join")
                                                        .then(
                                                                CommandManager.argument("game", GameArgumentType.game())
                                                                        .executes(Join::run)
                                                        )
                                        ).then(
                                                CommandManager.literal("leave")
                                                        .executes(Leave::run)
                                        ).then(
                                                CommandManager.literal("random")
                                                        .executes(Random::run)
                                        )
                        ).then(
                                CommandManager.literal("admin")
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
                                        ).then(
                                                CommandManager.literal("settings")
                                                        .then(
                                                                CommandManager.literal("changeworld")
                                                                        .then(
                                                                                CommandManager.argument("game", GameArgumentType.game())
                                                                                        .then(
                                                                                                CommandManager.argument("world", DimensionArgumentType.dimension())
                                                                                                        .executes(ChangeWorld::run)
                                                                                        )
                                                                        )
                                                        ).then(
                                                                CommandManager.literal("setspawncoords")
                                                                        .then(
                                                                                CommandManager.argument("game", GameArgumentType.game())
                                                                                        .then(
                                                                                                CommandManager.argument("coords", BlockPosArgumentType.blockPos())
                                                                                                        .executes(SetSpawnCoords::run)
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
        );
    }
}

