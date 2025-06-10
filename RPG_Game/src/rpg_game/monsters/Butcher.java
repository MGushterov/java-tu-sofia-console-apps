package rpg_game.monsters;

import rpg_game.characters.Character;

public class Butcher extends Monster {
    private static final int DEFAULT_RAGE_LEVEL = 0;
    private int rageLevel; // Всяка атака увеличава rage Level за по-силна атака

    public Butcher() {
        super("Butcher", 150, 20, "Demon");
        this.rageLevel = DEFAULT_RAGE_LEVEL;
    }

    @Override
    public void attack(Character target) {
        if (rageLevel >= 2) {
            int rageAttackPower = getPower() * 2;
            target.defend(rageAttackPower);
            System.out.println(getName() + " unleashes a powerful rage attack!");
            this.rageLevel = DEFAULT_RAGE_LEVEL;
        } else {
            target.defend(getPower());
            this.rageLevel++;
            System.out.println(getName() + " attacks with regular speed!");
        }
    }
}