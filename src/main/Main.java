package main;

import java.awt.EventQueue;

import anthill1_0.ui.Application;

public class Main {

    public static void main(final String[] args) {

        EventQueue.invokeLater(() -> {
            final Application app = new Application();
            app.setVisible(true);
        });
    }
}
