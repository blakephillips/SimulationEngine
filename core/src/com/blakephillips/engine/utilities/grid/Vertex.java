package com.blakephillips.engine.utilities.grid;

import java.util.Objects;

public class Vertex {

    public int x;
    public int y;
    public boolean obstacle = false;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (!(obj instanceof Vertex)) { return false; }

        Vertex v = (Vertex) obj;

        return v.x == x && v.y == y;
    }

    @Override
    public String toString() {
        return String.format("(%x, %x)", x, y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
