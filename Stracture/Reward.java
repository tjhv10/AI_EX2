package Stracture;

public class Reward {
    private final int x;
    private final int y;
    private final double reward;

    public Reward(int x, int y, double reward) {
        this.x = x;
        this.y = y;
        this.reward = reward;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getReward() {
        return reward;
    }
}
