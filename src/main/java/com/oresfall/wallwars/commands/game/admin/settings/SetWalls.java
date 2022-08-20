package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.commands.suggestion.GameSuggestions;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

public class SetWalls {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setwalls")
                .then(
                        CommandManager.argument("game", StringArgumentType.word()).suggests(new GameSuggestions()).then(
                                CommandManager.argument("minblockpos0", BlockPosArgumentType.blockPos())
                                        .then(
                                                CommandManager.argument("maxblockpos0", BlockPosArgumentType.blockPos())
                                                        .then(
                                                                CommandManager.argument("minblockpos1", BlockPosArgumentType.blockPos())
                                                                        .then(
                                                                                CommandManager.argument("maxblockpos1", BlockPosArgumentType.blockPos())
                                                                                        .executes(SetWalls::run)
                                                                        )
                                                        )
                                        )
                        )

                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos minBlockPos0 = BlockPosArgumentType.getBlockPos(context, "minblockpos0");
        BlockPos maxBlockPos0 = BlockPosArgumentType.getBlockPos(context, "maxblockpos0");
        BlockPos minBlockPos1 = BlockPosArgumentType.getBlockPos(context, "minblockpos1");
        BlockPos maxBlockPos1 = BlockPosArgumentType.getBlockPos(context, "maxblockpos1");
        Game game = Utils.getGameFromContext(context,"game");
        ServerPlayerEntity target = context.getSource().getPlayer();

        game.setWalls(minBlockPos0,maxBlockPos0,minBlockPos1,maxBlockPos1);

        target.sendMessage(defaultMsg(String.format(
                "Wall 0 minX:%d minY:%d minZ:%d maxX:%d maxY:%d maxZ:%d",
                minBlockPos0.getX(),minBlockPos0.getY(),minBlockPos0.getZ(),
                maxBlockPos0.getX(),maxBlockPos0.getY(),maxBlockPos0.getZ()
                )));
        target.sendMessage(defaultMsg(String.format(
                "Wall 1 minX:%d minY:%d minZ:%d maxX:%d maxY:%d maxZ:%d",
                minBlockPos1.getX(),minBlockPos1.getY(),minBlockPos1.getZ(),
                maxBlockPos1.getX(),maxBlockPos1.getY(),maxBlockPos1.getZ()
        )));
        return 0;
    }
}