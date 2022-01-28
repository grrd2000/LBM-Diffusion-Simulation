package com.gerskom;

import java.awt.event.*;
import java.io.IOException;

public class MainInputHandler implements KeyListener, MouseListener, MouseMotionListener {
    SimulationPanel simulationPanel;
    private static int mouseButton = -1;
    private static int opt = 1;

    private final float brushSize = 50f;
    private final float brushDensity = 25f;

    public MainInputHandler(SimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '1' -> {
                simulationPanel.map.initComputeArea();
            }
            case 'r' -> {
                simulationPanel.map.initRandomCells();
                simulationPanel.repaint();
            }
            case 'f' -> {
                try {
                    simulationPanel.exportImage("lattice_gas");
                    System.out.println("\nFRAME " + simulationPanel.i + " EXPORTED");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            case 'p' -> {
                //System.out.println("Number of particles: " + simulationPanel.countParticles());
                System.out.println("Total concentration: " + simulationPanel.countTotalC());
            }
            case 'o' -> {
                simulationPanel.fpsCounter();
            }
            case't' -> {
                if(opt == 1) opt = 0;
                else opt = 1;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (e.getButton()) {
            case 1 -> {
                mouseButton = 1;
                if(opt == 1)
                    simulationPanel.map.addWallSquare(x, y, 10);
                else
                    simulationPanel.map.removeWallSquare(x, y, 10);
            }
            case 2 -> {
                mouseButton = 2;
                //simulationPanel.map.removeSquareOfParticles(x, y, 100);
                simulationPanel.map.initCell(x, y);
            }
            case 3 -> {
                mouseButton = 3;
                if(opt == 1)
                    simulationPanel.map.addSquareOfParticles(x, y, 50, 0.95f);
                else
                    simulationPanel.map.addBrushOfParticles(x, y, brushSize, brushDensity);
            }
        }
        simulationPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (mouseButton) {
            case 1 -> {
                if(opt == 1)
                    simulationPanel.map.addWallSquare(x, y, 10);
                else
                    simulationPanel.map.removeWallSquare(x, y, 10);
            }
            case 2 -> simulationPanel.map.removeSquareOfParticles(x, y, 100);
            case 3 ->  {
                if(opt == 1)
                    simulationPanel.map.addSquareOfParticles(x, y, 100, 0.95f);
                else
                    simulationPanel.map.addBrushOfParticles(x, y, brushSize, brushDensity);
            }

        }
        simulationPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
