package fr.black_eyes.lootchest.commands;

import fr.black_eyes.lootchest.Constants;
import fr.black_eyes.lootchest.Lootchest;
import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.Utils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RandomSpawnCommand extends SubCommand {
	
	public RandomSpawnCommand() {
		super("randomspawn", Arrays.asList(ArgType.LOOTCHEST, ArgType.INTEGER));
	}
	
	@Override
	public String getUsage() {
		return "/lc randomspawn <chestname> <radius>";
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		String chestName = args[1];
		Lootchest lc = Main.getInstance().getLootChest().get(chestName);
		int newradius = Integer.parseInt(args[2]);
		newradius = Math.max(newradius, 0);
		lc.setRadius(newradius);
		if (newradius > 0) {
			Utils.msg(sender, "chestRadiusSet", Constants.cheststr, chestName);
		}
		
		lc.despawn();
		if (newradius == 0) {
			lc.setRandomLoc(null);
			Utils.msg(sender, "disabledChestRadius", Constants.cheststr, chestName);
		}
		lc.spawn(true);
		lc.updateData();
	}
}
