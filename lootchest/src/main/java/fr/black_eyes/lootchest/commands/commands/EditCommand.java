package fr.black_eyes.lootchest.commands.commands;

import java.util.Collections;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.commands.ArgType;
import fr.black_eyes.lootchest.commands.SubCommand;
import fr.black_eyes.lootchest.ui.UiHandler;

public class EditCommand extends SubCommand {
	
	private final UiHandler uiHandler;

	public EditCommand() {
		super("edit", Collections.singletonList(ArgType.LOOTCHEST));
		setPlayerRequired(true);
		this.uiHandler = Main.getInstance().getUiHandler();
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String chestName = args[1];
		uiHandler.openUi(player, UiHandler.UiType.MAIN, Main.getInstance().getLootChest().get(chestName));
	}
}
