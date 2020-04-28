import java.util.Random;

public class Player{
    private final Random random = new Random();

    private int points;

    public Player() {
        this.points = 0;
    }

    public void increasePoints() {
        this.points++;
    }

//    public void decreasePoints() {
//        this.points--;
//    }

    public int getPoints() {
        return points;
    }

    public char RPS_Roll() {
        int rand = random.nextInt(3);
        char roll;
        if (rand == 1) {
            roll = 'R';
        } else if (rand == 2) {
            roll = 'P';
        } else {
            roll = 'S';
        }
        return roll;
    }

}
