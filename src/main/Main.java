package main;

import java.awt.EventQueue;

import anthill.ui.Application;

public class Main {

    public static void main(final String[] args) {

        EventQueue.invokeLater(() -> {
            final Application app = new Application();
            app.setVisible(true);
        });
    }
}
