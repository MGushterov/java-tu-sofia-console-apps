public class SumOfArraySegments {
    public static void main(String[] args) throws InterruptedException {
        int[] numbers = ArrayGenerator.generate();
        Sum sum = new Sum();

        Thread tr1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 34; i++) {
                    int num = numbers[i];
                    sum.addToSum(num);
                }
            }
        });
        Thread tr2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 34; i < 67; i++) {
                    int num = numbers[i];
                    sum.addToSum(num);
                }
            }
        });
        Thread tr3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 67; i < 100; i++) {
                    int num = numbers[i];
                    sum.addToSum(num);
                }
            }
        });

        tr1.start();
        tr2.start();
        tr3.start();

        tr1.join();
        tr2.join();
        tr3.join();

        System.out.println("Total sum (with threads) is: " + sum.getSum());
        int sum2 = 0;
        for(int i = 0; i < 100; i++) {
            sum2 += numbers[i];
        }
        System.out.println("Total sum (without threads) is: " + sum2);
    }
}

class ArrayGenerator {
    public static int[] generate() {
        int[] numsArray = new int[100];
        for(int i = 0; i < 100; i++) {
            int randomNum = (int)Math.floor(Math.random() * 100);
            numsArray[i] = randomNum;
        }
        return numsArray;
    }
}

class Sum {
    private int sum = 0;

    public synchronized void addToSum(int value) {
        this.sum += value;
    }

    public int getSum() {
        return this.sum;
    }
}