package xf.xflp.opt.ga;

import xf.xflp.base.XFLPModel;
import xf.xflp.opt.XFLPBase;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class SingleContainerGAPacker extends XFLPBase {

    private static final int MAX_GENERATIONS = 10;
    private static final int MAX_INDIVIDUALS = 10;
    private SingleContainerParameterPacker packer = new SingleContainerParameterPacker();

    private Random rand = new Random(1234);

    @Override
    public void execute(XFLPModel model) {
        Individual[] population = getKickoffParameter(model);

        // Evaluation
        evaluation(model, population);
        Individual bestIndividual = null;
        bestIndividual = checkForBest(bestIndividual, population);

        // Selection
        population = select(population, true, 4, rand);

        for (int generationIdx = 0; generationIdx < MAX_GENERATIONS; generationIdx++) {
            // Recombination
            population = uniformCrossover(population);
            // Evaluation
            evaluation(model, population);
            bestIndividual = checkForBest(bestIndividual, population);

            // Selection
            population = select(population, true, 4, rand);
        }

        packer.executeFinally(model, bestIndividual.getParameter());
    }

    private Individual[] uniformCrossover(Individual[] selection) {
        Individual[] population = new Individual[MAX_INDIVIDUALS];
        for (int i = 0; i < population.length; i++) {
            int a = rand.nextInt(selection.length);
            int b;
            do {
                b = rand.nextInt(selection.length);
            }while(b == a);

            population[i] = join(selection[a], selection[b]);
        }

        return population;
    }

    private Individual join(Individual a, Individual b) {
        Individual newIndividual = new Individual();
        ItemParameter[] parameter = new ItemParameter[a.getParameter().length];
        for (int j = 0; j < parameter.length; j++) {
            parameter[j] = new ItemParameter();
            if(rand.nextFloat() < 0.5) {
                parameter[j].setOrientation(a.getParameter()[j].getOrientation());
                parameter[j].setStrategy(a.getParameter()[j].getStrategy());
            } else {
                parameter[j].setOrientation(b.getParameter()[j].getOrientation());
                parameter[j].setStrategy(b.getParameter()[j].getStrategy());
            }
        }
        newIndividual.setParameter(parameter);

        if(rand.nextFloat() < 0.5) {
            for (int j = 0; j < parameter.length; j++) {
                newIndividual.getParameter()[j].setIndex(a.getParameter()[j].getIndex());
            }
        } else {
            for (int j = 0; j < parameter.length; j++) {
                newIndividual.getParameter()[j].setIndex(b.getParameter()[j].getIndex());
            }
        }

        return newIndividual;
    }

    private Individual[] getKickoffParameter(XFLPModel model) {
        Individual[] population = new Individual[MAX_INDIVIDUALS];
        int[] indexTemplate = IntStream.range(0, model.getItems().length).toArray();
        for (int i = 0; i < population.length; i++) {
            Individual newIndividual = new Individual();
            ItemParameter[] parameter = new ItemParameter[model.getItems().length];
            int[] newIndex = Arrays.copyOf(indexTemplate, indexTemplate.length);
            shuffleArray(newIndex);
            for (int j = 0; j < parameter.length; j++) {
                parameter[j] = new ItemParameter();
                parameter[j].setIndex(newIndex[j]);
                parameter[j].setOrientation(Orientation.values()[rand.nextInt(Orientation.values().length)]);
                parameter[j].setStrategy(Strategy.values()[rand.nextInt(Strategy.values().length)]);
            }
            newIndividual.setParameter(parameter);
            population[i] = newIndividual;
        }

        return population;
    }

    private void evaluation(XFLPModel model, Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            float fitness = packer.execute(model, population[i].getParameter());
            population[i].setFitness(fitness);
        }
    }

    private Individual checkForBest(Individual bestIndividual, Individual[] population) {
        Individual maxIndividual = population[0];
        for (int i = 0; i < population.length; i++) {
            if(population[i].getFitness() > maxIndividual.getFitness()) {
                maxIndividual = population[i];
            }
        }

        if(bestIndividual == null || maxIndividual.getFitness() > bestIndividual.getFitness())
            return maxIndividual;
        return bestIndividual;
    }

    private Individual[] select(Individual[] population,
                              boolean naturalFitnessScores,
                              int selectionSize,
                              Random rand) {

        double[] cumulativeFitnesses = new double[population.length];
        cumulativeFitnesses[0] = getAdjustedFitness(population[0].getFitness(), naturalFitnessScores);
        for (int i = 1; i < population.length; i++) {
            double fitness = getAdjustedFitness(
                    population[i].getFitness(),
                    naturalFitnessScores
            );
            cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + fitness;
        }

        Individual[] selection = new Individual[selectionSize];
        for (int i = 0; i < selectionSize; i++) {
            double randomFitness = rand.nextDouble() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
            int index = Arrays.binarySearch(cumulativeFitnesses, randomFitness);
            if (index < 0)
            {
                // Convert negative insertion point to array index.
                index = Math.abs(index + 1);
            }
            selection[i] = population[index];
        }
        return selection;
    }


    private double getAdjustedFitness(double rawFitness,
                                      boolean naturalFitness) {
        if (naturalFitness) {
            return rawFitness;
        } else {
            // If standardised fitness is zero we have found the best possible
            // solution.  The evolutionary algorithm should not be continuing
            // after finding it.
            return rawFitness == 0 ? Double.POSITIVE_INFINITY : 1 / rawFitness;
        }
    }

    private void shuffleArray(int[] array) {
        int index, temp;
        for (int i = array.length - 1; i > 0; i--) {
            index = rand.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

}
