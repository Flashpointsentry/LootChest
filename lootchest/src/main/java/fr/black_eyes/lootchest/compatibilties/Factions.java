package fr.black_eyes.lootchest.compatibilties;

import org.bukkit.Location;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;

public class Factions {
	public static boolean isInClaim(Location loc) {
		try {
			Faction f = com.massivecraft.factions.entity.BoardColl.get().getFactionAt(PS.valueOf(loc));
		    return (!f.equals(FactionColl.get().getNone()));
		}catch(NoClassDefFoundError ignored){
			
		}
		return false;
	}

	private Factions() {
		throw new IllegalStateException("Utility class");
	}
}
