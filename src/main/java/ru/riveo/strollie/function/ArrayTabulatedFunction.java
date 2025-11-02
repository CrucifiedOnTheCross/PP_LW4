package ru.riveo.strollie.function;

import java.util.Objects;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction {

    private final double EPS = 1e-6;
    private final double[] xValues;
    private final double[] yValues;

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
        if (count < 2) {
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
        double step = (to - from) / (count - 1);
        double current = from;
        for (int i = 0; i < count; i++) {
            xValues[i] = current;
            yValues[i] = func.apply(current);
            current += step;
        }
    }

    @Override
    protected double extrapolateLeft(double x) {
        double x0 = xValues[0];
        double y0 = yValues[0];
        double x1 = xValues[1];
        double y1 = yValues[1];
        double slope = (y1 - y0) / (x1 - x0);
        return y0 + (x - x0) * slope;
    }

    @Override
    protected double extrapolateRight(double x) {
        int n = this.count;
        double x0 = xValues[n - 2];
        double y0 = yValues[n - 2];
        double x1 = xValues[n - 1];
        double y1 = yValues[n - 1];
        double slope = (y1 - y0) / (x1 - x0);
        return y1 + (x - x1) * slope;
    }

    @Override
    protected int floorIndexOfX(double x) {
        int n = count;
        if (x < xValues[0]) {
            return -1;
        }
        if (x > xValues[n - 1]) {
            return n - 1;
        }
        int l = 0;
        int r = n - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (xValues[m] <= x) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return r;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= this.count) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= this.count) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= this.count) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        int l = 0;
        int r = count - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            double xm = xValues[m];
            if (xm == x) {
                return m;
            } else if (xm < x) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (yValues[i] == y) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }
}
