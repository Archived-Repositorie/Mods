package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.db.Database;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Random {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)target;

        if(targetData.getPersistentData().getBoolean("JoinedGame")) {
            target.sendMessage(Text.empty().append("You are already in game!").formatted(Formatting.RED));
            return -1;
        }

        Game game = Database.getGames().get((int)(Math.random() * Database.getGames().size()));
        Main.LOGGER.info(String.valueOf((Math.random() * Database.getGames().size())));

        if(game.joinPlayer(target) == -1) {
            target.sendMessage(Text.empty().append("There is too many people in game").formatted(Formatting.RED));
            return -1;
        }

        target.sendMessage(Text.of("Joined the game!"));
        targetData.getPersistentData().putBoolean("JoinedGame",true);
        return 0;
    }
}
