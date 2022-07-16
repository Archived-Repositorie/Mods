package com.oresfall.wallwars.commands.argumenttype;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.db.Database;
import net.minecraft.text.Text;

public class GameArgumentType implements ArgumentType<Game> {
    public static GameArgumentType game() {
        return new GameArgumentType();
    }

    public static <S> Game getGame(CommandContext<S> context,String name) {
        return context.getArgument(name, Game.class);
    }


    @Override
    public Game parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String gameString = reader.getString().substring(argBeginning, reader.getCursor());

        Game game = Database.getGameByName(gameString);
        if(game == null) {
            throw new SimpleCommandExceptionType(Text.literal("Game doesn't exist.")).createWithContext(reader);
        }
        return game;
    }
}
