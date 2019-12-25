package com.example.seamcarving;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HorizontalBitmapGraph implements AStarGraph<Point> {

    private Bitmap picture;
    private Set<Point> seamPoints;

    public HorizontalBitmapGraph(Bitmap picture, Set<Point> seamPoints) {
        this.picture = picture;
        this.seamPoints = seamPoints;
    }

    public List<WeightedEdge<Point>> neighbors(Point p) {
        List<WeightedEdge<Point>> neighbors = new ArrayList<>();
        if (p.x() >= picture.getWidth() - 1) { // H
            neighbors.add(new WeightedEdge<>(p, new Point(picture.getWidth(), 0), 0));
            return neighbors;
        } else if (p.equals(new Point(-1, 0))) { // H root <
            add(neighbors);
        } else { // H <
            add(neighbors, p, new Point(p.x() + 1, p.y() + 1)); // se
            add(neighbors, p, new Point(p.x() + 1, p.y())); // e
            add(neighbors, p, new Point(p.x() + 1, p.y() - 1)); // ne
        }
        return neighbors;
    }

    private void add(List<WeightedEdge<Point>> neighbors) {
        Point from = new Point(-1, 0); // H <
        for (int i = 0; i < picture.getHeight(); i++) { // H
            Point to = new Point(0, i); // H
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    private void add(List<WeightedEdge<Point>> neighbors, Point from, Point to) {
        if (to.y() < picture.getHeight() && to.y() >= 0) { //H
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    public double estimatedDistanceToGoal(Point p, Point goal) {
        return 0;
    }

    private double energy(int x, int y) {
        if (x < 0 || x > picture.getWidth() - 1 || y < 0 || y > picture.getHeight() - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (seamPoints.contains(new Point(x, y)))
            return Double.MAX_VALUE;
        int north = y != 0 ? picture.getPixel(x, y - 1) : picture.getPixel(x, picture.getHeight() - 1);
        int south = y != picture.getHeight() - 1 ? picture.getPixel(x, y + 1) : picture.getPixel(x, 0);
        int east = x != picture.getWidth() - 1 ? picture.getPixel(x + 1, y) : picture.getPixel(0, y);
        int west = x != 0 ? picture.getPixel(x - 1, y) : picture.getPixel(picture.getWidth() - 1, y);
        int rx = Math.abs(Color.red(east) - Color.red(west));
        int gx = Math.abs(Color.green(east) - Color.green(west));
        int bx = Math.abs(Color.blue(east) - Color.blue(west));
        int ry = Math.abs(Color.red(north) - Color.red(south));
        int gy = Math.abs(Color.green(north) - Color.green(south));
        int by = Math.abs(Color.blue(north) - Color.blue(south));
        return Math.sqrt(p2(rx) + p2(gx) + p2(bx) + p2(ry) + p2(gy) + p2(by));
    }

    private double p2(double d) {
        return Math.pow(d, 2);
    }
}