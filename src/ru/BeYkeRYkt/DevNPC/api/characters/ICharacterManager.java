package ru.BeYkeRYkt.DevNPC.api.characters;

import java.util.Collection;

public interface ICharacterManager {

	public Collection<ICharacter> getList();

	public boolean registerCharacter(ICharacter character);

	public boolean unregisterCharacter(ICharacter character);

	public boolean unregisterCharacter(String id);

	public boolean checkCharacter(String id);

	public ICharacter getCharacter(String id);
}
