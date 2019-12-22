package com.example.seamcarving;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Picture;

import java.util.List;

public class AStarSeamCarver {
    private Bitmap picture;

    public AStarSeamCarver(Bitmap bitmap) {
        if (picture == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = bitmap; //
    }

    public Bitmap picture() {
        return Bitmap.createBitmap(picture);
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public Color get(int x, int y) {
        return picture.get(x, y);
    }

    public double energy(int x, int y) {
        if (x < 0 || x > picture.width() - 1 || y < 0 || y > picture.height() - 1) {
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

    public int[] findHorizontalSeam() {
        HorizontalBitmapGraph pgh = new HorizontalBitmapGraph(picture);
        Point start = new Point(-1, 0);
        Point end = new Point(picture.width(), 0);
        AStarSolver<Point> horizontalSeam = new AStarSolver<>(pgh, start, end, Integer.MAX_VALUE);
        return solutionToInt(horizontalSeam.solution(), true);
    }

    public int[] findVerticalSeam() {
        VerticalBitmapGraph pgv = new VerticalBitmapGraph(picture);
        Point start = new Point(0, -1);
        Point end = new Point(0, picture.height());
        AStarSolver<Point> verticalSeam = new AStarSolver<>(pgv, start, end, Integer.MAX_VALUE);
        return solutionToInt(verticalSeam.solution(), false);
    }

    private int[] solutionToInt(List<Point> seam, boolean x) {
        seam.remove(0);
        seam.remove(seam.size() - 1);
        int[] solution = new int[seam.size()];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = x ? (int) seam.get(i).y() : (int) seam.get(i).x();
        }
        return solution;
    }
}
