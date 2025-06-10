package rpg_game.characters;

import rpg_game.monsters.Monster;

public class Barbarian extends Character {
    private static final int BARBARIAN_POWER = 200;
    private static final String RACE = "Barbarian";

    public Barbarian(int health, String name) {
        super(name, health, BARBARIAN_POWER, RACE);
    }

    @Override
    public void attack(Monster target) {
        target.takeDamage(this.getPower());
        System.out.printf("Damage dealt to %s: %d", target.getName(), this.getPower());
    }

    @Override
    public void defend(int attackPower) {
        this.setHealth(Math.round(attackPower * 0.95f));
    }
}
