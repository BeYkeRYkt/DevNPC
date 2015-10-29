package ru.BeYkeRYkt.DevNPC.api.utils;

import org.bukkit.World;

import ru.BeYkeRYkt.DevNPC.api.entity.INMSCustomEntity;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;

public interface INMSRegistry {

	public void registerEntity(Class<? extends INMSCustomEntity> custom, String name, int id);

	public void unregisterEntity(String name, int entityId);

	public INPC createEntityFromName(String name, World world);

	public INPC createEntityFromId(int id, World world);
}
