package com.oresfall.wallwars.commands.argumenttype;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.gameclass.MapClass;
import com.oresfall.wallwars.utls.Utils;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import net.minecraft.text.Text;

public class MapArgumentType implements ArgumentType<MapClass> {
    public static MapArgumentType map() {
        return new MapArgumentType();
    }

    public static <S> MapClass getMap(CommandContext<S> context, String name) {
        return context.getArgument(name, MapClass.class);
    }


    @Override
    public MapClass parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String mapString = reader.getString().substring(argBeginning, reader.getCursor());

        Clipboard map =  Utils.readSchem(mapString);
        if(map == null) {
            throw new SimpleCommandExceptionType(Text.literal("Map doesn't exist.")).createWithContext(reader);
        }
        return new MapClass(mapString,map);
    }
}
