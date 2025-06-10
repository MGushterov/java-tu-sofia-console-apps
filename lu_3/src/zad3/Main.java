package zad3;

class NewClass1 {
    public static void main(String[] args)
    {
        try {
            MusicShop ms = new MusicShop();
            Viola viola = new Viola();
            Keyboard keyboard = new Keyboard();
            Drum drum = new Drum();
            Sax sax = new Sax();

            ms.demoInstrument(viola);
            ms.demoInstrument(keyboard);
            ms.demoInstrument(drum);
            ms.demoInstrument(sax);
        }
        catch(NotPlayableException e)
        {
            System.out.println(e.getMessage());
        }
    }
}


abstract class MusicalInstrument {
    public String name;
    public String brand;
    public double price;

}

interface Playable {
    public void play();
}

class Keyboard extends MusicalInstrument implements Playable {

    @Override
    public void play() {
        System.out.println("Playing instrument");
    }
}

class Viola extends MusicalInstrument implements Playable {


    @Override
    public void play() {
        System.out.println("Playing instrument");
    }
}

class Sax extends MusicalInstrument implements Playable {


    @Override
    public void play() {
        System.out.println("Playing instrument");
    }
}

class Drum extends MusicalInstrument {

}

class NotPlayableException extends RuntimeException {
    public NotPlayableException(String message) {
        super(message);
    }
}

class MusicShop {
    public void demoInstrument(MusicalInstrument instrument) throws NotPlayableException {
        if(instrument instanceof Playable) {
            ((Playable)instrument).play();
        } else {
            throw new NotPlayableException("This instrument can't be played for a demo!");
        }
    }
}