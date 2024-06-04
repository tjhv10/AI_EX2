public class ActionOutcome {
    private Action action;
    private double probability;

    public ActionOutcome(Action action, double probability) {
        this.action = action;
        this.probability = probability;
    }

    public int getDeltaX() {
        return action.getDeltaX();
    }

    public int getDeltaY() {
        return action.getDeltaY();
    }

    public double getProbability() {
        return probability;
    }
}
