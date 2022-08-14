package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.commands.argumenttype.GameTeamArgumentType;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SetTeams {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setteams")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .then(
                                        CommandManager.argument("team", GameTeamArgumentType.gameTeam())
                                                .then(
                                                        CommandManager.argument("coords", BlockPosArgumentType.blockPos())
                                                                .executes(SetLobby::run)
                                                )
                                )
                );
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context, "game");
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerPlayerEntity target = context.getSource().getPlayer();
        TeamBase teamBase = GameTeamArgumentType.getGameTeam(context, "team");
        teamBase.setSpawnPlace(blockPos);
        return 0;
    }
}
