package lesson4;

public class Lesson4 {

    private volatile char currentChar = 'A';

    public static void main(String[] args) {
        Lesson4 lesson4 = new Lesson4();
        new Thread(lesson4::printA).start();
        new Thread(lesson4::printB).start();
        new Thread(lesson4::printC).start();
    }

    private synchronized void printA() {
        for (int i = 0; i < 5; i++) {
            try {
                while (currentChar != 'A') {
                    this.wait();
                }
                System.out.print('A');
                currentChar = 'B';
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private synchronized void printB() {
        for (int i = 0; i < 5; i++) {
            try {
                while (currentChar != 'B') {
                    this.wait();
                }
                System.out.print('B');
                currentChar = 'C';
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private synchronized void printC() {
        for (int i = 0; i < 5; i++) {
            try {
                while (currentChar != 'C') {
                    this.wait();
                }
                System.out.print('C');
                currentChar = 'A';
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
