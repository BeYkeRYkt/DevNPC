package ru.BeYkeRYkt.DevNPC.api.path;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;

public interface AIGoal {

	public boolean shouldExecute();

	public void updateGoal();

	public void startExecuting();

	public void resetGoal();

	public boolean canContinue();

	public LivingEntityNPC getEntity();

	/**
	 * Get a bitmask telling which other tasks may not run concurrently. The test is a simple bitwise AND - if it yields zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
	 */
	public int getMutexBits();
}
