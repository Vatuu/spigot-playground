package dev.vatuu.test.util;

public class Tuple<A, B> {

    private A left;
    private B right;

    public Tuple(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public void setLeft(A left) {
        this.left = left;
    }

    public B getRight() {
        return right;
    }

    public void setRight(B right) {
        this.right = right;
    }
}
