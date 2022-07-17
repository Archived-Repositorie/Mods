package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.db.Database;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

/**
 * Command for setting lobby
 * Usage: `/game admin settings setlobbycoords {x} {y} {z}`
 */
public class SetLobbyCoords {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(Database.setLobby(blockPos.getX(), blockPos.getY(), blockPos.getZ()) == -1) {
            target.sendMessage(Text.empty().append("Those coords are already set").formatted(Formatting.RED));
            return -1;
        }
        target.sendMessage(Text.of(String.format("Coords have been changed to X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        Database.setLobby(blockPos.getX(),blockPos.getY(),blockPos.getZ());
        return 0;
    }
}
