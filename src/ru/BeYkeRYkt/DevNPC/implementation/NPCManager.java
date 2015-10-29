package ru.BeYkeRYkt.DevNPC.implementation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import ru.BeYkeRYkt.DevNPC.api.INPCManager;
import ru.BeYkeRYkt.DevNPC.api.characters.ICharacterManager;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;
import ru.BeYkeRYkt.DevNPC.api.utils.INMSRegistry;
import ru.BeYkeRYkt.DevNPC.implementation.characters.NPCCharacterManager;
import ru.BeYkeRYkt.DevNPC.implementation.utils.NMSRegistry;

public class NPCManager implements INPCManager {

	private Plugin plugin;
	private ICharacterManager manager;
	private INMSRegistry registry;

	public NPCManager(Plugin plugin) {
		this.plugin = plugin;
		this.manager = new NPCCharacterManager();
		this.registry = new NMSRegistry();
	}

	@Override
	public INPC spawnNPC(String entityTypeName, Location location) {
		INPC npc = null;
		World world = location.getWorld();
		npc = getNMSRegistry().createEntityFromName(entityTypeName, world);
		((Entity) npc).teleport(location);
		return npc;
	}

	@Override
	public INPC spawnNPC(int entityTypeId, Location location) {
		INPC npc = null;
		World world = location.getWorld();
		npc = getNMSRegistry().createEntityFromId(entityTypeId, world);
		((Entity) npc).teleport(location);
		return npc;
	}

	@Override
	public List<INPC> getNPCs() {
		List<INPC> list = new ArrayList<INPC>();
		for (World world : Bukkit.getWorlds()) {
			list.addAll(getNPCs(world));
		}
		return list;
	}

	@Override
	public List<INPC> getNPCs(World world) {
		List<INPC> list = new ArrayList<INPC>();
		for (Entity entity : world.getEntities()) {
			if (isNPC(entity)) {
				list.add((INPC) entity);
			}
		}
		return list;
	}

	@Override
	public List<INPC> getNPCs(Chunk chunk) {
		List<INPC> list = new ArrayList<INPC>();
		for (Entity entity : chunk.getEntities()) {
			if (isNPC(entity)) {
				list.add((INPC) entity);
			}
		}
		return list;
	}

	@Override
	public INPC getNPC(Entity entity) {
		if (!isNPC(entity)) {
			return null;
		}
		return (INPC) entity;
	}

	@Override
	public boolean isNPC(Entity entity) {
		if (INPC.class.isAssignableFrom(entity.getClass())) {
			return true;
		}
		return false;
	}

	@Override
	public void despawnAll() {
		for (INPC npc : getNPCs()) {
			((Entity) npc).remove();
		}
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public ICharacterManager getCharacterManager() {
		return manager;
	}

	@Override
	public INMSRegistry getNMSRegistry() {
		return registry;
	}
}
