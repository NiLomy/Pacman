package ru.kpfu.itis.lobanov.model.environment;

public class Cell {
    private int x;
    private int y;
    private boolean isWall;
    private boolean isSideWall;
    private boolean isChecked;

    public Cell() {
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public Cell[] getNeighboursToGo(Maze maze) {
        Cell[] res = new Cell[4];
        int i = 0;
        Cell[][] cells = maze.getData();
        if (x + 2 < maze.labyrinthLength() && !cells[x + 2][y].isChecked()) {
            res[i] = cells[x + 2][y];
            i++;
        }
        if (y + 2 < maze.labyrinthLength() && !cells[x][y + 2].isChecked()) {
            res[i] = cells[x][y + 2];
            i++;
        }
        if (x - 2 > 0 && !cells[x - 2][y].isChecked()) {
            res[i] = cells[x - 2][y];
            i++;
        }
        if (y - 2 > 0 && !cells[x][y - 2].isChecked()) {
            res[i] = cells[x][y - 2];
        }
        return res;

    }

    public Cell[] getEmptyNeighbours(Maze maze) {
        Cell[] result = new Cell[4];
        int counter = 0;
        Cell neighbour;
        if (x + 1 < maze.labyrinthLength()) {
            neighbour = maze.getCell(x + 1, y);
            if (!neighbour.isWall()) {
                result[counter] = neighbour;
                counter++;
            }
        }
        if (y + 1 < maze.labyrinthLength()) {
            neighbour = maze.getCell(x, y + 1);
            if (!neighbour.isWall()) {
                result[counter] = neighbour;
                counter++;
            }
        }
        if (x - 1 >= 0) {
            neighbour = maze.getCell(x - 1, y);
            if (!neighbour.isWall()) {
                result[counter] = neighbour;
                counter++;
            }
        }
        if (y - 1 >= 0) {
            neighbour = maze.getCell(x, y - 1);
            if (!neighbour.isWall()) {
                result[counter] = neighbour;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "{" + isWall + "}";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public boolean isSideWall() {
        return isSideWall;
    }

    public void setSideWall(boolean sideWall) {
        isSideWall = sideWall;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
