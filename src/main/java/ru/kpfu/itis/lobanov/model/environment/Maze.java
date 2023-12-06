package ru.kpfu.itis.lobanov.model.environment;

import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.Placement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze implements Serializable {
    private final Cell[][] data = new Cell[GameSettings.MAZE_SIZE][GameSettings.MAZE_SIZE];
    private final Random random = new Random();
    private final List<Cell> exits = new ArrayList<>();

    public Cell[][] getData() {
        return data;
    }

    public Maze() {
        final int FIRST_ELEM = 0;
        final int SECOND_ELEM = 1;

        generateCells();
        generateEmptyCells();
        Cell currentCell = data[1][1];
        currentCell.setChecked(true);

        Cell[] currentCellNeighbours;
        Cell[] path = new Cell[GameSettings.MAZE_SIZE * GameSettings.MAZE_SIZE];
        int indexOfPath = 0;

        do {
            currentCellNeighbours = currentCell.getNeighboursToGo(this);
            while (sizeOfArray(currentCellNeighbours) != 0) {
                path[indexOfPath] = currentCell;
                indexOfPath++;

                Cell randomNeighbour = getRandomNeighbour(currentCellNeighbours);

                Cell wallBetween = getWallBetween(currentCell, randomNeighbour);

                go(randomNeighbour, wallBetween);

                currentCell = randomNeighbour;
                currentCellNeighbours = currentCell.getNeighboursToGo(this);
            }

            indexOfPath--;
            currentCell = path[indexOfPath];

        } while (currentCell != data[1][1]);

        Cell[][] verticalWalls = getVerticalSideWalls();
        Cell[][] horizontalWalls = getHorizontalSideWalls();

        Cell[] leftWall = verticalWalls[FIRST_ELEM];
        Cell[] rightWall = verticalWalls[SECOND_ELEM];
        Cell[] upperWall = horizontalWalls[FIRST_ELEM];
        Cell[] lowerWall = horizontalWalls[SECOND_ELEM];

        setExit(leftWall);
        setExit(rightWall);
        setExit(upperWall);
        setExit(lowerWall);
    }

    private void generateCells() {
        for (int i = 0; i < GameSettings.MAZE_SIZE; i++) {
            for (int j = 0; j < GameSettings.MAZE_SIZE; j++) {
                data[i][j] = new Cell(i, j, true);
            }
        }
    }

    private void generateEmptyCells() {
        final int BEGIN_INDEX = 1;
        final int DISTANCE = 2;

        for (int i = BEGIN_INDEX; i < GameSettings.MAZE_SIZE; i += DISTANCE) {
            for (int j = BEGIN_INDEX; j < GameSettings.MAZE_SIZE; j += DISTANCE) {
                data[i][j].setWall(false);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return data[x][y];
    }

    public int labyrinthLength() {
        return GameSettings.MAZE_SIZE;
    }

    private void go(Cell randomEmptyNeighbour, Cell wallNeighbour) {
        randomEmptyNeighbour.setChecked(true);
        wallNeighbour.setWall(false);
        wallNeighbour.setChecked(true);
    }

    private Cell getRandomNeighbour(Cell[] cells) {
        return cells[random.nextInt(sizeOfArray(cells))];
    }

    private Cell getWallBetween(Cell currentCell, Cell randomNeighbour) {
        int xOfNeighbour = randomNeighbour.getX();
        int yOfNeighbour = randomNeighbour.getY();
        int xOfCurrent = currentCell.getX();
        int yOfCurrent = currentCell.getY();

        int xIndexOfWall = xOfCurrent;
        int yIndexOfWall = yOfCurrent;

        if (xOfNeighbour > xOfCurrent) {
            xIndexOfWall++;
        } else if (xOfNeighbour < xOfCurrent) {
            xIndexOfWall--;
        }
        if (yOfNeighbour > yOfCurrent) {
            yIndexOfWall++;
        } else if (yOfNeighbour < yOfCurrent) {
            yIndexOfWall--;
        }
        return data[xIndexOfWall][yIndexOfWall];
    }

    private int sizeOfArray(Cell[] cells) {
        int count = 0;
        for (Cell cell : cells) {
            if (cell != null) {
                count++;
            }
        }
        return count;
    }

    private Cell[][] getVerticalSideWalls() {
        Cell[] rightWall = new Cell[GameSettings.MAZE_SIZE];
        Cell[] leftWall = new Cell[GameSettings.MAZE_SIZE];

        for (int i = 0; i < GameSettings.MAZE_SIZE; i++) {
            leftWall[i] = data[i][0];
            data[i][0].setSideWall(true);
            rightWall[i] = data[i][GameSettings.MAZE_SIZE - 1];
            data[i][GameSettings.MAZE_SIZE - 1].setSideWall(true);
        }
        return new Cell[][]{leftWall, rightWall};
    }

    private Cell[][] getHorizontalSideWalls() {
        Cell[] upperWall = data[0];
        Cell[] lowerWall = data[GameSettings.MAZE_SIZE - 1];
        for (int i = 0; i < GameSettings.MAZE_SIZE; i++) {
            data[0][i].setSideWall(true);
            data[GameSettings.MAZE_SIZE - 1][i].setSideWall(true);
        }
        return new Cell[][]{upperWall, lowerWall};
    }

    private void setExit(Cell[] cells) {
        while (true) {
            int index;
            do {
                index = random.nextInt(cells.length);
            } while (index == 0 || index == GameSettings.MAZE_SIZE - 1);
            Cell cell = cells[index];
            Cell[] neighbours = cell.getEmptyNeighbours(this);
            if (sizeOfArray(neighbours) > 0) {
                cell.setWall(false);
                exits.add(cell);
                break;
            }
        }
    }

    public Cell[] getCellsForPlacement(Placement placement) {
        int xStartIndex;
        int xEndIndex;
        int yStartIndex;
        int yEndIndex;
        switch (placement) {
            case CENTER:
                xStartIndex = GameSettings.MAZE_SIZE * 3 / 8; // SIZE / 2 - SIZE / 8
                xEndIndex = GameSettings.MAZE_SIZE * 5 / 8; // SIZE / 2 + SIZE / 8
                yStartIndex = GameSettings.MAZE_SIZE * 3 / 8;
                yEndIndex = GameSettings.MAZE_SIZE * 5 / 8;
                return getCellsInRange(xStartIndex, xEndIndex, yStartIndex, yEndIndex);
            case UPPER_LEFT_CORNER:
                xStartIndex = 1;
                xEndIndex = 1 + GameSettings.MAZE_SIZE / 8;
                yStartIndex = 1;
                yEndIndex = 1 + GameSettings.MAZE_SIZE / 8;
                return getCellsInRange(xStartIndex, xEndIndex, yStartIndex, yEndIndex);
            case UPPER_RIGHT_CORNER:
                xStartIndex = GameSettings.MAZE_SIZE * 7 / 8 - 2; // SIZE - 2 - SIZE / 8
                xEndIndex = GameSettings.MAZE_SIZE - 2;
                yStartIndex = 1;
                yEndIndex = 1 + GameSettings.MAZE_SIZE / 8;
                return getCellsInRange(xStartIndex, xEndIndex, yStartIndex, yEndIndex);
            case LOWER_LEFT_CORNER:
                xStartIndex = 1;
                xEndIndex = 1 + GameSettings.MAZE_SIZE / 8;
                yStartIndex = GameSettings.MAZE_SIZE * 7 / 8 - 2; // SIZE - 2 - SIZE / 8
                yEndIndex = GameSettings.MAZE_SIZE - 2;
                return getCellsInRange(xStartIndex, xEndIndex, yStartIndex, yEndIndex);
            case LOWER_RIGHT_CORNER:
                xStartIndex = GameSettings.MAZE_SIZE * 7 / 8 - 2; // SIZE - 2 - SIZE / 8
                xEndIndex = GameSettings.MAZE_SIZE - 2;
                yStartIndex = GameSettings.MAZE_SIZE * 7 / 8 - 2;
                yEndIndex = GameSettings.MAZE_SIZE - 2;
                return getCellsInRange(xStartIndex, xEndIndex, yStartIndex, yEndIndex);
        }
        return null;
    }

    private Cell[] getCellsInRange(int xStartIndex, int xEndIndex, int yStartIndex, int yEndIndex) {
        Cell[] cellsForPlacement = new Cell[(xEndIndex - xStartIndex + 1) * (yEndIndex - yStartIndex + 1)];
        int index = 0;
        for (int x = xStartIndex; x <= xEndIndex; x++) {
            for (int y = yStartIndex; y <= yEndIndex; y++) {
                cellsForPlacement[index] = data[x][y];
                index++;
            }
        }
        return cellsForPlacement;
    }

    public List<Bonus> generateBonuses(double pacmanX, double pacmanY) {
        List<Bonus> bonuses = new ArrayList<>();
        Cell[] cellsForUpperLeftCorner = getCellsForPlacement(Placement.UPPER_LEFT_CORNER);
        Cell[] cellsForUpperRightCorner = getCellsForPlacement(Placement.UPPER_RIGHT_CORNER);
        Cell[] cellsForLowerLeftCorner = getCellsForPlacement(Placement.LOWER_LEFT_CORNER);
        Cell[] cellsForLowerRightCorner = getCellsForPlacement(Placement.LOWER_RIGHT_CORNER);

        bonuses.add(getBonusFromPlacement(cellsForUpperLeftCorner, pacmanX, pacmanY));
        bonuses.add(getBonusFromPlacement(cellsForUpperRightCorner, pacmanX, pacmanY));
        bonuses.add(getBonusFromPlacement(cellsForLowerLeftCorner, pacmanX, pacmanY));
        bonuses.add(getBonusFromPlacement(cellsForLowerRightCorner, pacmanX, pacmanY));

        return bonuses;
    }

    private Bonus getBonusFromPlacement(Cell[] cellsForPlacement, double pacmanX, double pacmanY) {
        while (true) {
            int index = random.nextInt(cellsForPlacement.length);
            Cell cell = cellsForPlacement[index];
            double cellX = cell.getX() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
            double cellY = cell.getY() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;

            if (!cell.isWall() && (cellX != pacmanX || cellY != pacmanY)) {
                return new Bonus(cellX, cellY, GameSettings.BONUS_SCORES);
            }
        }
    }

    public List<Pellet> generatePellets(double pacmanX, double pacmanY, List<Bonus> bonuses) {
        List<Pellet> pellets = new ArrayList<>();
        int beginIndex = 1;

        for (int x = beginIndex; x < GameSettings.MAZE_SIZE - 1; x++) {
            for (int y = beginIndex; y < GameSettings.MAZE_SIZE - 1; y++) {
                double cellX = x * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
                double cellY = y * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
                Bonus tempBonus = new Bonus(cellX, cellY, GameSettings.BONUS_SCORES);
                if (!data[x][y].isWall() && !bonuses.contains(tempBonus) && (cellX != pacmanX || cellY != pacmanY)) {
                    pellets.add(new Pellet(cellX - GameSettings.CELL_SIZE / 2, cellY - GameSettings.CELL_SIZE / 2, GameSettings.PELLET_SCORES));
                }
            }
        }
        return pellets;
    }

    public List<Rectangle> getWalls() {
        List<Rectangle> walls = new ArrayList<>();
        for (int x = 0; x < GameSettings.MAZE_SIZE; x++) {
            for (int y = 0; y < GameSettings.MAZE_SIZE; y++) {
                if (data[x][y].isWall()) {
                    Rectangle rectangle = new Rectangle(x * GameSettings.CELL_SIZE, y * GameSettings.CELL_SIZE, GameSettings.CELL_SIZE, GameSettings.CELL_SIZE);
                    walls.add(rectangle);
                }
            }
        }
        return walls;
    }

    public Cell getUpperExit() {
        for (Cell cell : exits) {
            if (cell.getY() == 0) {
                return cell;
            }
        }
        return null;
    }

    public Cell getLowerExit() {
        for (Cell cell : exits) {
            if (cell.getY() == GameSettings.MAZE_SIZE - 1) {
                return cell;
            }
        }
        return null;
    }

    public Cell getLeftExit() {
        for (Cell cell : exits) {
            if (cell.getX() == 0) {
                return cell;
            }
        }
        return null;
    }

    public Cell getRightExit() {
        for (Cell cell : exits) {
            if (cell.getX() == GameSettings.MAZE_SIZE - 1) {
                return cell;
            }
        }
        return null;
    }
}
