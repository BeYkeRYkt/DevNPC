package ru.BeYkeRYkt.DevNPC.api;

public enum Animations {

	// For packets: Player
	SWING_ARM(PacketType.ANIMATION, 0), // TAKE_DAMAGE(PacketType.ANIMATION, 1),
	LEAVE_BED(PacketType.ANIMATION, 2), EAT_FOOD(PacketType.ANIMATION, 3), CRITICAL_EFFECT(PacketType.ANIMATION, 4), MAGIC_CRITICAL_EFFECT(PacketType.ANIMATION, 5),

	// For entity status
	HURT(PacketType.ENTITY_STATUS, 2), DEAD(PacketType.ENTITY_STATUS, 3), IRON_GOLEM_ARMS_UP(PacketType.ENTITY_STATUS, 4), HEART_PARTICLES(PacketType.ENTITY_STATUS, 6), SMOKE_PARTICLES(PacketType.ENTITY_STATUS, 7), WOLF_SHAKING(PacketType.ENTITY_STATUS, 8), SHEEP_EATING_GRASS(PacketType.ENTITY_STATUS, 9), VILLAGER_ANGRY(PacketType.ENTITY_STATUS, 13), VILLAGER_HAPPY(PacketType.ENTITY_STATUS, 14), WITCH_MAGIC_PARTICLE(PacketType.ENTITY_STATUS, 15), ZOMBIE_CONVERTING(PacketType.ENTITY_STATUS, 16), FIREWORK_EXPLODING(PacketType.ENTITY_STATUS, 17), EXPLOSION_PARTICLE(PacketType.ENTITY_STATUS, 20), GUARDIAN_SOUND(PacketType.ENTITY_STATUS, 21),

	// For eat/drink etc.
	NONE(PacketType.NONE, -1), DRINK(PacketType.NONE, -1), BLOCK(PacketType.NONE, -1), BOW(PacketType.NONE, -1);

	public enum PacketType {
		NONE, ANIMATION, ENTITY_STATUS;
	}

	private int id;
	private PacketType type;

	private Animations(PacketType type, int id) {
		this.type = type;
		this.id = id;
	}

	public PacketType getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}
