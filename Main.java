import java.awt.Point;
import Stracture.Grid;
import Stracture.PrintingTools;
import Stracture.Reward;
import Technics.PolicyIterationBoltzmann;
import Technics.SARSA;
import Technics.ValueIteration;

public class Main {
    public static void main(String[] args) {
        int height = 3;
        int width = 4;
    
        Reward[] rewards = {
            new Reward(0, 3, 1),
            new Reward(1, 3, -1),
            new Reward(1, 1, 0)
        };
        int size =0;
        for (int i = 0; i < rewards.length; i++) {
            if (rewards[i].getReward() == 0) 
                size++;
        }
        Point[] walls = new Point[size];
        int j = 0;
        for (int i = 0; i < rewards.length; i++) {
            if (rewards[i].getReward() == 0) {
                walls[j++] = new Point(rewards[i].getX(), rewards[i].getY());
            }
        }
        double stepCost = -0.04;

        Grid grid = new Grid(width, height, rewards, stepCost, walls);

        double discountFactor = 0.5;
        double theta = 0.01;
        double alpha = 0.1;
        double epsilon = 0.1;
        int episodes =1000;
        double p = 0.8;
        long startTime;
        long endTime;
        
        // Measure time for Value Iteration
        startTime = System.currentTimeMillis();
        ValueIteration vi = new ValueIteration(grid, discountFactor, theta, p);
        vi.run();
        endTime = System.currentTimeMillis();
        long viTime = endTime - startTime;
        System.out.println();
        System.out.println("Value Iteration Time: " + viTime + " ms");


        System.out.println("Value Iteration Results:");
        PrintingTools.printPolicy(vi.getPolicy(), grid);


        startTime = System.currentTimeMillis();
        PolicyIterationBoltzmann PIB = new PolicyIterationBoltzmann(grid,discountFactor, theta, p,0.01);
        PIB.run();
        endTime = System.currentTimeMillis();
        long pibTime = endTime - startTime;
        System.out.println("PolicyIterationBoltzmann Time: " + pibTime + " ms");

        System.out.println("PIB Results:");
        PrintingTools.printPolicy(PIB.getPolicy(), grid);


        // Measure time for SARSA
        startTime = System.currentTimeMillis();
        SARSA sarsa = new SARSA(grid, alpha, discountFactor, epsilon,episodes, theta,p);
        sarsa.run();
        endTime = System.currentTimeMillis();
        long sarsaTime = endTime - startTime;
        System.out.println("SARSA Time: " + sarsaTime + " ms");


        System.out.println("SARSA Results:");
        PrintingTools.printPolicy(sarsa.getPolicy(), grid);
    }
}
