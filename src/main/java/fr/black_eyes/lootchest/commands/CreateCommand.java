package fr.black_eyes.lootchest.commands;

import fr.black_eyes.lootchest.Constants;
import fr.black_eyes.lootchest.Lootchest;
import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.Mat;
import fr.black_eyes.lootchest.Utils;
import fr.black_eyes.lootchest.ui.UiHandler;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.BlockIterator;

public class CreateCommand extends SubCommand {
	
	private final UiHandler uiHandler;
	public CreateCommand(UiHandler uiHandler) {
		super("create", Arrays.asList(ArgType.STRING));
		setPlayerRequired(true);

		this.uiHandler = uiHandler;
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Block chest;
		String chestName = args[1];
		
		BlockIterator iter = new BlockIterator(player, 10);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (lastBlock.getType() == Material.AIR) {
				continue;
			}
			break;
		}
		chest = lastBlock;
		if (!Mat.isALootChestBlock(chest)) {
			Utils.msg(sender, "notAChest", " ", " ");
		} else if (Utils.isEmpty(((InventoryHolder) chest.getState()).getInventory())) {
			Utils.msg(sender, "chestIsEmpy", " ", " ");
		} else if (Main.getInstance().getLootChest().containsKey(chestName)) {
			Utils.msg(sender, "chestAlreadyExist", Constants.cheststr, chestName);
		} else if (Utils.isLootChest(chest.getLocation()) != null) {
			Utils.msg(sender, "blockIsAlreadyLootchest", Constants.cheststr, chestName);
		} else {
			Lootchest newChest = new Lootchest(chest, chestName);
			Main.getInstance().getLootChest().put(chestName, newChest);
			newChest.spawn(true);
			newChest.updateData();
			Utils.msg(sender, "chestSuccefulySaved", Constants.cheststr, chestName);
			uiHandler.openUi(player, UiHandler.UiType.MAIN, newChest);
		}
	}
}
