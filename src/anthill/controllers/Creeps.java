package anthill.controllers;

import java.util.ArrayList;
import java.util.List;

import anthill.ants.AbsCreep;
import anthill.ants.Queen;
import anthill.threads.PredatorGenerator;
import anthill.threads.RunnableHolder;

public class Creeps {

    private static class SingletonHolder {
        private static final Creeps INSTANCE = new Creeps();
    }

    public static Creeps _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final ArrayList<RunnableHolder> peons = new ArrayList<>();
    private final ArrayList<RunnableHolder> predators = new ArrayList<>();
    private final ArrayList<RunnableHolder> queens = new ArrayList<>();
    private final ArrayList<RunnableHolder> soldiers = new ArrayList<>();

    private Creeps() {

    }

    public List<RunnableHolder> _getPeons() {
        return this.peons;
    }

    public List<RunnableHolder> _getPredators() {
        return this.predators;
    }

    public List<RunnableHolder> _getQueens() {
        return this.queens;
    }

    public List<RunnableHolder> _getSoldiers() {
        return this.soldiers;
    }

    public void _initQueensAndPredators() {
        final AbsCreep queen = new Queen(1, 50, Anthills._getInstance());
        final RunnableHolder queenThread = new RunnableHolder(queen, "Queen 1");
        Anthills._getInstance()._setQueen(queenThread);
        this.queens.add(queenThread);
        queenThread.start();
        final Thread predatorsGenerator = new Thread(new PredatorGenerator(), "Predator generator");
        predatorsGenerator.start();
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
