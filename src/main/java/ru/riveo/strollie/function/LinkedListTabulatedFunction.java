package ru.riveo.strollie.function;

import java.util.Objects;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {

    private Node head;
    private Node tail;
    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        Objects.requireNonNull(xValues, "xValues must not be null");
        Objects.requireNonNull(yValues, "yValues must not be null");

        checkLengthIsTheSame(xValues, yValues);
        checkCountX(xValues.length);
        checkSorted(xValues);

        super.count = xValues.length;

        Node prev = null;
        for (int i = 0; i < xValues.length; i++) {
            Node node = new Node(xValues[i], yValues[i]);
            if (prev == null) {
                head = node;
            } else {
                prev.next = node;
                node.prev = prev;
            }
            prev = node;
        }
        tail = prev;
    }

    public LinkedListTabulatedFunction(MathFunction func, double from, double to, int count) {
        Objects.requireNonNull(func, "func must not be null");
        checkCountX(count);
        if (from > to) {
            double tmp = from;
            from = to;
            to = tmp;
        }

        super.count = count;
        double step = (to - from) / (count - 1);
        double current = from;
        Node prev = null;
        for (int i = 0; i < count; i++) {
            Node node = new Node(current, func.apply(current));
            if (prev == null) {
                head = node;
            } else {
                prev.next = node;
                node.prev = prev;
            }
            prev = node;
            current += step;
        }
        tail = prev;
    }

    private Node nodeAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node cur;
        int i;
        if (index <= count / 2) {
            cur = head;
            i = 0;
            while (i < index) {
                cur = cur.next;
                i++;
            }
        } else {
            cur = tail;
            i = count - 1;
            while (i > index) {
                cur = cur.prev;
                i--;
            }
        }
        return cur;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public double getX(int index) {
        return nodeAt(index).x;
    }

    @Override
    public double getY(int index) {
        return nodeAt(index).y;
    }

    @Override
    public void setY(int index, double value) {
        nodeAt(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.x == x) return i;
            i++;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.y == y) return i;
            i++;
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return head.x;
    }

    @Override
    public double rightBound() {
        return tail.x;
    }

    @Override
    protected double extrapolateLeft(double x) {
        double x0 = head.x;
        double y0 = head.y;
        double x1 = head.next.x;
        double y1 = head.next.y;
        double slope = (y1 - y0) / (x1 - x0);
        return y0 + (x - x0) * slope;
    }

    @Override
    protected double extrapolateRight(double x) {
        double x0 = tail.prev.x;
        double y0 = tail.prev.y;
        double x1 = tail.x;
        double y1 = tail.y;
        double slope = (y1 - y0) / (x1 - x0);
        return y1 + (x - x1) * slope;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < head.x) {
            return -1;
        }
        if (x > tail.x) {
            return count - 1;
        }
        if (x == tail.x) {
            return count - 1;
        }
        int i = 0;
        Node cur = head;
        while (cur.next != null) {
            if (cur.x <= x && x < cur.next.x) {
                return i;
            }
            cur = cur.next;
            i++;
        }
        return count - 2;
    }

    @Override
    public void insert(double x, double y) {
        if (count == 0) {
            Node node = new Node(x, y);
            head = tail = node;
            count = 1;
            return;
        }

        int floor = floorIndexOfX(x);

        if (floor == -1) {
            // insert at head
            Node node = new Node(x, y);
            node.next = head;
            head.prev = node;
            head = node;
            count++;
            return;
        }

        Node left = nodeAt(floor);
        if (left.x == x) {
            // replace existing value
            left.y = y;
            return;
        }

        Node node = new Node(x, y);
        if (floor == count - 1) {
            // append at tail
            tail.next = node;
            node.prev = tail;
            tail = node;
            count++;
            return;
        }

        Node right = left.next;
        left.next = node;
        node.prev = left;
        node.next = right;
        right.prev = node;
        count++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (count <= 2) {
            throw new IllegalStateException("Cannot remove: tabulated function must have at least 2 points");
        }
        Node target = nodeAt(index);
        Node prev = target.prev;
        Node next = target.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
        count--;
    }

    private static class Node {
        double x;
        double y;
        Node prev;
        Node next;

        Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}