package rpg_game.monsters;

import rpg_game.interfaces.IAttackable;
import rpg_game.characters.Character;

public abstract class Monster implements IAttackable {
    private String name;
    private int health;
    private int power;
    private String race;

    public Monster(String name, int health, int power, String race) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.race = race;
    }

    public abstract void attack(Character target);

    @Override
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public String getName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    public int getHealth() {
        return this.health;
    }

    public String getRace() {
        return this.race;
    }
}
