package com.example.seamcarving;

import android.graphics.Bitmap;

import astar.AStarGraph;
import astar.WeightedEdge;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VerticalBitmapGraph {

    private Bitmap picture;

    public VerticalBitmapGraph(Bitmap picture) {
        this.picture = picture;
    }

    public List<WeightedEdge<Point>> neighbors(Point p) {
        List<WeightedEdge<Point>> neighbors = new ArrayList<>();
        if (p.y() >= picture.height() - 1) { // V
            neighbors.add(new WeightedEdge<>(p, new Point(0, picture.height()), 0)); // V
            return neighbors;
        } else if (p.equals(new Point(0, -1))) { // V root ^
            add(neighbors); // V
        } else { // V ^
            add(neighbors, p, new Point(p.x() + 1, p.y() + 1)); // se
            add(neighbors, p, new Point(p.x(), p.y() + 1)); // s
            add(neighbors, p, new Point(p.x() - 1, p.y() + 1)); // sw
        }
        return neighbors;
    }

    private void add(List<WeightedEdge<Point>> neighbors) {
        Point from = new Point(0, -1); // V
        for (int i = 0; i < picture.width(); i++) { // V
            Point to = new Point(i, 0); // V
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    private void add(List<WeightedEdge<Point>> neighbors, Point from, Point to) {
        if (to.x() < picture.width() && to.x() >= 0) { //V
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    public double estimatedDistanceToGoal(Point p, Point goal) {
        return 0;
    }

    private double energy(int x, int y) {
        if (x < 0 || x > picture.width() - 1 || y < 0 || y > picture.height() - 1) {
            System.out.println("\n" + picture.width() + " | x = " + x);
            System.out.println(picture.height() + " | y = " + y);
            throw new IndexOutOfBoundsException();
        }
        Color north = y != 0 ? picture.get(x, y - 1) : picture.get(x, picture.height() - 1);
        Color south = y != picture.height() - 1 ? picture.get(x, y + 1) : picture.get(x, 0);
        Color east = x != picture.width() - 1 ? picture.get(x + 1, y) : picture.get(0, y);
        Color west = x != 0 ? picture.get(x - 1, y) : picture.get(picture.width() - 1, y);
        int rx = Math.abs(east.getRed() - west.getRed());
        int gx = Math.abs(east.getGreen() - west.getGreen());
        int bx = Math.abs(east.getBlue() - west.getBlue());
        int ry = Math.abs(north.getRed() - south.getRed());
        int gy = Math.abs(north.getGreen() - south.getGreen());
        int by = Math.abs(north.getBlue() - south.getBlue());
        return Math.sqrt(p2(rx) + p2(gx) + p2(bx) + p2(ry) + p2(gy) + p2(by));
    }

    private double p2(double d) {
        return Math.pow(d, 2);
    }
}
