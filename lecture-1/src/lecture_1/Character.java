package lecture_1;

public abstract class Character {
    private int health;
    private int damage;
    private double attackSpeed;
    private String race;
    private String name;

    public Character(int health, int damage, double attackSpeed, String race, String name) {
        this.health = health;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.race = race;
        this.name = name;
    }

    public void introduce() {
        System.out.println("I am " + this.race + "my name is " + this.name);
    }

    public int getHealth() {
        return health;
    }

    public void attack(Character defender) {
        defender.defend(this.damage);
    }

    private void defend(int damage) {
        this.health -= damage;
        System.out.println("Ouch! New Health: " + this.getHealth());
    }
}
