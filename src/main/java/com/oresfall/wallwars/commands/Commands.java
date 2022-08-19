package com.oresfall.wallwars.commands;

import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.commands.argumenttype.MapArgumentType;
import com.oresfall.wallwars.commands.game.Game;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

import static com.oresfall.wallwars.Main.modid;

/**
 * Commands stuff
 */
public class Commands {
    /**
     * Register commands and commands like stuff
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register(Game::register);
        ArgumentTypeRegistry.registerArgumentType(new Identifier(modid,"game"), GameArgumentType.class,  ConstantArgumentSerializer.of(GameArgumentType::game));
        ArgumentTypeRegistry.registerArgumentType(new Identifier(modid,"map"), MapArgumentType.class,  ConstantArgumentSerializer.of(MapArgumentType::map));
    }
}
