package com.gerskom;

public record GridTaskMaker(Grid map, int xp, int xk, int yp, int yk) {

    public void iteration() {
        for(int x = xp; x < xk; x++)
            for(int y = yp; y < yk; y++) {
                if(map.wallTable[x][y] != Grid.wall) {
                    map.cellTable[x][y][0][0] = map.cellTable[x][y - 1][1][2];
                    map.cellTable[x][y][0][1] = map.cellTable[x + 1][y][1][3];
                    map.cellTable[x][y][0][2] = map.cellTable[x][y + 1][1][0];
                    map.cellTable[x][y][0][3] = map.cellTable[x - 1][y][1][1];

                    double C = map.cellTable[x][y][0][0] + map.cellTable[x][y][0][1] + map.cellTable[x][y][0][2] + map.cellTable[x][y][0][3];
                    map.cellTable[x][y][2][0] = C / 4d;
                    map.cellTable[x][y][2][1] = C / 4d;
                    map.cellTable[x][y][2][2] = C / 4d;
                    map.cellTable[x][y][2][3] = C / 4d;

                    map.cellTable[x][y][1][0] = map.cellTable[x][y][2][0];
                    map.cellTable[x][y][1][1] = map.cellTable[x][y][2][1];
                    map.cellTable[x][y][1][2] = map.cellTable[x][y][2][2];
                    map.cellTable[x][y][1][3] = map.cellTable[x][y][2][3];

                    if(map.wallTable[x][y - 1] == Grid.wall && map.wallTable[x + 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][2] += map.cellTable[x][y][1][0];
                        map.cellTable[x][y][1][3] += map.cellTable[x][y][1][1];
                        map.cellTable[x][y][1][0] = 0;
                        map.cellTable[x][y][1][1] = 0;
                    }
                    else if(map.wallTable[x][y + 1] == Grid.wall && map.wallTable[x + 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][0] += map.cellTable[x][y][1][0];
                        map.cellTable[x][y][1][3] += map.cellTable[x][y][1][1];
                        map.cellTable[x][y][1][2] = 0;
                        map.cellTable[x][y][1][1] = 0;
                    }
                    else if(map.wallTable[x][y + 1] == Grid.wall && map.wallTable[x - 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][0] += map.cellTable[x][y][1][0];
                        map.cellTable[x][y][1][1] += map.cellTable[x][y][1][1];
                        map.cellTable[x][y][1][2] = 0;
                        map.cellTable[x][y][1][3] = 0;
                    }
                    else if(map.wallTable[x][y - 1] == Grid.wall && map.wallTable[x - 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][2] += map.cellTable[x][y][1][0];
                        map.cellTable[x][y][1][1] += map.cellTable[x][y][1][1];
                        map.cellTable[x][y][1][0] = 0;
                        map.cellTable[x][y][1][3] = 0;
                    }
                    else if(map.wallTable[x][y - 1] == Grid.wall) {
                        map.cellTable[x][y][1][2] += map.cellTable[x][y][1][0];
                        map.cellTable[x][y][1][0] = 0;
                    }
                    else if(map.wallTable[x + 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][3] += map.cellTable[x][y][1][1];
                        map.cellTable[x][y][1][1] = 0;
                    }
                    else if(map.wallTable[x][y + 1] == Grid.wall) {
                        map.cellTable[x][y][1][0] += map.cellTable[x][y][1][2];
                        map.cellTable[x][y][1][2] = 0;
                    }
                    else if(map.wallTable[x - 1][y] == Grid.wall) {
                        map.cellTable[x][y][1][1] += map.cellTable[x][y][1][3];
                        map.cellTable[x][y][1][3] = 0;
                    }
                }
            }
    }
}
