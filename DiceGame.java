import java.util.*;

public class DiceGame {
    
    private static final int NUM_DICE = 5;
    private static final int NUM_SIMULATIONS = 10000;
    private static final int TARGET_VALUE = 3;
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        Simulation simulation = new Simulation(NUM_SIMULATIONS, NUM_DICE);
        simulation.runSimulation();
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        simulation.printResults(executionTime);
    }
    
    public static class Game {
        private List<Integer> dice;
        private int totalScore;
        
        public Game() {
            this.dice = new ArrayList<>();
            this.totalScore = 0;
        }
        
        public int playGame() {
            // Start with 5 dice
            dice.clear();
            for (int i = 0; i < NUM_DICE; i++) {
                dice.add(rollDie());
            }
            
            totalScore = 0;
            
            // Continue playing until no dice remain
            while (!dice.isEmpty()) {
                int roundScore = playRound();
                totalScore += roundScore;
            }
            
            return totalScore;
        }
        
        private int playRound() {
            // Check if there are any 3's
            long countOfThrees = dice.stream().filter(die -> die == TARGET_VALUE).count();
            
            if (countOfThrees > 0) {
                // Remove all 3's
                dice.removeIf(die -> die == TARGET_VALUE);
                return 0;
            } else {
                // Find and remove the lowest die
                int minValue = Collections.min(dice);
                dice.remove(Integer.valueOf(minValue));
                return minValue;
            }
        }
        
        private int rollDie() {
            return (int) (Math.random() * 6) + 1;
        }
    }
    
    public static class Simulation {
        private Map<Integer, Integer> scoreCounts;
        private int numSimulations;
        private int numDice;
        
        public Simulation(int numSimulations, int numDice) {
            this.numSimulations = numSimulations;
            this.numDice = numDice;
            this.scoreCounts = new HashMap<>();
        }
        
        public void runSimulation() {
            Game game = new Game();
            
            for (int i = 0; i < numSimulations; i++) {
                int score = game.playGame();
                scoreCounts.put(score, scoreCounts.getOrDefault(score, 0) + 1);
            }
        }
        
        public void printResults(long executionTime) {
            System.out.printf("Number of simulations was %d using %d dice.%n", 
                            numSimulations, numDice);
            
            // Sort scores for consistent output
            List<Integer> sortedScores = new ArrayList<>(scoreCounts.keySet());
            Collections.sort(sortedScores);
            
            for (int score : sortedScores) {
                int count = scoreCounts.get(score);
                double percentage = (double) count / numSimulations;
                System.out.printf("Total %d occurs %.2f occurred %.1f times.%n", 
                                score, percentage, (double) count);
            }
            
            System.out.printf("Total simulation took %.1f seconds.%n", 
                            executionTime / 1000.0);
        }
    }
} 