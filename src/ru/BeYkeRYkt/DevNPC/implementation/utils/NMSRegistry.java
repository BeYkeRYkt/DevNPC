package ru.BeYkeRYkt.DevNPC.implementation.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.WorldServer;
import ru.BeYkeRYkt.DevNPC.DevNPC;
import ru.BeYkeRYkt.DevNPC.api.entity.INMSCustomEntity;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;
import ru.BeYkeRYkt.DevNPC.api.utils.INMSRegistry;

public class NMSRegistry implements INMSRegistry {

	private Method method;

	// Register entity without Entity Egg
	@Override
	public void registerEntity(Class<? extends INMSCustomEntity> custom, String name, int id) {
		try {
			addToMaps(custom, name, id);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void addToMaps(Class<? extends INMSCustomEntity> clazz, String name, int id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//if (clazz.isAssignableFrom(Entity.class)) {
		if(Entity.class.isAssignableFrom(clazz)){
			Class<? extends Entity> entity = (Class<? extends Entity>) clazz;
			// ((Map<String, Class<? extends Entity>>) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, entity);
			// ((Map<Class<? extends Entity>, String>) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(entity, name);
			// ((Map<Integer, Class<? extends Entity>>) getPrivateField("e", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(Integer.valueOf(id), entity);
			// ((Map<Class<? extends Entity>, Integer>) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(entity, Integer.valueOf(id));
			// ((Map<String, Integer>) getPrivateField("g", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, Integer.valueOf(id));
			getNonEggMethod().invoke(EntityTypes.class, entity, name, id);
			DevNPC.getNPCManager().getPlugin().getLogger().info("Added new entity " + name + ":" + id);
		}
	}

	// Unregister entity
	@Override
	public void unregisterEntity(String name, int entityId) {
		Class<? extends Entity> clazz = EntityTypes.a(entityId);
		if (clazz != null) {
			removeToMaps(clazz, name, entityId);
		}
	}

	@SuppressWarnings("unchecked")
	private void removeToMaps(Class<? extends Entity> clazz, String name, int id) {
		((Map<String, Class<? extends Entity>>) getPrivateField("c", EntityTypes.class, null)).remove(name);
		((Map<Class<? extends Entity>, String>) getPrivateField("d", EntityTypes.class, null)).remove(clazz);
		((Map<Integer, Class<? extends Entity>>) getPrivateField("e", EntityTypes.class, null)).remove(Integer.valueOf(id));
		((Map<Class<? extends Entity>, Integer>) getPrivateField("f", EntityTypes.class, null)).remove(clazz);
		((Map<String, Integer>) getPrivateField("g", EntityTypes.class, null)).remove(name);
		if (EntityTypes.eggInfo.containsKey(Integer.valueOf(id))) {
			EntityTypes.eggInfo.remove(Integer.valueOf(id));
		}
		DevNPC.getNPCManager().getPlugin().getLogger().info("Removed entity " + name + ":" + id);
	}
	
	@Override
	public INPC createEntityFromName(String name, World world) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		Entity entity = EntityTypes.createEntityByName(name, nms);
		nms.addEntity(entity);
		entity.ah();
		
		//if(entity.getClass().isAssignableFrom(INMSCustomEntity.class)){
		if(INMSCustomEntity.class.isAssignableFrom(entity.getClass())){
			return ((INMSCustomEntity) entity).getBukkitNPC();
		}
		return null;
	}

	@Override
	public INPC createEntityFromId(int id, World world) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		Entity entity = EntityTypes.a(id, nms);
		nms.addEntity(entity);
		entity.ah();
		
		//if(entity.getClass().isAssignableFrom(INMSCustomEntity.class)){
		if(INMSCustomEntity.class.isAssignableFrom(entity.getClass())){
			return ((INMSCustomEntity) entity).getBukkitNPC();
		}
		return null;
	}

	private Method getNonEggMethod() throws NoSuchMethodException, SecurityException {
		if (method == null) {
			method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			method.setAccessible(true);
		}
		return method;
	}

	// Field
	private static Object getPrivateField(String fieldName, Class<EntityTypes> clazz, Object object) {
		Field field;
		Object o = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			o = field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	// Others...
	@Override
	public int getColor(int red, int green, int blue) {
		int color_int = (red << 16) + (green << 8) + blue;
		return color_int;
	}
}
