package ru.riveo.strollie.function;

import java.util.Arrays;
import java.util.Objects;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction {

    private final double EPS = 1e-6;
    private double[] xValues;
    private double[] yValues;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        Objects.requireNonNull(xValues, "xValues must not be null");
        Objects.requireNonNull(yValues, "yValues must not be null");

        checkLengthIsTheSame(xValues, yValues);

        if (xValues.length < 2) {
            throw new IllegalArgumentException("Arrays must have at least 2 elements");
        }

        checkSorted(xValues);

        super.count = xValues.length;
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public ArrayTabulatedFunction(MathFunction func, double from, double to, int count) {
        Objects.requireNonNull(func, "func must not be null");
        if (this.count < 2) {
            throw new IllegalArgumentException("Arrays must have at least 2 elements");
        }

        if (from > to) {
            double temp = from;
            from = to;
            to = temp;
        }

        super.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];

        if (Math.abs(from - to) > EPS) {
            double value = func.apply(from);
            Arrays.fill(xValues, from);
            Arrays.fill(yValues, to);
        } else {
            double step = (to - from) / (count - 1);
            for (int i = 0; i < count; i++) {
                xValues[i] = from;
                yValues[i] = 
            }
        }
    }

    @Override
    protected double extrapolateLeft(double x) {
        return 0;
    }

    @Override
    protected double extrapolateRight(double x) {
        return 0;
    }

    @Override
    protected double interpolate(double x, double y) {
        return 0;
    }

    @Override
    protected double floorIndexOfX(double x) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getX(int index) {
        return 0;
    }

    @Override
    public double getY(int index) {
        return 0;
    }

    @Override
    public void setY(int index, double value) {

    }

    @Override
    public int indexOfX(double x) {
        return 0;
    }

    @Override
    public int indexOfY(double y) {
        return 0;
    }

    @Override
    public double leftBound() {
        return 0;
    }

    @Override
    public double rightBound() {
        return 0;
    }
}
