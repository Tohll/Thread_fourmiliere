package anthill.controllers;

import java.util.ArrayList;
import java.util.List;

import anthill.threads.RunnableHolder;

public class Ants {

    private static class SingletonHolder {
        private static final Ants INSTANCE = new Ants();
    }

    public static Ants _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final ArrayList<RunnableHolder> peons = new ArrayList<>();
    private final ArrayList<RunnableHolder> queens = new ArrayList<>();
    private final ArrayList<RunnableHolder> soldiers = new ArrayList<>();

    private Ants() {

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

}
