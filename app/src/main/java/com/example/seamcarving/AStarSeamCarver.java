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
        return picture.getWidth();
    }

    public int height() {
        return picture.getHeight();
    }

    /* TODO ?????
    public Color get(int x, int y) {
        int pixel = picture.getPixel(x, y);
        return Color.valueOf((Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
    }*/

    private double energy(int x, int y) {
        if (x < 0 || x > picture.getWidth() - 1 || y < 0 || y > picture.getHeight() - 1) {
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

    public int[] findHorizontalSeam() {
        HorizontalBitmapGraph pgh = new HorizontalBitmapGraph(picture);
        Point start = new Point(-1, 0);
        Point end = new Point(picture.getWidth(), 0);
        AStarSolver<Point> horizontalSeam = new AStarSolver<>(pgh, start, end);
        return solutionToInt(horizontalSeam.solution(), true);
    }

    public int[] findVerticalSeam() {
        VerticalBitmapGraph pgv = new VerticalBitmapGraph(picture);
        Point start = new Point(0, -1);
        Point end = new Point(0, picture.getHeight());
        AStarSolver<Point> verticalSeam = new AStarSolver<>(pgv, start, end);
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
