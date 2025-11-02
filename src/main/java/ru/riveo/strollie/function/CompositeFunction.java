package ru.riveo.strollie.function;

public class CompositeFunction implements MathFunction {

    MathFunction f;
    MathFunction g;

    public CompositeFunction(MathFunction f, MathFunction g) {
        this.f = f;
        this.g = g;
    }

    @Override
    public double apply(double x) {
        return g.apply(f.apply(x));
    }

}