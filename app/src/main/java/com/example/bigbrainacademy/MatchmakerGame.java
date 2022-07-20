package com.example.bigbrainacademy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// TODO: implement
public class MatchmakerGame extends AbstractIdentifyGame {

    private final ArrayList<Integer> grid = new ArrayList<>();
    private int columns = 0;
    private int pairs = 0;
    private int currentPairs = 0;
    private final ArrayList<ArrayList<MatchmakerTemplate>> templates = new ArrayList<>();
    private final Random rng = new Random(123);

    public static class MatchmakerTemplate {
        int columns; // columns we would like to have, rows = grid.size() / columns (should ensure these are factors for rectangular grids)
        int pairs; // how many pairs there will be
        int size; // how many square the grid will contain
        // will probably need a field to tell the screen which group of images to use, can add that later, for now we will just use integers

        MatchmakerTemplate(int cols, int pairs, int size) {
            columns = cols;
            this.pairs = pairs;
            this.size = size;
        }

        public static void setTemplate(ArrayList<MatchmakerTemplate> templateList, int diffLevel) {

            if (diffLevel == DifficultyLevel.SPROUT.getValue()) {
                templateList.add(new MatchmakerTemplate(2, 1, 4));
            }

            else if (diffLevel == DifficultyLevel.BEGIN.getValue()) {
                templateList.add(new MatchmakerTemplate(3, 1, 6));
                templateList.add(new MatchmakerTemplate(4, 1, 8));
            }

            else if (diffLevel == DifficultyLevel.INTER.getValue()) {
                templateList.add(new MatchmakerTemplate(3, 1, 9));
                templateList.add(new MatchmakerTemplate(4, 1, 16));
            }

            else if (diffLevel == DifficultyLevel.ADVANCE.getValue()) {

            }

            else if (diffLevel == DifficultyLevel.ELITE.getValue()) {

            }

            else if (diffLevel == DifficultyLevel.S_ELITE.getValue()) {

            }

            else {
                // throw here for a difficulty level that is too high
                throw new IllegalArgumentException();
            }

        }
    }

    MatchmakerGame() {

        // initialize templates
        for (int i = 0; i <= DifficultyLevel.S_ELITE.getValue(); ++i) {
            templates.add(new ArrayList<>());
            MatchmakerTemplate.setTemplate(templates.get(i), i);
        }


    }

    private MatchmakerTemplate getRandomTemplate(int difficulty) {

        ArrayList<MatchmakerTemplate> indexTemplate = templates.get(difficulty);
        return indexTemplate.get(rng.nextInt(indexTemplate.size()));
    }

    // add up to max size of grid, shuffle grid, take only the first (size) elements
    private void fillGrid(int size) {

        final int maxSize = 30; // this should be placed somewhere else
        grid.clear(); // make sure grid is empty

        for (int i = 0; i < maxSize; ++i) {
            grid.add(i);
        }

        // shuffle and remove last (maxSize - size) elements, leaving only the first (size) elements
        Collections.shuffle(grid);
        for (int i = 0; i < maxSize - size; ++i) {
            grid.remove(grid.size() - 1);
        }

    }

    // returns if 2 positions are visually adjacent, although they can be logically adjacent
    // i.e beside each other in the grid but they wrap on the screen (this part needs work)
    private boolean notAdjacent(int x, int y) {
        // this part should be right but might need to be checked
        if (Math.abs(x - y) == 1) {
            if (x > y && x % columns == 0) {
                return true;
            }
            else if (y > x && y % columns == 0) {
                return true;
            }
        }
        return (Math.abs(x - y) != 1 && Math.abs(x - y) != columns);
    }

    // adds all pairs in indices that are not adjacent to each other (on the
    private void addPairs() {

        // array of all unused positions, needed to ensure rng doesnt select a position already used
        // in another pair. Need an arrayList for random access, cant use a set
        ArrayList<Integer> gridPositions = new ArrayList<>();
        for (int i = 0; i < grid.size(); ++i) {
            gridPositions.add(i);
        }

        // for the amount of pairs requested, get a random position, select all positions not equal to
        // or adjacent to the first position and chose a random second position from the group, set
        // the value of the second position to the first position and remove those positions from
        // the set of positions to chose from
        for (int i = 0; i < pairs; ++i) {

            int basePosition = gridPositions.get(rng.nextInt(gridPositions.size()));
            ArrayList<Integer> tempList = new ArrayList<>();

            for (int position : gridPositions) {

                if (basePosition != position && notAdjacent(basePosition, position)) {
                    tempList.add(position);
                }
            }

            int secondPosition = tempList.get(rng.nextInt(tempList.size()));
            grid.set(secondPosition, grid.get(basePosition));

            // remove selected positions from pool, note that since all values are unique there will
            // never be a repeat
            gridPositions.remove(Integer.valueOf(basePosition));
            gridPositions.remove(Integer.valueOf(secondPosition));
        }


    }

    // generates a new grid from the current difficulty level
    public ArrayList<Integer> generateGrid() {

        MatchmakerTemplate randomTemplate = getRandomTemplate(diff);

        columns = randomTemplate.columns;
        pairs = randomTemplate.pairs;
        currentPairs = 0; // must reset the amount of currently made pairs for the grid

        fillGrid(randomTemplate.size);
        addPairs();

        return grid;
    }


    public int getColumns() {
        return columns;
    }

    public int getPairCount() {
        return pairs;
    }

    public int getCurrentPairs() {
        return currentPairs;
    }

    public void pairMade() {
        ++currentPairs;
    }

    @Override
    protected void calcScore(boolean wasRight) {

        incDifficulty(wasRight);

        if (wasRight) {
            score += 10 + diff;
        }
        else {
            score -= (7 - diff);
        }

    }

}
