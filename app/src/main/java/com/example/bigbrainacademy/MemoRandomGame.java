package com.example.bigbrainacademy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MemoRandomGame extends AbstractMemorizeGame {

    private final Random rand;

    // One integer per image to memorize, value corresponds to index of image in image list
    private List<Integer> answerSetImagesIndices;
    // List of which images player must memorize have been hidden, corresponds to answerSetImagesIndices
    private List<Integer> answerSetHiddenIndices;
    // List of which images the player can click on to solve problem, corresponds to index of images in image list
    private List<Integer> givenOptionImagesIndices;

    // The grid that contains the memo icons
    private MemorizeGrid memorizeGrid;

    public MemoRandomGame() {
        super();
        rand = new Random();
        memorizeGrid = new MemorizeGrid(true);
    }

    private class MemorizeGrid {
        private ArrayList<ArrayList<Integer>> grid;
        private ArrayList<Integer> options;
        private ArrayList<Coordinate> hiddenCoordinates;
        private int maxHeight;
        private int maxWidth;
        private int numHidden;
        private int totalIcons = 0; //needed?
        private int height;

        // Constructor for test purposes only
        public MemorizeGrid(boolean prototype) {
            grid = new ArrayList<>();
            options = new ArrayList<>();
            hiddenCoordinates = new ArrayList<>();
            ArrayList<Integer> arr = new ArrayList<>();
            arr.add(0);
            options.add(0);
            arr.add(1);
            options.add(1);
            grid.add(arr);
            height = 1;
            setHiddenCoordinates();
        }

        public MemorizeGrid() {
            this.maxHeight = maxRows();
            this.maxWidth = maxCols();
            this.numHidden = hiddenImageCount();
            this.options = new ArrayList<>();
            this.hiddenCoordinates = new ArrayList<>();

            generateGrid();
        }

        private void generateGrid() {





            setHiddenCoordinates();
        }

        /**
         * Depending on the difficulty level, determines the max vertical size of the problem
         * @return int, the maximum rows
         */
        private int maxRows() {
            switch(diff) {
                case 1: return 1;
                case 2: return 1;
                case 3: return 2;
                case 4: return 2;
                case 5: return 2;
                case 6: return 3;
            }
            throw new IllegalStateException();
        }

        /**
         * Depending on the difficulty level, determines the max horizontal size of the problem
         * @return int, the maximum columns
         */
        private int maxCols() {
            switch(diff) {
                case 0: return 2;
                case 1: return 3;
                case 2: return 3;
                case 3: return 4;
                case 4: return 4;
                case 5: return 5;
            }
            throw new IllegalStateException();
        }

        /**
         * Depending on the difficulty level, determines how many images get hidden
         * @return int, the number of hidden images
         */
        private int hiddenImageCount() {
            switch(diff) {
                case 1: return 1;
                case 2: return rand.nextInt(2) + 1;
                case 3: return 2;
                case 4: return 3;
                case 5: return rand.nextInt(2) + 3;
                case 6: return rand.nextInt(3) + 3;
            }
            throw new IllegalStateException();
        }

        public ArrayList<Integer> getDistinctAnsweringOptions() {
            return options;
        }

        public int getHeight() { return this.height; }
        public int getRowWidth(int rowIndex) { return this.grid.get(rowIndex).size(); }

        public ArrayList<Integer> getRow(int level) {
            if (level >= 0 && level < height) {
                return grid.get(level);
            }
            else {
                return null;
            }
        }

        private void setHiddenCoordinates() {
            //TODO: implement properly
            hiddenCoordinates.add(new Coordinate(0,0));
        }

        public ArrayList<Coordinate> getHiddenCoordinates() {
            return hiddenCoordinates;
        }
    }



    /**
     * Sets up the game state for a new problem depending on the difficulty
     */
    public void generateProblem() {
        memorizeGrid = new MemorizeGrid(true);
        givenOptionImagesIndices = memorizeGrid.getDistinctAnsweringOptions();
    }


    public int getHeight() { return this.memorizeGrid.getHeight(); }

    public int getRowLength(int rowIndex) { return this.memorizeGrid.getRowWidth(rowIndex); }

    public ArrayList<Integer> getRow(int level) {
        if (level >= 0 && level < getHeight()) {
            return memorizeGrid.getRow(level);
        }
        else {
            return null;
        }
    }

    public ArrayList<Coordinate> getHiddenCoordinates() {
        return memorizeGrid.getHiddenCoordinates();
    }
}
