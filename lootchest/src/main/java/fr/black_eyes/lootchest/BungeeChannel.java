package fr.black_eyes.lootchest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import fr.black_eyes.lootchest.googleThings.ByteArrayDataOutput;
import fr.black_eyes.lootchest.googleThings.ByteStreams;
import org.jetbrains.annotations.NotNull;


/**
 * @author Black_Eyes
 * Broadcasts lootchest messages through bungeecord. Should works on 1.7+
 */
public class BungeeChannel implements PluginMessageListener {
	

	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {

    }

	

	/**
	 * @param args The message to send
	 * @param p The player that should send the message. Selects a random player to send it. BungeeCord needs a player to broadcast a message.
	 */
	public static void sendPluginMsg(String[] args, Player p) {

			if(p == null) {
				p = Bukkit.getOnlinePlayers().iterator().next();
				if(p==null) {
					return;
				}
			}
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			for(String arg : args) {
				out.writeUTF(arg);
			}
			p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
		
	}
	
	
	/**
	 * @param message The message to broadcast
	 */
	public static void bungeeBroadcast(String message) {
		sendPluginMsg(new String[]{"Message", "ALL", message}, null);
	}
	
	

}
