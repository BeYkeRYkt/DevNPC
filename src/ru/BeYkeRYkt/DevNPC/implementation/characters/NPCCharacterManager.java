package ru.BeYkeRYkt.DevNPC.implementation.characters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.BeYkeRYkt.DevNPC.api.characters.ICharacter;
import ru.BeYkeRYkt.DevNPC.api.characters.ICharacterManager;

public class NPCCharacterManager implements ICharacterManager {

	private Map<String, ICharacter> list;

	public NPCCharacterManager() {
		this.list = new HashMap<String, ICharacter>();
	}

	@Override
	public Collection<ICharacter> getList() {
		return list.values();
	}

	@Override
	public boolean registerCharacter(ICharacter character) {
		if (checkCharacter(character.getId())) {
			return false;
		}
		list.put(character.getId(), character);
		return true;
	}

	@Override
	public boolean unregisterCharacter(ICharacter character) {
		return unregisterCharacter(character.getId());
	}

	@Override
	public boolean unregisterCharacter(String id) {
		if (!checkCharacter(id)) {
			return false;
		}
		list.remove(id);
		return true;
	}

	@Override
	public boolean checkCharacter(String id) {
		return list.containsKey(id);
	}

	@Override
	public ICharacter getCharacter(String id) {
		if (!checkCharacter(id)) {
			return new NullCharacter("null");
		}
		return list.get(id);
	}

}
