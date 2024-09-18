package fr.black_eyes.lootchest.commands;

import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class LootchestCommand {

	public LootchestCommand() {}

	public static String getCardinalDirection(Player player) {
		float rotation = Utils.normalizeYaw(player.getLocation().getYaw());
		if (rotation > 135.0 || rotation <= -135.0) {
			return "NORTH";
		} else if (rotation > -135.0 && rotation < -45.0) {
			return "EAST";
		} else if (rotation >= -45.0 && rotation < 45.0) {
			return "SOUTH";
		} else if (rotation >= 45.0 && rotation <= 135.0) {
			return "WEST";
		}
		return null;
	}
}
