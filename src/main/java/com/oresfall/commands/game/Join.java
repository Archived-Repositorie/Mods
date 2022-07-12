package com.oresfall.commands.game;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.IEntityDataSaver;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Join {
    static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context,"target");

        if(target == null) {
            context.getSource().sendError(Text.of("This player doesn't exist."));
            return -1;
        }
        IEntityDataSaver targetData = (IEntityDataSaver)target;
        if(targetData.getPersistentData().getBoolean("JoinedGame")) {
            target.sendMessage(Text.of("You are already in game!"));
            return -1;
        }
        target.sendMessage(Text.of("Joined the game!"));
        context.getSource().sendFeedback(Text.of(target.getName().getString()),true);
        targetData.getPersistentData().putBoolean("JoinedGame",true);
        return 0;
    }
}
