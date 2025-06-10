package rpg_game.characters;

import rpg_game.interfaces.IHealable;
import rpg_game.monsters.Monster;

public class Paladin extends Character implements IHealable {
    private static final int MAX_HEALTH = 100;

    public Paladin(String name) {
        super(name, MAX_HEALTH, 15, "Human");
    }

    @Override
    public void attack(Monster target) {
        target.takeDamage(this.getPower());
        if (getHealth() + 5 <= MAX_HEALTH) {
            heal(this, 5);
        }
    }

    @Override
    public void defend(int attackPower) {
        setHealth(Math.max(0, getHealth() - attackPower));
        if (getHealth() > 0) {
            heal(this, 5);
        }
        System.out.printf("%s took %d damage and then healed with 5 HP! -> Remaining health: %d\n",
                getName(),
                attackPower,
                getHealth());
    }

    @Override
    public void heal(Character target, int amount) {
        target.setHealth(target.getHealth() + amount);
    }
}
