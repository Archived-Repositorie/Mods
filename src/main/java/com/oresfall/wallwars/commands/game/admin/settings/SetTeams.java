package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.commands.suggestion.GameSuggestions;
import com.oresfall.wallwars.commands.suggestion.TeamSuggestions;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

public class SetTeams {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setteams")
                .then(
                        CommandManager.argument("game", StringArgumentType.word())
                                .then(
                                        CommandManager.argument("team", StringArgumentType.word())
                                                .then(
                                                        CommandManager.argument("coords", BlockPosArgumentType.blockPos())
                                                                .executes(SetTeams::run)
                                                ).suggests(new TeamSuggestions())
                                ).suggests(new GameSuggestions())
                );
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = Utils.getGameFromContext(context,"game");
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerPlayerEntity target = context.getSource().getPlayer();
        String string = StringArgumentType.getString(context,"team");
        TeamBase team = game.getTeam(string);
        if(team == null) {
            throw new SimpleCommandExceptionType(Text.literal("This team doesn't exist.")).create();
        }
        team.setSpawnPlace(blockPos);
        target.sendMessage(defaultMsg(String.format("Coords have been changed to X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        return 0;
    }
}
