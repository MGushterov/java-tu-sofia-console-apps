public class Main {
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Should be exactly 3 arguments");
            return;
        }
        String carType = args[0];
        int speed = Integer.parseInt(args[1]);
        int fuel = Integer.parseInt(args[2]);

        if(carType.equals("SimpleCar")) {
            System.out.println("This is a " + carType + ".");
            SimpleCar sCar = new SimpleCar(speed, fuel);
            sCar.refuel();
        } else if(carType.equals("ElectricCar")) {
            System.out.println("This is a " + carType + ".");
            ElectricCar eCar = new ElectricCar(speed, fuel);
            eCar.refuel();
        } else {
            System.out.println("Incorrect car type!");
        }
    }
}

abstract class Vehicle {
    abstract protected void accelerate();
    abstract protected void brake();
}

interface Refuelable {
    void refuel();
}

class SimpleCar extends Vehicle implements Refuelable{
    private int speed;
    private int fuel;
    private final int MAX_SPEED = 50;
    private final int MIN_SPEED = 0;

    public SimpleCar(int speed, int fuel) {
        this.speed = speed;
        this.fuel = fuel;
    }

    protected void accelerate() {
        if(this.fuel - 1 <= 0) {
            System.out.println("Out of battery!");
        }
        else {
            this.fuel--;
        }

        if(this.speed + 10 <= MAX_SPEED) {
            this.speed += 10;
        }
        else {
           this.speed = MAX_SPEED;
        }
        System.out.println("Speed after accelerate: " + this.speed + ", Fuel left: " + this.fuel);
    }

    protected void brake() {
        if(this.speed - 20 >= MIN_SPEED) {
            this.speed -= 20;
        }
        else {
            this.speed = MIN_SPEED;
        }
        System.out.println("Speed after brake: " + this.speed);
    }

    @Override
    public void refuel() {
        this.fuel += 10;
        System.out.println("Car refueled, Fuel left: " + this.fuel);
    }
}

class ElectricCar extends Vehicle implements Refuelable{
    private int speed;
    private int battery;
    private final int MAX_SPEED = 50;
    private final int MIN_SPEED = 0;

    public ElectricCar(int speed, int battery) {
        this.speed = speed;
        this.battery = battery;
    }

    protected void accelerate() {
        if(this.battery - 1 <= 0) {
            System.out.println("Battery empty!");
        }
        else {
            this.battery--;
        }

        if(this.speed + 10 <= MAX_SPEED) {
            this.speed += 10;
        }
        else {
            this.speed = MAX_SPEED;
        }
        System.out.println("Speed after accelerate: " + this.speed + ", Battery: " + this.battery);
    }

    protected void brake() {
        if(this.speed - 20 >= MIN_SPEED) {
            this.speed -= 20;
        }
        else {
            this.speed = MIN_SPEED;
        }
        System.out.println("Speed after brake: " + this.speed);
    }

    @Override
    public void refuel() {
        this.battery += 20;
        System.out.println("Car refueled, Battery left: " + this.battery);
    }
}