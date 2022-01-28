package com.gerskom;

import java.util.Random;

public class Grid {
    public int width;
    public int height;
    public int nMax = 50000;

    public static final int borderSize = 10;

    public double[][][][] cellTable;
    public int[][] wallTable;
    public final static int wall = -1;

    public static final double dT = 1f;
    public static final double tau = 1f;
    public static final float initC = 1f;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellTable = new double[width][height][3][4];
        initCellsOfSimArea();
        this.wallTable = new int[width][height];
        initComputeArea();
    }

    public void initCell(int xCor, int yCor) {
        //if(xCor > borderSize && xCor < width - borderSize - 1 && yCor > borderSize && yCor < height - borderSize - 1) {
        if(wallTable[xCor][yCor] != wall) {
            cellTable[xCor][yCor][1][0] = initC;
            cellTable[xCor][yCor][1][1] = initC;
            cellTable[xCor][yCor][1][2] = initC;
            cellTable[xCor][yCor][1][3] = initC;
        }
    }

    public void initCell(int xCor, int yCor, float c) {
        if(xCor > borderSize && xCor < width - borderSize && yCor > borderSize && yCor < height - borderSize) {
            cellTable[xCor][yCor][1][0] = c;
            cellTable[xCor][yCor][1][1] = c;
            cellTable[xCor][yCor][1][2] = c;
            cellTable[xCor][yCor][1][3] = c;
        }
    }

    public void initWallCell(int xCor, int yCor) {
        removeParticle(xCor, yCor);
        removeParticle(xCor, yCor - 1);
        removeParticle(xCor + 1, yCor);
        removeParticle(xCor, yCor + 1);
        removeParticle(xCor - 1, yCor);
        wallTable[xCor][yCor] = wall;
    }

    public void removeWallCell(int xCor, int yCor) {
        wallTable[xCor][yCor] = 0;
    }

    public void addWallSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                initWallCell(x, y);
            }
    }

    public void removeWallSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                removeWallCell(x, y);
            }
    }

    public void addSquareOfParticles(int xCor, int yCor, int size, float c) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                initCell(x, y, c);
            }
    }

    public void addBrushOfParticles(int xCor, int yCor, float size, float density) {
        for (double r = 4; r <= size / 2; r += 1.5) {
            for (double a = 0; a < 2 * Math.PI; a += 0.05) {
                Random random = new Random();
                float rand = 100 * random.nextFloat();

                int x = (int)(r * Math.cos(a));
                int y = (int)(r * Math.sin(a));

                if (density >= rand)
                    if ((x + xCor) >= 0 && (x + xCor) <= width && (y + yCor) >= 0 && (y + yCor) <= height)
                        initCell(x + xCor, y + yCor);
            }
        }
    }

    private void removeParticle(int xCor, int yCor) {
        if(xCor > 0 && xCor < width && yCor > 0 && yCor < height) {
            if (wallTable[xCor][yCor] != wall) {
                cellTable[xCor][yCor][1][0] = 0;
                cellTable[xCor][yCor][1][1] = 0;
                cellTable[xCor][yCor][1][2] = 0;
                cellTable[xCor][yCor][1][3] = 0;
            }
        }
    }

    public void removeSquareOfParticles(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++)
                removeParticle(x, y);
    }

    public void initRandomCells() {
        for(int c = 0; c < nMax; c++) {
            Random ran = new Random();
            int x = ran.nextInt(width - 10) + 5;
            int y = ran.nextInt(height - 10) + 5;
            initCell(x, y);
        }
    }

    public void initComputeArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                if(x <= borderSize || x >= width - borderSize - 1 ||
                   y <= borderSize || y >= height - borderSize - 1) {
                    initWallCell(x, y);
                }
                else if(x < (width / 2) - 45) {
                    initCell(x, y);
                }
                else if(x == (width / 2) - 40 && (y <= (height / 2) - 35 || y >= (height / 2) + 35)) {
                    addWallSquare(x, y, 6);
                }
            }
        //addSquareOfParticles(width / 2, height / 2, 80, 1);
    }

    private void initCellsOfSimArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                for(int i = 0; i < 3; i++)
                    for (int j = 0; j < 4; j++)
                        cellTable[x][y][i][j] = 0;
            }
    }
}