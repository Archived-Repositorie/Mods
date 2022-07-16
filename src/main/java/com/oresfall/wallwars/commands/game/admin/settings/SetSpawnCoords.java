package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.db.Database;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class SetSpawnCoords {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context,"game");
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!Database.ifGameExist(game)) {
            target.sendMessage(Text.empty().append("Game doesn't exist!").formatted(Formatting.RED));
            return -1;
        }
        if(game.setSpawnCoords(blockPos.getX(), blockPos.getY(), blockPos.getZ()) == -1) {
            target.sendMessage(Text.empty().append("Those coords already exist").formatted(Formatting.RED));
            return -1;
        }

        target.sendMessage(Text.of(String.format("Coords have been changed to X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        return 0;
    }
}
