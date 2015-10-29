package ru.BeYkeRYkt.DevNPC.implementation.utils;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.spigotmc.AsyncCatcher;

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.AttributeMapServer;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityEnderSignal;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityLeash;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.IAnimal;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityPainting;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateEntityNBT;
import ru.BeYkeRYkt.DevNPC.api.entity.ICustomPacketsEntity;

public class CustomEntityTrackerEntry extends EntityTrackerEntry {

	private boolean velocity;

	public CustomEntityTrackerEntry(Entity entity, int i, int j, boolean flag) {
		super(entity, i, j, flag);
		velocity = flag;
	}

	@Override
	public void updatePlayer(EntityPlayer entityplayer) {
		AsyncCatcher.catchOp("player tracker update");
		if (entityplayer != this.tracker)
			if (c(entityplayer)) {
				if ((this.trackedPlayers.contains(entityplayer)) || ((!(e(entityplayer))) && (!(this.tracker.attachedToPlayer))))
					return;
				if (this.tracker instanceof EntityPlayer) {
					Player player = ((EntityPlayer) this.tracker).getBukkitEntity();
					if (!(entityplayer.getBukkitEntity().canSee(player))) {
						return;
					}
				}

				entityplayer.removeQueue.remove(Integer.valueOf(this.tracker.getId()));

				this.trackedPlayers.add(entityplayer);

				for (Packet<?> packet : getSpawnPackets()) {
					entityplayer.playerConnection.sendPacket(packet);
				}

				for (Packet<?> packet : getUpdatePackets()) {
					entityplayer.playerConnection.sendPacket(packet);
				}

				if (!(this.tracker.getDataWatcher().d())) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(this.tracker.getId(), this.tracker.getDataWatcher(), true));
				}

				NBTTagCompound nbttagcompound = this.tracker.getNBTTag();

				if (nbttagcompound != null) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateEntityNBT(this.tracker.getId(), nbttagcompound));
				}

				if (this.tracker instanceof EntityLiving) {
					AttributeMapServer attributemapserver = (AttributeMapServer) ((EntityLiving) this.tracker).getAttributeMap();
					Collection<AttributeInstance> collection = attributemapserver.c();

					if (this.tracker.getId() == entityplayer.getId()) {
						((EntityPlayer) this.tracker).getBukkitEntity().injectScaledMaxHealth(collection, false);
					}

					if (!(collection.isEmpty())) {
						entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(this.tracker.getId(), collection));
					}
				}

				this.j = this.tracker.motX;
				this.k = this.tracker.motY;
				this.l = this.tracker.motZ;
				if ((this.velocity)) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(this.tracker.getId(), this.tracker.motX, this.tracker.motY, this.tracker.motZ));
				}

				if (this.tracker.vehicle != null) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this.tracker, this.tracker.vehicle));
				}

				if ((this.tracker instanceof EntityInsentient) && (((EntityInsentient) this.tracker).getLeashHolder() != null)) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(1, this.tracker, ((EntityInsentient) this.tracker).getLeashHolder()));
				}

				if (this.tracker instanceof EntityLiving) {
					for (int i = 0; i < 5; ++i) {
						ItemStack itemstack = ((EntityLiving) this.tracker).getEquipment(i);

						if (itemstack != null) {
							entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(this.tracker.getId(), i, itemstack));
						}
					}
				}

				if (this.tracker instanceof EntityHuman) {
					EntityHuman entityhuman = (EntityHuman) this.tracker;

					if (entityhuman.isSleeping()) {
						entityplayer.playerConnection.sendPacket(new PacketPlayOutBed(entityhuman, new BlockPosition(this.tracker)));
					}
				}

				this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
				broadcast(new PacketPlayOutEntityHeadRotation(this.tracker, (byte) this.i));

				if (this.tracker instanceof EntityLiving) {
					EntityLiving entityliving = (EntityLiving) this.tracker;
					Iterator<?> iterator = entityliving.getEffects().iterator();

					while (iterator.hasNext()) {
						MobEffect mobeffect = (MobEffect) iterator.next();

						entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.tracker.getId(), mobeffect));
					}
				}
			} else if (this.trackedPlayers.contains(entityplayer)) {
				this.trackedPlayers.remove(entityplayer);
				entityplayer.d(this.tracker);
			}
	}

	private boolean e(EntityPlayer entityplayer) {
		return entityplayer.u().getPlayerChunkMap().a(entityplayer, this.tracker.ae, this.tracker.ag);
	}

	private Packet<?>[] getSpawnPackets() {
		if (ICustomPacketsEntity.class.isAssignableFrom(tracker.getClass())) {
			ICustomPacketsEntity npc = (ICustomPacketsEntity) tracker;
			return (Packet<?>[]) npc.getSpawnPackets();
		}
		return new Packet<?>[] { c() };
	}

	private Packet<?>[] getUpdatePackets() {
		if (ICustomPacketsEntity.class.isAssignableFrom(tracker.getClass())) {
			ICustomPacketsEntity npc = (ICustomPacketsEntity) tracker;
			return (Packet<?>[]) npc.getUpdatePackets();
		}
		return new Packet<?>[] {};
	}

	private Packet<?> c() {
		if (this.tracker.dead) {
			return null;
		}

		if (this.tracker instanceof EntityItem)
			return new PacketPlayOutSpawnEntity(this.tracker, 2, 1);
		if (this.tracker instanceof EntityPlayer)
			return new PacketPlayOutNamedEntitySpawn((EntityHuman) this.tracker);
		if (this.tracker instanceof EntityMinecartAbstract) {
			EntityMinecartAbstract entityminecartabstract = (EntityMinecartAbstract) this.tracker;

			return new PacketPlayOutSpawnEntity(this.tracker, 10, entityminecartabstract.s().a());
		}
		if (this.tracker instanceof EntityBoat)
			return new PacketPlayOutSpawnEntity(this.tracker, 1);
		if (this.tracker instanceof IAnimal) {
			this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
			return new PacketPlayOutSpawnEntityLiving((EntityLiving) this.tracker);
		}
		if (this.tracker instanceof EntityFishingHook) {
			EntityHuman entityhuman = ((EntityFishingHook) this.tracker).owner;

			return new PacketPlayOutSpawnEntity(this.tracker, 90, (entityhuman != null) ? entityhuman.getId() : this.tracker.getId());
		}
		if (this.tracker instanceof EntityArrow) {
			Entity entity = ((EntityArrow) this.tracker).shooter;

			return new PacketPlayOutSpawnEntity(this.tracker, 60, (entity != null) ? entity.getId() : this.tracker.getId());
		}
		if (this.tracker instanceof EntitySnowball)
			return new PacketPlayOutSpawnEntity(this.tracker, 61);
		if (this.tracker instanceof EntityPotion)
			return new PacketPlayOutSpawnEntity(this.tracker, 73, ((EntityPotion) this.tracker).getPotionValue());
		if (this.tracker instanceof EntityThrownExpBottle)
			return new PacketPlayOutSpawnEntity(this.tracker, 75);
		if (this.tracker instanceof EntityEnderPearl)
			return new PacketPlayOutSpawnEntity(this.tracker, 65);
		if (this.tracker instanceof EntityEnderSignal)
			return new PacketPlayOutSpawnEntity(this.tracker, 72);
		if (this.tracker instanceof EntityFireworks) {
			return new PacketPlayOutSpawnEntity(this.tracker, 76);
		}

		if (this.tracker instanceof EntityFireball) {
			EntityFireball entityfireball = (EntityFireball) this.tracker;

			PacketPlayOutSpawnEntity packetplayoutspawnentity = null;
			byte b0 = 63;

			if (this.tracker instanceof EntitySmallFireball)
				b0 = 64;
			else if (this.tracker instanceof EntityWitherSkull) {
				b0 = 66;
			}

			if (entityfireball.shooter != null)
				packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, b0, ((EntityFireball) this.tracker).shooter.getId());
			else {
				packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, b0, 0);
			}

			packetplayoutspawnentity.d((int) (entityfireball.dirX * 8000.0D));
			packetplayoutspawnentity.e((int) (entityfireball.dirY * 8000.0D));
			packetplayoutspawnentity.f((int) (entityfireball.dirZ * 8000.0D));
			return packetplayoutspawnentity;
		}
		if (this.tracker instanceof EntityEgg)
			return new PacketPlayOutSpawnEntity(this.tracker, 62);
		if (this.tracker instanceof EntityTNTPrimed)
			return new PacketPlayOutSpawnEntity(this.tracker, 50);
		if (this.tracker instanceof EntityEnderCrystal)
			return new PacketPlayOutSpawnEntity(this.tracker, 51);
		if (this.tracker instanceof EntityFallingBlock) {
			EntityFallingBlock entityfallingblock = (EntityFallingBlock) this.tracker;

			return new PacketPlayOutSpawnEntity(this.tracker, 70, Block.getCombinedId(entityfallingblock.getBlock()));
		}
		if (this.tracker instanceof EntityArmorStand)
			return new PacketPlayOutSpawnEntity(this.tracker, 78);
		if (this.tracker instanceof EntityPainting) {
			return new PacketPlayOutSpawnEntityPainting((EntityPainting) this.tracker);
		}

		if (this.tracker instanceof EntityItemFrame) {
			EntityItemFrame entityitemframe = (EntityItemFrame) this.tracker;

			PacketPlayOutSpawnEntity packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, 71, entityitemframe.direction.b());
			BlockPosition blockposition = entityitemframe.getBlockPosition();
			packetplayoutspawnentity.a(MathHelper.d(blockposition.getX() * 32));
			packetplayoutspawnentity.b(MathHelper.d(blockposition.getY() * 32));
			packetplayoutspawnentity.c(MathHelper.d(blockposition.getZ() * 32));
			return packetplayoutspawnentity;
		}
		if (this.tracker instanceof EntityLeash) {
			EntityLeash entityleash = (EntityLeash) this.tracker;

			PacketPlayOutSpawnEntity packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, 77);
			BlockPosition blockposition = entityleash.getBlockPosition();
			packetplayoutspawnentity.a(MathHelper.d(blockposition.getX() * 32));
			packetplayoutspawnentity.b(MathHelper.d(blockposition.getY() * 32));
			packetplayoutspawnentity.c(MathHelper.d(blockposition.getZ() * 32));
			return packetplayoutspawnentity;
		}
		if (this.tracker instanceof EntityExperienceOrb) {
			return new PacketPlayOutSpawnEntityExperienceOrb((EntityExperienceOrb) this.tracker);
		}
		throw new IllegalArgumentException("Don't know how to add " + this.tracker.getClass() + "!");
	}
}
