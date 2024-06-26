package Stracture;

public enum Action {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int deltaX;
    private final int deltaY;

    Action(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public ActionOutcome[] getOutcomes(double p) {
        return new ActionOutcome[]{
                new ActionOutcome(this, p),
                new ActionOutcome(this.getLeft(), (1-p)/2),
                new ActionOutcome(this.getRight(), (1-p)/2)
        };
    }

    public Action getLeft() {
        switch (this) {
            case UP -> {
                return LEFT;
            }
            case DOWN -> {
                return RIGHT;
            }
            case LEFT -> {
                return DOWN;
            }
            case RIGHT -> {
                return UP;
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public Action getRight() {
        switch (this) {
            case UP:
                return RIGHT;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case RIGHT:
                return DOWN;
            default:
                throw new IllegalArgumentException();
        }
    }
}
