package com.example.bigbrainacademy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import static java.util.Arrays.asList;
import androidx.appcompat.app.AppCompatActivity;

import com.ibm.icu.text.RuleBasedNumberFormat;


// model for the written math minigame
// TODO: throw exceptions for invalid cases
public class WrittenMath extends AbstractComputeGame {
    private String input;
    private String answer;
    private String problem;
    private Random rng;
    private WrittenMathScreen view;
    private ArrayList<ArrayList<EqnTemplate>> diffTemplates;

    // idea for using templates of random generation, store operand limits + operators
    private class EqnTemplate {
        public ArrayList<Integer> ints;
        public ArrayList<MathOperators> ops;
        EqnTemplate() {
            ints = new ArrayList<>();
            ops = new ArrayList<>();
        }
        EqnTemplate(List<Integer> operands, List<MathOperators> ops) {
            ints = new ArrayList<>();
            this.ops = new ArrayList<>();
            ints.addAll(operands);
            this.ops.addAll(ops);
        }
    }

    public WrittenMath(WrittenMathScreen v) {
        super();
        input = "";
        rng = new Random(123); // setting same seed each time for consistent results
        view = v;
        diffTemplates = new ArrayList<>();
        for (int i = 0; i <= DifficultyLevel.S_ELITE.getValue(); ++i) {
            diffTemplates.add(new ArrayList<>());
            setTemplates(i, diffTemplates.get(i));
        }
        generateProblem(); // generate first problem
    }

    // TODO: make this better, div needs better generation
    private void setTemplates(int diff_level, ArrayList<EqnTemplate> list) {

        if (diff_level == DifficultyLevel.SPROUT.getValue()) {
            // basic single addition
            list.add(new EqnTemplate(asList(10,5), asList(MathOperators.ADD)));
        }
        else if (diff_level == DifficultyLevel.BEGIN.getValue()) {
            // basic addition and subtraction
            list.add(new EqnTemplate(asList(15,15), asList(MathOperators.ADD)));
            list.add(new EqnTemplate(asList(15,15), asList(MathOperators.SUB)));
        }
        else if (diff_level == DifficultyLevel.INTER.getValue()) {
            // basic addition and subtraction and 3 num addition, mult and div
            list.add(new EqnTemplate(asList(20,20), asList(MathOperators.ADD)));
            list.add(new EqnTemplate(asList(15,15), asList(MathOperators.SUB)));
            list.add(new EqnTemplate(asList(10,10,10), asList(MathOperators.ADD, MathOperators.ADD)));
            list.add(new EqnTemplate(asList(10,8), asList(MathOperators.MULT)));
            list.add(new EqnTemplate(asList(8,8), asList(MathOperators.DIV)));

        }
        else if (diff_level == DifficultyLevel.ADVANCE.getValue()) {
            // basic addition and subtraction, 3 num addition/subtraction, mult and division
        }
        else if (diff_level == DifficultyLevel.ELITE.getValue()) {

        }
        else {

        }
    }

    public void addDigit(char c) {
        input = input + c;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String s) {
        input = s;
    }

    public String getProblem() {
        return problem;
    }

    // makes problem and returns a formatted string, sets answer after generating problem
    public String generateProblem() {
        // TODO: make algorithm for generating problems ADD REMAINDERS, add support for solving for operands
        EqnTemplate values = getValues();
        answer = calculateAnswer(values);
        problem = getEquationString(values);
        return problem;
    }

    private String getEquationString(EqnTemplate template) {
        String s = convertIntoWords(template.ints.get(0)) + " ";
        // TODO: add support for MOD
        for (int i = 0; i < template.ops.size(); ++i) {
            switch (template.ops.get(i)) {
                case DIV:
                    s += view.getResources().getString(R.string.written_math_div);
                    break;
                case SUB:
                    s += view.getResources().getString(R.string.written_math_sub);
                    break;
                case MULT:
                    s += view.getResources().getString(R.string.written_math_mult);
                    break;
                default:
                    s += view.getResources().getString(R.string.written_math_add);
                    break;
            }
            s += " " + convertIntoWords(template.ints.get(i + 1));
            if (i + 1 < template.ops.size()) { // not last iteration
                s += " ";
            }
        }
        return s;
    }

    // chose a random template and generate the random values
    // use 2 or 3 step generation to ensure subtraction is not < 0, division has no remainder
    // TODO: set bottom limits (replace + 1 with something else)
    private EqnTemplate getValues() {
        int size = diffTemplates.get(diff).size();
        EqnTemplate template = diffTemplates.get(diff).get(rng.nextInt(size)); // randomly select template
        EqnTemplate ret_val = new EqnTemplate();
        ret_val.ints.add(rng.nextInt(template.ints.get(0)) + 1); // randomly generate 1st number
        for (int i = 0; i < template.ops.size(); ++i) {
            ret_val.ops.add(template.ops.get(i));
            if (template.ops.get(i) == MathOperators.SUB) {
                ret_val.ints.add(rng.nextInt(ret_val.ints.get(i)) + 1); // bound ith number to the max of (i-1)th
            }
            else if (template.ops.get(i) == MathOperators.DIV) {
                // generate random number bound by divisor, round to closest divisor
                ret_val.ints.add(rng.nextInt(template.ints.get(i + 1)) + 1);
                //TODO: make rounding better
                if (ret_val.ints.get(i) % ret_val.ints.get(i + 1) != 0) {
                    int divisor = ret_val.ints.get(i + 1);
                    while (ret_val.ints.get(i) % divisor != 0) {
                        --divisor;
                    }
                    ret_val.ints.set(i + 1, divisor);
                }
            }
            else {
                ret_val.ints.add(rng.nextInt(template.ints.get(i)) + 1);
            }

        }
        /*
        for (int i : template.ints) {
            ret_val.ints.add(rng.nextInt(i + 1));
        }
        */
        return ret_val;
    }

    private String calculateAnswer(EqnTemplate template) {
        int ans = template.ints.get(0);
        for (int i = 0; i < template.ints.size() - 1; ++i) {
            switch (template.ops.get(i)) {
                case ADD:
                    ans += template.ints.get(i + 1);
                    break;
                case SUB:
                    ans -= template.ints.get(i + 1);
                    break;
                case MULT:
                    ans *= template.ints.get(i + 1);
                    break;
                case DIV:
                    ans /= template.ints.get(i + 1);
                    break;
                case MOD:
                    ans %= template.ints.get(i + 1);
                    break;
                default:
                    // throw exception here
                    break;
            }
        }
        return String.valueOf(ans);
    }

    private String convertIntoWords(int str) {
        Locale local = new Locale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(str);
    }

    /*
    // generate limits for operands based on difficulty and operator(s)
    // TODO: make more advanced cases for operand limits
    private void getOperandLimits(ArrayList<Integer> ints, ArrayList<MathOperators> ops) {
        // resize ints arraylist to proper size
        int numOperands = ops.size() + 1;
        for (int i = 0; i < numOperands; ++i) {
            ints.add(0); // placeholder
        }
        int operand_idx = 0;
        for (MathOperators op : ops) {
            // only addition of 2 ints should exist for this level
            // TODO: add constants for operand limits
            if (diff == DifficultyLevel.SPROUT) {
                ints.set(operand_idx, 5);
                ints.set(operand_idx + 1, 4);
            }
            // only add/sub of 2 ints should exist for this level
            else if (diff == DifficultyLevel.BEGIN) {
                ints.set(operand_idx, 10);
                ints.set(operand_idx + 1, 9);
            }
            // add/sub of 2 ints, add/sub of 3 ints
            else if (diff == DifficultyLevel.INTER) {
                if (ops.size() == 2) {
                    switch ()
                }
                else {

                }
            }
            else if (diff == DifficultyLevel.ADVANCE) {

            }
            else if (diff == DifficultyLevel.ELITE) {

            }
            else {

            }
            operand_idx += 2;
        }
        // replace all operand limits with random values
        for (int i = 0; i < ints.size(); ++i)  {
            ints.set(i, rng.nextInt(ints.get(i) + 1));
        }
    }

    // generate 1 operator based on diff, add second depending on condition and rng
    // (currently will add extra operator 1/3rd of the time)
    private void getOperator(ArrayList<MathOperators> list) {
        ArrayList<MathOperators> l = new ArrayList<>();
        switch (diff) {
            case S_ELITE:
            case ELITE:
                l.add(MathOperators.MOD);
            case ADVANCE:
                l.add(MathOperators.MULT);
                l.add(MathOperators.DIV);
            case INTER:
            case BEGIN:
                l.add(MathOperators.SUB);
            default:
                l.add(MathOperators.ADD);
        }
        list.add(l.get(rng.nextInt(l.size())));
        if (diff.getValue() >= DifficultyLevel.INTER.getValue() && rng.nextInt(3) == 0){
            switch (list.get(0)) {
                case MULT:
                case DIV:
                    l.add(MathOperators.MULT);
                case ADD:
                case SUB:
                    l.add(MathOperators.ADD);
                    l.add(MathOperators.SUB);
                default:
                    break;
            }
            list.add(l.get(rng.nextInt(l.size())));
        }
    }
    */

    public boolean isContinue() {
        return (answer.charAt(input.length() - 1) == input.charAt(input.length() - 1));
    }

    public boolean isCorrect() {
        boolean wasRight = answer.equals(input);
        if (wasRight || !isContinue()) {
            incDifficulty(wasRight);
        }
        return wasRight;
    }


}
