package ru.riveo.strollie.function;

import ru.riveo.strollie.exception.ArrayIsNotSortedException;
import ru.riveo.strollie.exception.DifferentLenghtOfArraysException;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    protected int count;

    public static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLenghtOfArraysException("Arrays must have the same length");
        }
    }

    public static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new ArrayIsNotSortedException("Values must be ordered");
            }
        }
    }

    protected void checkCountX(int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Arrays must have at least 2 elements");
        }
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                int floor = floorIndexOfX(x);
                return interpolate(x, floor);
            }
        }
    }


    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract int floorIndexOfX(double x);

    protected double interpolate(double x, int floorIndex) {
        double x0 = getX(floorIndex);
        double x1 = getX(floorIndex + 1);
        double y0 = getY(floorIndex);
        double y1 = getY(floorIndex + 1);
        return interpolate(x, x0, x1, y0, y1);
    }

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        double slope = (rightY - leftY) / (rightX - leftX);
        return leftY + (x - leftX) * slope;
    }

}
