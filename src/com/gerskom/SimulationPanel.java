package com.gerskom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {
    public final Grid map;
    public double[][] tmpData;
    public boolean started = false;

    private static final int THREADS = 1;
    private static final int XTASKS = 1;
    private static final int YTASKS = 1;
    private final int deltaTime = 1;
    public long firstTime;
    public long secondTime;
    private double maxDiff = 0;
    private double minDiff = Integer.MAX_VALUE;
    private final ArrayList<Double> diffs = new ArrayList<>();
    private final ArrayList<Double> fps = new ArrayList<>();
    public int i = 0;
    public Graphics2D g2D;

    public static final Color wallColor = new Color(54, 54, 54);
    public static final Color bgColor = new Color(80, 80, 80);

    public SimulationPanel (Grid grid) {
        super();
        this.map = grid;
        dataConversion();

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(new MainInputHandler(this));
        this.addMouseMotionListener(new MainInputHandler(this));
        this.addKeyListener(new MainInputHandler(this));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g.create();

        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                if(map.wallTable[x][y] != Grid.wall) {
                    int color = (int)(tmpData[x][y] * 210) + 25;
                    if(color > 255) color = 255;
                    g2D.setColor(new Color(color, color, color));
                }
                else if (map.wallTable[x][y] == Grid.wall)
                    g2D.setColor(wallColor);
                else
                    g2D.setColor(bgColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }
        g2D.dispose();
    }

    public void startTheParallelGasSimulation(){
        started = true;
        Timer timer;

        int taskSizeX = (int)Math.ceil((double)((map.width - 1) - 1) / XTASKS);
        int taskSizeY = (int)Math.ceil((double)((map.height - 1) - 1) / YTASKS);

        GridTaskMaker sim = new GridTaskMaker(map, 0, map.width - 1, 0, map.height - 1);

        timer = new Timer(deltaTime, e -> {
            firstTime = System.nanoTime();
            i++;

            dataConversion();
            sim.iteration();

            /*ExecutorService executor = Executors.newFixedThreadPool(THREADS);
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 1);
                    executor.execute(worker);
                }
            }
            //if(Grid.dT != 1 || Grid.tau != 1) {
                for (int i = 0; i < XTASKS; i++) {
                    for (int j = 0; j < YTASKS; j++) {
                        Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 2);
                        executor.execute(worker);
                    }
                }
            //}
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 3);
                    executor.execute(worker);
                }
            }
            executor.shutdown();*/
            if((i % 4) == 0)
                repaint();

            secondTime = System.nanoTime();
        });
        timer.start();
    }

    public void exportImage(String fileName) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(map.width, map.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2D = bufferedImage.createGraphics();

        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                if(tmpData[x][y] != -1) {
                    int color = (int)(tmpData[x][y] * 255);
                    g2D.setColor(new Color(color, color, color));
                }
                else
                    g2D.setColor(wallColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }
        g2D.dispose();

        String formatName = "bmp";
        File file;

        if (i != 0)
            file = new File("output/" + fileName + "_" + i + "." + formatName);
        else
            file = new File("output/test_gas." + formatName);

        ImageIO.write(bufferedImage, formatName, file);
    }

    private void dataConversion() {
        this.tmpData = new double[map.width][map.height];
        for(int i = 0; i < map.width; i++)
            for(int j = 0; j < map.height; j++) {
                //if(map.cellTable[i][j][1][0] != -1)
                    tmpData[i][j] = 0;
            }

        for(int x = 1; x < map.width - 1; x++)
            for(int y = 1; y < map.height - 1; y++) {
                for(int i = 0; i < 3; i++)
                    for (int j = 0; j < 4; j++) {
                        if(map.cellTable[x][y][1][0] >= 0)
                            tmpData[x][y - 1] = map.cellTable[x][y][1][0];
                        if(map.cellTable[x][y][1][1] >= 0)
                            tmpData[x + 1][y] = map.cellTable[x][y][1][1];
                        if(map.cellTable[x][y][1][2] >= 0)
                            tmpData[x][y + 1] = map.cellTable[x][y][1][2];
                        if(map.cellTable[x][y][1][3] >= 0)
                            tmpData[x - 1][y] = map.cellTable[x][y][1][3];
                        if(map.wallTable[x][y] == -1)
                            tmpData[x][y] = -1;
                    }
            }
    }

    public int countParticles() {
        int counter = 0;

        for(int i = 0; i < map.width; i++)
            for(int j = 0; j < map.height; j++)
                if(tmpData[i][j] >= 0)
                    counter++;

       return counter;
    }
    public float countTotalC() {
        float counter = 0;

        for(int i = 0; i < map.width; i++)
            for(int j = 0; j < map.height; j++)
                if(tmpData[i][j] >= 0)
                    counter += tmpData[i][j];

        return counter;
    }

    public int countParticles(int xp, int xk, int yp, int yk) {
        int counter = 0;

        for(int i = xp; i < xk; i++)
            for(int j = yp; j < yk; j++)
                if(tmpData[i][j] == 1)
                    counter++;

        return counter;
    }

    public void fpsCounter() {
        double diff = secondTime - firstTime;
        double fps = (1 / (diff / 1000000000D));
        if(maxDiff <= diff) maxDiff = diff;
        else if(minDiff > diff) minDiff = diff;
        System.out.println("\nFRAME: " + i +
                "\nbefore: " + firstTime + "\tafter: " + secondTime +
                "\ndelay: " + deltaTime + "ms" +
                "\ndiff: " + new DecimalFormat("##.###").format(diff / 1000000D) + "ms" +
                "\nFPS: " + new DecimalFormat("###.##").format(fps) +
                "\nmax diff: " + new DecimalFormat("##.###").format(maxDiff / 1000000D) + "ms" +
                "\nmin diff: " + new DecimalFormat("##.###").format(minDiff / 1000000D) + "ms");
        averageResults(diff, fps);
    }

    public void averageResults(double newDiff, double newFPS) {
        //int n = 5;
        double diffAverage = 0;
        double fpsAverage = 0;
        diffs.add(newDiff);
        fps.add(newFPS);
        for(int i = 0; i < diffs.size(); i++) {
            diffAverage += diffs.get(i) / diffs.size();
            fpsAverage += fps.get(i) / fps.size();
        }
        System.out.println("average diff: " + new DecimalFormat("##.###").format(diffAverage / 1000000D)
                + "ms" + "\naverage FPS: " + new DecimalFormat("###.##").format(fpsAverage));
    }
}
