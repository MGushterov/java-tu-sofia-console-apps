package rpg_game.characters;

import rpg_game.interfaces.IHealable;
import rpg_game.monsters.Monster;

public class Witch extends Character implements IHealable {
    public Witch(String name) {
        super(name, 90, 15, "Bloody Elf");
    }

    @Override
    public void attack(Monster target) {
        target.takeDamage(getPower());
        heal(this, 5);
    }

    @Override
    public void defend(int attackPower) {
        int reducedDamage = Math.max(attackPower - 3, 0);
        setHealth(Math.max(0, getHealth() - reducedDamage));
        if (getHealth() > 0) {
            heal(this, 5);
        }
    }

    @Override
    public void heal(Character target, int amount) {
        target.setHealth(target.getHealth() + amount);
        System.out.printf("%s healed %d HP! -> Remaining HP: %d%n", getName(), amount, getHealth());
    }
}
