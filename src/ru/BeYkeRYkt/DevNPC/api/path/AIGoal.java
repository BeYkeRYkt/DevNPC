package ru.BeYkeRYkt.DevNPC.api.path;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;

public interface AIGoal {

	public boolean shouldExecute();

	public void updateGoal();

	public void startExecuting();

	public void resetGoal();

	public boolean canContinue();

	public LivingEntityNPC getEntity();
}
