package rpg_game.monsters;

import rpg_game.characters.Character;

public class FallenShaman extends Monster {

    public FallenShaman() {
        super("Fallen Shaman", 60, 10, "Undead");
    }

    @Override
    public void attack(Character target) {
        target.defend(getPower());
    }
}
