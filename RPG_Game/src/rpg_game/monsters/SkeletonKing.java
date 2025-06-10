package rpg_game.monsters;

import rpg_game.characters.Character;

public class SkeletonKing extends Monster {

    public SkeletonKing() {
        super("Skeleton King", 80, 15, "Undead");
    }

    @Override
    public void attack(Character target) {
        target.defend(getPower());
    }
}
