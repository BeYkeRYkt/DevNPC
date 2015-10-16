package ru.BeYkeRYkt.DevNPC.api.path;

public interface AIManager {

	public void addTask(int i, AIGoal ai);

	public void removeTask(AIGoal ai);

	public void clear();
}
