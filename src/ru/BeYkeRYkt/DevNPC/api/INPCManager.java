package ru.BeYkeRYkt.DevNPC.api;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import ru.BeYkeRYkt.DevNPC.api.characters.ICharacterManager;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;
import ru.BeYkeRYkt.DevNPC.api.utils.INMSRegistry;

public interface INPCManager {

	public INPC spawnNPC(String entityTypeName, Location location);

	public INPC spawnNPC(int entityTypeId, Location location);

	public List<INPC> getNPCs();

	public List<INPC> getNPCs(World world);

	public List<INPC> getNPCs(Chunk chunk);

	public INPC getNPC(Entity entity);

	public boolean isNPC(Entity entity);

	public void despawnAll();

	public ICharacterManager getCharacterManager();

	public INMSRegistry getNMSRegistry();

	public Plugin getPlugin();
}
