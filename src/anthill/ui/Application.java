/**
 *
 */
package anthill.ui;

import javax.swing.JFrame;

import anthill.controllers.Anthills;

/**
 * @author Seldan
 *
 */
public class Application extends JFrame {

    private static final long serialVersionUID = 6424756108494662748L;

    public Application() {
        this.add(new GraphicEngine());
        this.setResizable(false);
        this.pack();
        this.setTitle("Fourmiliere 1.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        Anthills._getInstance().initQueens();
    }
}
