package ru.BeYkeRYkt.DevNPC.api.path.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIGoal;

public class SearchGoalTargetTask implements AIGoal {

	private LivingEntityNPC npc;
	private Class<? extends Entity> clazz;
	private LivingEntity target;

	public SearchGoalTargetTask(LivingEntityNPC npc, Class<? extends Entity> clazz) {
		this.npc = npc;
		this.clazz = clazz;
	}

	@Override
	public boolean shouldExecute() {
		if (getEntity().getTarget() != null && getEntity().getTarget().isDead() || getEntity().getTarget() == null) {
			// getEntity().setTarget(null);
			return true;
		}
		return false;
	}

	@Override
	public void updateGoal() {
		LivingEntity living = null;
		for (Entity entity : getEntity().getNearbyEntities(getEntity().getViewDistance(), getEntity().getViewDistance(), getEntity().getViewDistance())) {
			if (clazz.isAssignableFrom(entity.getClass())) {
				if (living == null) {
					living = (LivingEntity) entity;
				} else {
					if (getEntity().getLocation().distance(entity.getLocation()) < getEntity().getLocation().distance(living.getLocation())) {
						living = (LivingEntity) entity;
					}
				}
			}
		}

		if (living != null) {
			target = living;
		}
	}

	@Override
	public void startExecuting() {
	}

	@Override
	public void resetGoal() {
		if (target != null) {
			getEntity().setTarget(target);
			target = null;
		}
	}

	@Override
	public boolean canContinue() {
		if (target == null) {
			return true;
		} else if (getEntity().getTarget() != null) {
			return false;
		}
		return false;
	}

	@Override
	public LivingEntityNPC getEntity() {
		return npc;
	}

	@Override
	public int getMutexBits() {
		return 1;
	}

}
