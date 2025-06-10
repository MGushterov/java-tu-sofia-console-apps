package rpg_game.interfaces;

import rpg_game.characters.Character;

public interface IHealable {
    void heal(Character character, int health);
}

