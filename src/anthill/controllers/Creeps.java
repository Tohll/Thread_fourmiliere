package anthill.controllers;

import java.util.ArrayList;
import java.util.List;

import anthill.threads.RunnableHolder;

public class Creeps {

    private static class SingletonHolder {
        private static final Creeps INSTANCE = new Creeps();
    }

    public static Creeps _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final ArrayList<RunnableHolder> peons = new ArrayList<>();
    private final ArrayList<RunnableHolder> queens = new ArrayList<>();
    private final ArrayList<RunnableHolder> soldiers = new ArrayList<>();
    private final ArrayList<RunnableHolder> predators = new ArrayList<>();

    private Creeps() {

    }

    public List<RunnableHolder> _getPeons() {
        return this.peons;
    }

    public List<RunnableHolder> _getQueens() {
        return this.queens;
    }

    public List<RunnableHolder> _getSoldiers() {
        return this.soldiers;
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }

    public List<RunnableHolder> _getPredators() {
        return predators;
    }

}
