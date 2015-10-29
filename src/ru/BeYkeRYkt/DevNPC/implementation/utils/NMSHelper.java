package ru.BeYkeRYkt.DevNPC.implementation.utils;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.WorldServer;
import ru.BeYkeRYkt.DevNPC.api.Animations;
import ru.BeYkeRYkt.DevNPC.api.Animations.PacketType;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;

public class NMSHelper {

	private static Random random = new Random();

	public static void registerWorldAccess(World world) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		nmsWorld.addIWorldAccess(new DevWorldAccess());
	}

	public static WorldServer getWorld(Entity entity) {
		return ((WorldServer) entity.world);
	}

	public static void playAnimation(Entity entity, PacketType type, int animationId, int particalCount) {
		if (type == PacketType.ANIMATION) {
			getWorld(entity).getTracker().a(entity, new PacketPlayOutAnimation(entity, animationId));
		} else if (type == PacketType.ENTITY_STATUS) {
			getWorld(entity).getTracker().a(entity, new PacketPlayOutEntityStatus(entity, (byte) animationId));
		} else if (type == PacketType.NONE) {
			if (animationId == Animations.DRINK.getId()) {
				makeSound(entity, "random.drink", 0.5F, random.nextFloat() * 0.1F + 0.9F);
			} else if (animationId == Animations.EAT_FOOD.getId()) {
				for (int j = 0; j < particalCount; ++j) {
					Vec3D vec3d = new Vec3D((random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

					vec3d = vec3d.a(-entity.pitch * 3.141593F / 180.0F);
					vec3d = vec3d.b(-entity.yaw * 3.141593F / 180.0F);
					double d0 = -random.nextFloat() * 0.6D - 0.3D;
					Vec3D vec3d1 = new Vec3D((random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

					vec3d1 = vec3d1.a(-entity.pitch * 3.141593F / 180.0F);
					vec3d1 = vec3d1.b(-entity.yaw * 3.141593F / 180.0F);
					vec3d1 = vec3d1.add(entity.locX, entity.locY + entity.getHeadHeight(), entity.locZ);
					ItemStack itemstack = entity.getEquipment()[0];
					if (itemstack.usesData())
						getWorld(entity).addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, new int[] { Item.getId(itemstack.getItem()), itemstack.getData() });
					else {
						getWorld(entity).addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, new int[] { Item.getId(itemstack.getItem()) });
					}
				}

				makeSound(entity, "random.eat", 0.5F + 0.5F * random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	public static void playAnimation(Entity entity, Animations id) {
		// getWorld(entity).getTracker().a(entity, new PacketPlayOutAnimation(entity, id.getId()));
		playAnimation(entity, id.getType(), id.getId(), 10);
	}

	public static void playAnimation(Entity entity, Animations anim, int particalCount) {
		playAnimation(entity, anim);
	}

	public static void makeSound(Entity entity, String name, float f, float f1) {
		getWorld(entity).makeSound(entity, name, f, f1);
	}

	@SuppressWarnings("unchecked")
	public static EntityTrackerEntry registerEntityTracker(Entity entity, int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
		WorldServer worldServer = getWorld(entity);
		CustomEntityTrackerEntry tracker = new CustomEntityTrackerEntry(entity, trackingRange, updateFrequency, sendVelocityUpdates);

		try {
			Field field = worldServer.tracker.getClass().getDeclaredField("c");
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			Set<EntityTrackerEntry> list = (Set<EntityTrackerEntry>) field.get(worldServer.tracker);

			if (worldServer.tracker.trackedEntities.b(entity.getId())) { // if contains entity tracker
				// go to delete
				EntityTrackerEntry oldTracker = worldServer.tracker.trackedEntities.d(entity.getId());
				list.remove(oldTracker);
				oldTracker.a();
			}

			list.add(tracker);
			worldServer.tracker.trackedEntities.a(entity.getId(), tracker);
			tracker.scanPlayers(worldServer.players);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return tracker;
	}

	public static void saveBasicInfoNBT(INPC entity, NBTTagCompound nbttagcompound) {
		try {
			nbttagcompound.setBoolean("damageable", entity.isDamageable());
			nbttagcompound.setBoolean("gravity", entity.isGravity());
			nbttagcompound.setBoolean("freezing", entity.isFreezing());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void restoreBasicInfoNBT(INPC entity, NBTTagCompound nbttagcompound) {
		try {
			entity.setDamageable(nbttagcompound.getBoolean("damageable"));
			entity.setGravity(nbttagcompound.getBoolean("gravity"));
			entity.setFreezing(nbttagcompound.getBoolean("freezing"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
