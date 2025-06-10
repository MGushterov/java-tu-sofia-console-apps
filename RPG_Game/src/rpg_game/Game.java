package rpg_game;
import java.util.ArrayDeque;
import java.util.Random;

import rpg_game.characters.*;
import rpg_game.characters.Character;
import rpg_game.interfaces.IHealable;
import rpg_game.monsters.*;

public class Game {
    private Character hero;
    private ArrayDeque<Monster> monsters = new ArrayDeque<>();

    public void createHero(String name, String race) {
        switch (race) {
            case "Human" -> hero = new Paladin(name);
            case "Orc" -> hero = new Barbarian(200, name);
            case "Bloody Elf" -> hero = new Witch(name);
            default -> throw new IllegalArgumentException("Invalid race: " + race);
        }
    }

    public void addMonster(Monster monster) {
        this.monsters.offer(monster);
    }

    public void startBattle() throws InterruptedException {
        while (!monsters.isEmpty() && hero.isAlive()) {
            Monster monster = this.monsters.poll();
            System.out.printf("The battle between %s and %s starts!%n", hero.getName(), monster.getName());

            while (monster.isAlive() && hero.isAlive()) {
                hero.attack(monster);
                System.out.printf("%s has %d HP.%n", monster.getName(), monster.getHealth());

                if (monster.isAlive()) {
                    monster.attack(hero);
                    System.out.printf("%s has %d HP.%n", hero.getName(), hero.getHealth());
                }
            }

            if (hero.isAlive()) {
                System.out.printf("%s killed %s!%n", hero.getName(), monster.getName());
                try {
                    recover(hero);
                } catch (NotHealableException exception){
                    System.out.println(exception.getMessage());
                }

            } else {
                System.out.printf("%s died!%n", hero.getName());
            }
        }
    }

    private void recover(Character character) throws NotHealableException {
        if(character instanceof IHealable) {
            throw new NotHealableException("This character cannot heal");
        }
        System.out.println("Hero is recovering! Wait 5 seconds...");
        hero.setHealth(hero.getHealth() + 20);
        System.out.println("Hero is recovered!");
    }

    public static Monster createRandomMonster() {
        Random random = new Random();
        int monsterType = random.nextInt(2);

        switch (monsterType) {
            case 1 -> {
                return new SkeletonKing();
            }
            case 2 -> {
                return new Butcher();
            }
            default -> {
                return new FallenShaman();
            }
        }
    }
}
