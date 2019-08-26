package anthill.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.controllers.Creeps;
import anthill.controllers.FoodSpots;
import anthill.food_spots.FoodSpot;
import anthill.threads.RunnableHolder;
import anthill.utils.Configuration;
import anthill.utils.SpriteInfos;

public class GraphicEngine extends JPanel implements Runnable {

    private static final long serialVersionUID = -7273880090886312807L;
    private ImageIcon bush;
    private final Font defaultFont;
    private final int delay;
    private final boolean hasToDisplay;
    private final Random rand;
    private SpriteInfos[] sceneryArray;
    private ImageIcon shadow;
    private final Font smallDefaultFont;
    private int totalAlivePeons;
    private int totalAliveSoldiers;
    private final Color uiBackgroundColor;

    public GraphicEngine() {
        this.defaultFont = new Font("Arial", Font.BOLD, 18);
        this.smallDefaultFont = new Font("Arial", Font.BOLD, 11);
        this.totalAlivePeons = 0;
        this.totalAliveSoldiers = 0;
        this.uiBackgroundColor = new Color(0, 0, 0, 0.5f);
        this.delay = 25;
        this.rand = new Random();
        this.hasToDisplay = true;
        this.initSceneryAndShadows(Configuration.SCENERY_DENSITY);
        this.setBackground(new Color(169, 150, 25));
        this.setPreferredSize(new Dimension(Configuration.SQUARE_SIDE, Configuration.SQUARE_SIDE));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread animator;
        animator = new Thread(this);
        animator.start();
    }

    private void drawAnthills(final Graphics g) {
        final Point anthillPosition = Anthills._getInstance()._getPosition();
        g.drawImage(Anthills._getInstance().getSprite(), anthillPosition.x - (Anthills._getInstance()._getHeight() / 2),
                anthillPosition.y - (Anthills._getInstance()._getHeight() / 2), Anthills._getInstance()._getHeight(),
                Anthills._getInstance()._getHeight(), this);
    }

    private void drawFoodSpots(final Graphics g) {
        g.setColor(Color.WHITE);
        for (final FoodSpot foodSpot : FoodSpots._getInstance()._getFoodSpots()) {
            if (foodSpot._isDiscovered()) {
                final Font font = this.smallDefaultFont;
                g.setFont(font);
                g.drawString(String.format("Food : %d", foodSpot._getFoodStock()),
                        (foodSpot._getPosition().x - (foodSpot._getWidth() / 2) - 5),
                        (foodSpot._getPosition().y - (foodSpot._getHeight() / 2) - 5));
                g.drawImage(FoodSpots._getInstance()._getSprite(), foodSpot._getPosition().x - foodSpot._getWidth() / 2,
                        foodSpot._getPosition().y - foodSpot._getHeight() / 2, foodSpot._getWidth(),
                        foodSpot._getHeight(), this);
            }
        }
    }

    private void drawLifeBar(final Graphics g, final int width, final int height, final int x, final int y,
            final RunnableHolder creep) {
        g.setColor(this.uiBackgroundColor);
        g.fillRect(x, y, width, height);
        final double div = (double) (creep._getRunnable()._getLife()) / (double) (creep._getRunnable()._getMaxLife());
        final double lifePercentage = 100d * div;
        if (lifePercentage > 50) {
            g.setColor(Color.GREEN);
        } else if (lifePercentage > 25) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.RED);
        }
        final double aFloat = width / 100d;
        final double bFloat = aFloat * lifePercentage;
        final int aInt = (int) (this.round(bFloat, 2));
        g.fillRect(x, y, aInt, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    private void drawPeons(final Graphics g) {
        this.totalAlivePeons = 0;
        g.setColor(Color.DARK_GRAY);
        final int r = 6;
        int x;
        int y;
        final ArrayList<RunnableHolder> peonsArray = new ArrayList<>();
        peonsArray.addAll(Creeps._getInstance()._getPeons());
        for (final Iterator<RunnableHolder> iterator = peonsArray.iterator(); iterator.hasNext();) {
            final RunnableHolder peon = iterator.next();
            if (peon._getRunnable()._isAlive()) {
                this.totalAlivePeons++;
                if (!peon._getRunnable()._isUnderground()) {
                    x = peon._getRunnable()._getPosition().x;
                    y = peon._getRunnable()._getPosition().y;
                    x = x - (r / 2);
                    y = y - (r / 2);
                    g.fillOval(x, y, r, r);
                }
            }
        }
    }

    private void drawPredators(final Graphics g) {
        final int r = 14;
        int x;
        int y;
        final ArrayList<RunnableHolder> predatorsArray = new ArrayList<>();
        predatorsArray.addAll(Creeps._getInstance()._getPredators());
        for (final Iterator<RunnableHolder> iterator = predatorsArray.iterator(); iterator.hasNext();) {
            final RunnableHolder predator = iterator.next();
            if (predator._getRunnable()._isAlive() && !predator._getRunnable()._isUnderground()) {
                g.setColor(Color.RED);
                x = predator._getRunnable()._getPosition().x;
                y = predator._getRunnable()._getPosition().y;
                x = x - (r / 2);
                y = y - (r / 2);
                g.fillOval(x, y, r, r);
                if (predator._getRunnable()._getLife() < predator._getRunnable()._getMaxLife()) {
                    this.drawLifeBar(g, r, 7, x, y - 9, predator);
                }
            }
        }
    }

    private void drawScenery(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        final AffineTransform backup = g2d.getTransform();
        for (final SpriteInfos spriteInfos : this.sceneryArray) {
            g2d.setTransform(AffineTransform.getRotateInstance(spriteInfos._getRotate(), spriteInfos._getPosition().x,
                    spriteInfos._getPosition().y));
            g2d.drawImage(this.bush.getImage(), spriteInfos._getPosition().x - (spriteInfos._getWidth() / 2),
                    spriteInfos._getPosition().y - (spriteInfos._getWidth() / 2),
                    spriteInfos._getWidth() < 1 ? 2 : spriteInfos._getWidth(),
                    spriteInfos._getWidth() < 1 ? 2 : spriteInfos._getWidth(), this);
        }
        g2d.setTransform(backup);
    }

    private void drawShadows(final Graphics g) {
        g.drawImage(this.shadow.getImage(), 0, 0, Configuration.SQUARE_SIDE, Configuration.SQUARE_SIDE, this);
    }

    private void drawSoldiers(final Graphics g) {
        this.totalAliveSoldiers = 0;
        g.setColor(Color.BLACK);
        final int r = 9;
        int x;
        int y;
        final ArrayList<RunnableHolder> soldiersArray = new ArrayList<>();
        soldiersArray.addAll(Creeps._getInstance()._getSoldiers());
        for (final Iterator<RunnableHolder> iterator = soldiersArray.iterator(); iterator.hasNext();) {
            final RunnableHolder soldier = iterator.next();
            if (soldier._getRunnable()._isAlive()) {
                this.totalAliveSoldiers++;
                if (!soldier._getRunnable()._isUnderground()) {
                    x = soldier._getRunnable()._getPosition().x;
                    y = soldier._getRunnable()._getPosition().y;
                    x = x - (r / 2);
                    y = y - (r / 2);
                    g.fillOval(x, y, r, r);
                }
            }
        }
    }

    private void drawUI(final Graphics g) {
        final Point anthillPosition = Anthills._getInstance()._getPosition();
        final int anthillSideSize = Configuration.SQUARE_SIDE / 10;
        g.setColor(this.uiBackgroundColor);
        g.fillRect(15, 20, 122, 50);
        final Font font = this.defaultFont;
        g.setFont(font);
        g.setColor(Color.WHITE);
        final int x = anthillPosition.x - (anthillSideSize / 2);
        final int y = anthillPosition.y - (anthillSideSize / 2);
        g.drawString(String.format("Food : %d", Anthills._getInstance()._getFoodMonitor()._getFoodStock()), x, y - 8);
        g.drawString(String.format("Soldiers : %d", this.totalAliveSoldiers), 20, 40);
        g.drawString(String.format("Peons : %d", this.totalAlivePeons), 20, 62);
        this.drawLifeBar(g, anthillSideSize, 6, x, y, Anthills._getInstance()._getQueen());
    }

    /**
     * Generate randomly placed scenery items, and initialize the shadow sprite
     *
     * @param nbrOfItems : the number of items to be generated
     */
    private void initSceneryAndShadows(final int nbrOfItems) {
        this.sceneryArray = new SpriteInfos[nbrOfItems];
        this.bush = new ImageIcon("img/Aloevera.png");
        this.shadow = new ImageIcon("img/shadow.png");
        for (int i = 0; i < nbrOfItems; i++) {
            Point position = new Point(this.rand.nextInt(Configuration.SQUARE_SIDE) + 1,
                    this.rand.nextInt(Configuration.SQUARE_SIDE) + 1);
            while (Collisions._getInstance()._isPointInObjectRange(Anthills._getInstance(), position)) {
                position = new Point(this.rand.nextInt(Configuration.SQUARE_SIDE) + 1,
                        this.rand.nextInt(Configuration.SQUARE_SIDE) + 1);
            }
            this.sceneryArray[i] = new SpriteInfos(this.rand.nextInt(360) + 1f, this.rand.nextInt(40), position);
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.drawScenery(g);
        this.drawAnthills(g);
        this.drawPeons(g);
        this.drawPredators(g);
        this.drawSoldiers(g);
        this.drawFoodSpots(g);
        this.drawShadows(g);
        this.drawUI(g);
    }

    private double round(final double value, final int precision) {
        final int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public void run() {
        long beforeTime;
        long timeDiff;
        long sleep;
        beforeTime = System.currentTimeMillis();
        while (this.hasToDisplay) {
            this.repaint();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = this.delay - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (final InterruptedException e) {
                final String msg = String.format("Thread interrupted: %s", e.getMessage());
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                Thread.currentThread().interrupt();
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}
