package fr.cylian91.autologin;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;


public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("autologin");
	@Override
	public void onInitialize() {

		/*ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {

			dispatcher.register(
					ClientCommandManager.literal("autologin")
							.then(ClientCommandManager.argument("Mot de passe", greedyString())
							.executes(ctx -> {
								try {
									Configuration.writeConfig("MDP",ctx.getArgument("Mot de passe", String.class));
								} catch (IOException e) {
									throw new RuntimeException(e);
								}


								return 0;
							})

							));
		}));*/

		ClientSendMessageEvents.COMMAND.register((command -> {
			if (command.startsWith("login ")){
				String pass = command.toString().substring(6);
				try {
					Configuration.writeConfig("MDP", pass);
				} catch (IOException e) {
					throw new RuntimeException(e);
				};
			}
			if (command.startsWith("l ")){
				String pass = command.toString().substring(2);
				try {
					Configuration.writeConfig("MDP", pass);
				} catch (IOException e) {
					throw new RuntimeException(e);
				};
			}
		}));
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			try {
				if (message.getString().startsWith("Use the command /login <password>.") && Configuration.getConfig("MDP")!=null) {
					LOGGER.info(message.getString());

						MinecraftClient.getInstance().player.networkHandler.sendCommand("login MDP"
								.replace("MDP",Configuration.getConfig("MDP").get(0)));

				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}



}