package rpg_game.characters;

import rpg_game.interfaces.IDefendable;
import rpg_game.monsters.Monster;

public abstract class Character implements IDefendable {

    private String name;
    private int health;
    private int power;
    private String race;

    public Character(String name, int health, int power, String race) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.race = race;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getRace() {
        return this.race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public abstract void attack(Monster target);
    public abstract void defend(int attackPower);

    public boolean isAlive() {
        return this.health > 0;
    }
}
