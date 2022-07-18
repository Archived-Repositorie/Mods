package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.db.Database;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

/**
 * Command for setting lobby
 * Usage: `/game admin settings setlobby {world} {x} {y} {z}`
 */
public class SetLobby {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setlobby")
                .then(
                        CommandManager.argument("world", DimensionArgumentType.dimension()).then(
                                CommandManager.argument("coords", BlockPosArgumentType.blockPos())
                                        .executes(SetLobby::run)
                        )

                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(context, "world");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!Database.setLobby(world, blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
            throw new SimpleCommandExceptionType(Text.literal("Values are already set.")).create();
        }

        target.sendMessage(defaultMsg(String.format("Coords have been changed to X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        return 0;
    }
}
