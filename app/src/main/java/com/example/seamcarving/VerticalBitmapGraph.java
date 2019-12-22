package com.example.seamcarving;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class VerticalBitmapGraph implements AStarGraph<Point> {

    private Bitmap picture;

    public VerticalBitmapGraph(Bitmap picture) {
        this.picture = picture;
    }

    public List<WeightedEdge<Point>> neighbors(Point p) {
        List<WeightedEdge<Point>> neighbors = new ArrayList<>();
        if (p.y() >= picture.getHeight() - 1) { // V
            neighbors.add(new WeightedEdge<>(p, new Point(0, picture.getHeight()), 0)); // V
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
        for (int i = 0; i < picture.getWidth(); i++) { // V
            Point to = new Point(i, 0); // V
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    private void add(List<WeightedEdge<Point>> neighbors, Point from, Point to) {
        if (to.x() < picture.getWidth() && to.x() >= 0) { //V
            neighbors.add(new WeightedEdge<>(from, to, energy((int) to.x(), (int) to.y())));
        }
    }

    public double estimatedDistanceToGoal(Point p, Point goal) {
        return 0;
    }

    private double energy(int x, int y) {
        if (x < 0 || x > picture.getWidth() - 1 || y < 0 || y > picture.getHeight() - 1) {
            System.out.println(picture.getWidth() + " | x = " + x);
            System.out.println(picture.getHeight() + " | y = " + y);
            throw new IndexOutOfBoundsException();
        }
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
