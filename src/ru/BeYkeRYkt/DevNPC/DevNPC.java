package ru.BeYkeRYkt.DevNPC;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import ru.BeYkeRYkt.DevNPC.api.INPCManager;
import ru.BeYkeRYkt.DevNPC.implementation.NPCManager;
import ru.BeYkeRYkt.DevNPC.implementation.entity.nms.EntityHumanNPC;
import ru.BeYkeRYkt.DevNPC.implementation.utils.NMSHelper;
import ru.BeYkeRYkt.NPCTest.NPCAttacker;
import ru.BeYkeRYkt.NPCTest.NPCGuardian;
import ru.BeYkeRYkt.NPCTest.NPCListener;

public class DevNPC extends JavaPlugin {

	private static INPCManager npcManager;

	@Override
	public void onLoad() {
		DevNPC.npcManager = new NPCManager(this);
		getNPCManager().getNMSRegistry().registerEntity(EntityHumanNPC.class, "Human", 999);
		getNPCManager().getCharacterManager().registerCharacter(new NPCAttacker());
		getNPCManager().getCharacterManager().registerCharacter(new NPCGuardian());
	}

	@Override
	public void onEnable() {
		for (World world : getServer().getWorlds()) {
			NMSHelper.registerWorldAccess(world);
		}

		getServer().getPluginManager().registerEvents(new NPCListener(), this);
	}

	public static INPCManager getNPCManager() {
		return npcManager;
	}
}
