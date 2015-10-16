package ru.BeYkeRYkt.DevNPC.implementation.entity.nms;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Enchantment;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import ru.BeYkeRYkt.DevNPC.api.Animations;
import ru.BeYkeRYkt.DevNPC.api.entity.ICustomPacketsEntity;
import ru.BeYkeRYkt.DevNPC.api.entity.INMSCustomEntity;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.HumanNPC;
import ru.BeYkeRYkt.DevNPC.implementation.entity.CraftEntityNPC;
import ru.BeYkeRYkt.DevNPC.implementation.entity.CraftHumanNPC;
import ru.BeYkeRYkt.DevNPC.implementation.utils.NMSHelper;

public class EntityHumanNPC extends EntityCreature implements INMSCustomEntity, ICustomPacketsEntity, IRangedEntity {

	private HumanNPC npc;
	public GameProfile profile;
	private float bF = 0.02F;
	public float bo;
	private EntityTrackerEntry tracker;

	public EntityHumanNPC(World world) {
		super(world);
		bX();
		j(true);
		this.maxFireTicks = 20;
		setCustomName("");

		((Navigation) getNavigation()).a(true);
		((Navigation) getNavigation()).b(true);
		setSize(0.6F, 1.8F);
	}

	@Override
	public void ah() { // onChunkLoad
		if (tracker == null) {
			this.tracker = NMSHelper.registerEntityTracker(this, 80, 3, false);
		}
	}

	@Override
	protected boolean isTypeNotPersistent() {
		// return super.isTypeNotPersistent();
		return false;
	}

	@Override
	public void setCustomName(String name) {
		super.setCustomName(name);
		this.profile = new GameProfile(b(name), name);
		if (tracker != null) {
			tracker.a();
			tracker.trackedPlayers.clear();
			tracker.scanPlayers(world.players);
		}
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3F);
		getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
	}

	@Override
	protected void h() {
		super.h();
		this.datawatcher.a(13, String.valueOf(""));
		this.datawatcher.a(14, Integer.valueOf(0));
		this.datawatcher.a(25, Integer.valueOf(0));
		// this.datawatcher.a(16, Byte.valueOf((byte) 0));
		// this.datawatcher.a(17, "");
		this.datawatcher.a(23, Byte.valueOf((byte) 0));
		this.datawatcher.a(24, Integer.valueOf(0));
	}

	@Override
	public HumanNPC getBukkitNPC() {
		return (HumanNPC) getBukkitEntity();
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (npc == null) {
			npc = new CraftHumanNPC(this.world.getServer(), this);
		}
		return (CraftEntity) npc;
	}

	public GameProfile getProfile() {
		return profile;
	}

	public UUID b(String s) {
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s.getBytes(Charsets.UTF_8)).getBytes());
	}

	// ****************************************************************//
	// Generic
	// ****************************************************************//
	@Override
	public void m() { // onTick ?
		if (!getBukkitNPC().isFreezing()) {
			this.yaw = this.aK; // head fix

			// Vehicle control
			if ((au()) && (getGoalTarget() != null) && (this.vehicle instanceof EntityHorse) && (this.vehicle instanceof EntityPig)) {
				((EntityInsentient) this.vehicle).getNavigation().a(getNavigation().j(), 1.5D);
			}

			// Regeneration health
			if ((this.world.getDifficulty() == EnumDifficulty.PEACEFUL) && (this.world.getGameRules().getBoolean("naturalRegeneration") && getBukkitNPC().canRegenerationHealth())) {
				if ((getHealth() < getMaxHealth()) && (this.ticksLived % 20 == 0)) {
					heal(1.0F, RegainReason.REGEN);
				}
			}

			AttributeInstance attributeinstance = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

			this.aM = this.bF;
			if (isSprinting()) {
				this.aM = (float) (this.aM + this.bF * 0.3D);
			}

			k((float) attributeinstance.getValue());

			float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
			float f1 = (float) (TrigMath.atan(-this.motY * 0.2000000029802322D) * 15.0D);

			if (f > 0.1F) {
				f = 0.1F;
			}

			if ((!(this.onGround)) || (getHealth() <= 0.0F)) {
				f = 0.0F;
			}

			if ((this.onGround) || (getHealth() <= 0.0F)) {
				f1 = 0.0F;
			}

			this.bo += (f - this.bo) * 0.4F;
			this.aF += (f1 - this.aF) * 0.8F;

			super.m();
		}
	}

	protected void dropEquipment(boolean flag, int i) {
		return;
	}

	@Override
	public void t_() { // onTick
		if (!getBukkitNPC().isFreezing()) {
			// if ((isBurning()) && (this.abilities.isInvulnerable)) {
			if (isBurning() && !getBukkitNPC().isDamageable()) {
				extinguish();
			}

			double d4 = MathHelper.a(this.locX, -29999999.0D, 29999999.0D);
			double d5 = MathHelper.a(this.locZ, -29999999.0D, 29999999.0D);

			if ((d4 != this.locX) || (d5 != this.locZ)) {
				setPosition(d4, this.locY, d5);
			}

			if (this.noDamageTicks > 0) {
				this.noDamageTicks -= 1;
			}

			super.t_();
		}
	}

	@Override
	public void k(float f) {
		if (!getBukkitNPC().isFreezing() && !isSleeping()) {
			super.k(f);
		}
	}

	@Override
	public float getHeadHeight() {
		float f = 1.62F;

		if (isSleeping()) {
			f = 0.2F;
		}

		if (isSneaking()) {
			f -= 0.08F;
		}

		return f;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (!getBukkitNPC().isDamageable()) {
			return false;
		}

		this.ticksFarFromPlayer = 0;
		if (getHealth() <= 0.0F) {
			return false;
		}
		return super.damageEntity(damagesource, f);
	}

	@Override
	public void die(DamageSource damagesource) {
		super.die(damagesource);
		setSize(0.2F, 0.2F);
		setPosition(this.locX, this.locY, this.locZ);
		this.motY = 0.1000000014901161D;
		if (damagesource != null) {
			this.motX = (-MathHelper.cos((this.aw + this.yaw) * 3.141593F / 180.0F) * 0.1F);
			this.motZ = (-MathHelper.sin((this.aw + this.yaw) * 3.141593F / 180.0F) * 0.1F);
		} else {
			this.motX = (this.motZ = 0.0D);
		}
	}

	@Override
	public void g(float f, float f1) {
		if (!getBukkitNPC().isFreezing()) {
			super.g(f, f1);
		}
	}

	// Restore
	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		NMSHelper.restoreBasicInfoNBT(getBukkitNPC(), nbttagcompound);
		((CraftEntityNPC) getBukkitNPC()).readExtraData(nbttagcompound);
	}

	// Backup
	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		NMSHelper.saveBasicInfoNBT(getBukkitNPC(), nbttagcompound);
		((CraftEntityNPC) getBukkitNPC()).setExtraData(nbttagcompound);
	}

	// ****************************************************************//
	// Sounds
	// ****************************************************************//
	@Override
	protected String P() {
		return "game.player.swim";
	}

	@Override
	protected String aa() {
		return "game.player.swim.splash";
	}

	@Override
	protected String z() {
		// return "mob.zombie.say";
		return getBukkitNPC().getCharacter().getSaySound();
	}

	@Override
	protected String bo() {
		// return "mob.zombie.hurt";
		return getBukkitNPC().getCharacter().getDamageSound();
	}

	@Override
	protected String bp() {
		// return "mob.zombie.death";
		return getBukkitNPC().getCharacter().getDeathSound();
	}

	@Override
	protected void a(BlockPosition blockposition, Block block) {
		makeSound("mob.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected String n(int i) {
		return ((i > 4) ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small");
	}

	@Override
	public boolean r(Entity entity) {
		float f = (float) getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entity instanceof EntityLiving) {
			f += EnchantmentManager.a(bA(), ((EntityLiving) entity).getMonsterType());
			i += EnchantmentManager.a(this);
		}

		NMSHelper.playAnimation(this, Animations.SWING_ARM); // PlayAnimation
		boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);

		if (flag) {
			if (i > 0) {
				entity.g(-MathHelper.sin(this.yaw * 3.141593F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.yaw * 3.141593F / 180.0F) * i * 0.5F);
				this.motX *= 0.6D;
				this.motZ *= 0.6D;
				setSprinting(false);
			}

			int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);

			if (j > 0) {
				EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), j * 4);
				Bukkit.getPluginManager().callEvent(combustEvent);

				if (!(combustEvent.isCancelled())) {
					entity.setOnFire(combustEvent.getDuration());
				}

			}

			a(this, entity);
		}
		// u().getTracker().a(this, new PacketPlayOutAnimation(this, 0));
		super.r(entity);
		return flag;
	}

	@Override
	public Packet<?>[] getSpawnPackets() {
		return new Packet<?>[] { getPlayerListPacket(EnumPlayerInfoAction.ADD_PLAYER), getSpawnPacket(), getPlayerListPacket(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME), getPlayerListPacket(EnumPlayerInfoAction.REMOVE_PLAYER) };
	}

	@Override
	public Packet<?>[] getUpdatePackets() {
		return new Packet[] {};
	}

	public Packet<?> getPlayerListPacket(EnumPlayerInfoAction a) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		try {
			Field action = packet.getClass().getDeclaredField("a");
			action.setAccessible(true);
			action.set(packet, a);

			Field data = packet.getClass().getDeclaredField("b");
			data.setAccessible(true);
			List<PlayerInfoData> info = (List<PlayerInfoData>) data.get(packet);
			PlayerInfoData playerInfo = packet.new PlayerInfoData(getProfile(), 0, EnumGamemode.SURVIVAL, null);
			info.add(playerInfo);
			data.set(packet, info);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public Packet<?> getSpawnPacket() {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		try {
			Field id = packet.getClass().getDeclaredField("a");
			Field uuid = packet.getClass().getDeclaredField("b");
			Field x = packet.getClass().getDeclaredField("c");
			Field y = packet.getClass().getDeclaredField("d");
			Field z = packet.getClass().getDeclaredField("e");
			Field yaw = packet.getClass().getDeclaredField("f");
			Field pitch = packet.getClass().getDeclaredField("g");
			Field itemId = packet.getClass().getDeclaredField("h");
			Field dataWatcher = packet.getClass().getDeclaredField("i");

			if (!id.isAccessible()) {
				id.setAccessible(true);
			}

			if (!uuid.isAccessible()) {
				uuid.setAccessible(true);
			}

			if (!x.isAccessible()) {
				x.setAccessible(true);
			}

			if (!y.isAccessible()) {
				y.setAccessible(true);
			}

			if (!z.isAccessible()) {
				z.setAccessible(true);
			}

			if (!yaw.isAccessible()) {
				yaw.setAccessible(true);
			}

			if (!pitch.isAccessible()) {
				pitch.setAccessible(true);
			}

			if (!itemId.isAccessible()) {
				itemId.setAccessible(true);
			}

			if (!dataWatcher.isAccessible()) {
				dataWatcher.setAccessible(true);
			}

			id.set(packet, getId());
			uuid.set(packet, getProfile().getId());
			x.set(packet, MathHelper.floor(this.locX * 32.0D));
			y.set(packet, MathHelper.floor(this.locY * 32.0D));
			z.set(packet, MathHelper.floor(this.locZ * 32.0D));
			yaw.set(packet, (byte) (int) (this.yaw * 256.0F / 360.0F));
			pitch.set(packet, (byte) (int) (this.pitch * 256.0F / 360.0F));
			ItemStack item = getEquipment(0);
			itemId.set(packet, ((item == null) ? 0 : Item.getId(item.getItem())));// TEST
			dataWatcher.set(packet, getDataWatcher());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public void shoot(EntityLiving target, float damage) {
		a(target, damage);
	}

	@Override
	public void a(EntityLiving paramEntityLiving, float paramFloat) {
		EntityArrow entityarrow = new EntityArrow(this.world, this, paramEntityLiving, 1.6F, 14 - (this.world.getDifficulty().a() * 4));
		int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, bA());
		int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, bA());

		entityarrow.b(paramFloat * 2.0F + this.random.nextGaussian() * 0.25D + this.world.getDifficulty().a() * 0.11F);
		if (i > 0) {
			entityarrow.b(entityarrow.j() + i * 0.5D + 0.5D);
		}

		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}

		if ((EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bA()) > 0)) {
			EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
			this.world.getServer().getPluginManager().callEvent(event);

			if (!(event.isCancelled())) {
				entityarrow.setOnFire(event.getDuration());
			}

		}

		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, bA(), entityarrow, 0.8F);
		if (event.isCancelled()) {
			event.getProjectile().remove();
			return;
		}

		if (event.getProjectile() == entityarrow.getBukkitEntity()) {
			this.world.addEntity(entityarrow);
		}

		makeSound("random.bow", 1.0F, 1.0F / (bc().nextFloat() * 0.4F + 0.8F));
	}
}
