package com.example.bigbrainacademy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class HeavyweightGame extends AbstractThinkGame {

    public enum ScaleStatus {LEANING_LEFT, BALANCED, LEANING_RIGHT}
    public enum Handedness {LEFT, RIGHT}

    private ArrayList<WeightedObject> weightedObjects; //index 0 is heaviest, rest arbitrary (probably needs better imp.)
    private ArrayList<Scale> scales;

    private int input;

    private Random rand;

    public HeavyweightGame() {
        weightedObjects = new ArrayList<>();
        scales = new ArrayList<>();

        rand = new Random();

        generateProblem();
    }

    public void generateProblem() {
        //Reset
        weightedObjects = new ArrayList<>();
        scales = new ArrayList<>();
        input = -1;

        switch (this.diff) {
            case 0: {
                Scale scale = new Scale();
                WeightedObject heaviest = new WeightedObject(2, 0);
                WeightedObject lightest = new WeightedObject(1, 1);
                weightedObjects.add(heaviest);
                weightedObjects.add(lightest);

                Handedness heavySide = randomSide();
                scale.addWeightedObject(heaviest, heavySide);
                scale.addWeightedObject(lightest, oppositeSide(heavySide));
                scales.add(scale);
                break;
            }
            case 1: {
                int layout = rand.nextInt(1);
                if (layout == 0) {
                    // One scale, H on one side and l1, l2 on other
                    Scale scale = new Scale();
                    WeightedObject heaviest = new WeightedObject(3, 0);
                    WeightedObject light1 = new WeightedObject(1, 1);
                    WeightedObject light2 = new WeightedObject(1, 2);
                    weightedObjects.add(heaviest);
                    weightedObjects.add(light1);
                    weightedObjects.add(light2);

                    Handedness heavySide = randomSide();
                    scale.addWeightedObject(light1, heavySide);
                    scale.addWeightedObject(light2, heavySide);
                    scale.addWeightedObject(heaviest, oppositeSide(heavySide));
                    scales.add(scale);
                } else {
                    // Two scales, max 1 object per side. Objects H, l1, l2
                    Scale scale1 = new Scale();
                    Scale scale2 = new Scale();

                    //WeightedObject heaviest = new WeightedObject()
                }
                break;
            }
            case 2: {
                int layout = rand.nextInt(1);
                if (layout == 0) {

                } else {

                }
                break;
            }
        }
    }

    private Handedness randomSide() {
        int n = rand.nextInt(1);
        if (n == 0) return Handedness.LEFT;
        return Handedness.RIGHT;
    }

    private Handedness oppositeSide(Handedness side) {
        if (side == Handedness.LEFT) return Handedness.RIGHT;
        return Handedness.LEFT;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public boolean isCorrect() {
        boolean wasRight = input == 0;
        // incDifficulty(wasRight);
        calcScore(wasRight);
        return wasRight;
    }

    @Override
    protected void calcScore(boolean wasRight) {
        //TODO: impl
    }

    public int getNumScales() { return scales.size(); }

    public ScaleStatus getScaleStatus(int scaleIndex) {
        return scales.get(scaleIndex).getScaleStatus();
    }

    public ArrayList<Integer> getLeftObjectIds(int scaleIndex) {
        return scales.get(scaleIndex).getObjectIds(Handedness.LEFT);
    }

    public ArrayList<Integer> getRightObjectIds(int scaleIndex) {
        return scales.get(scaleIndex).getObjectIds(Handedness.RIGHT);
    }

    private class WeightedObject {
        private int weight;
        private int id;
        public WeightedObject(int weight, int id) {
            this.weight = weight;
            this.id = id;
        }

        public int getWeight() { return weight; }

        public int getId() { return id; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WeightedObject that = (WeightedObject) o;
            return weight == that.weight && id == that.id;
        }
    }

    private class Scale {
        private ScaleStatus scaleStatus;
        private int weightImbalance; // negative when leaning left, positive for the right
        private ArrayList<WeightedObject> leftPan;
        private ArrayList<WeightedObject> rightPan;

        public Scale() {
            weightImbalance = 0;
            leftPan = new ArrayList<>();
            rightPan = new ArrayList<>();
        }

        /**
         * Adds a WeightedObject to the given side and recalibrates the scale's balance
         * @param object WeightedObject, the object to add to the scale
         * @param side Handedness, the side to add the object to
         */
        public void addWeightedObject(WeightedObject object, Handedness side) {
            if (side == Handedness.LEFT) {
                leftPan.add(object);
                weightImbalance += object.getWeight();
            }
            else {
                rightPan.add(object);
                weightImbalance -= object.getWeight();
            }

            switch (weightImbalance / Math.abs(weightImbalance)) {
                case -1: scaleStatus = ScaleStatus.LEANING_LEFT;
                case 0: scaleStatus = ScaleStatus.BALANCED;
                case 1: scaleStatus = ScaleStatus.LEANING_RIGHT;
            }
        }

        public ScaleStatus getScaleStatus() { return scaleStatus; }

        public ArrayList<Integer> getObjectIds(Handedness side) {
            ArrayList<Integer> idList = new ArrayList<>();
            if (side.equals(Handedness.LEFT)) {
                for (WeightedObject object : leftPan) {
                    idList.add(object.getId());
                }
            }
            else {
                for (WeightedObject object : rightPan) {
                    idList.add(object.getId());
                }
            }
            return idList;
        }
    }


}
