package rpg_game;

import rpg_game.characters.Paladin;
import rpg_game.monsters.Butcher;

public class Main {
    public static void main(String[] args) {
        Paladin paladin = new Paladin("Boromir");
        Butcher butcher = new Butcher();
        while(paladin.isAlive() && butcher.isAlive()) {
            butcher.takeDamage(paladin.getPower());
            paladin.defend(butcher.getPower());
        }
        if(paladin.isAlive()) {
            System.out.println("Paladin is victorious!");
        }
        else {
            System.out.println("Butcher is victorious!");
        }
    }
}