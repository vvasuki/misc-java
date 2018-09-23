package package1;

public class ThreadTest extends Thread {

    public void run() {
        while (true) {
            try {

                sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ThreadTest threadTest = new ThreadTest();
        threadTest.start();
        threadTest.interrupt();
        threadTest.start();
    }

}
