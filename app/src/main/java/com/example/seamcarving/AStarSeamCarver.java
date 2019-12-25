package com.example.seamcarving;

import android.graphics.Bitmap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AStarSeamCarver {

    private Bitmap picture;
    private Set<Point> seamPoints;

    public AStarSeamCarver(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = bitmap; //
        seamPoints = new HashSet<>();
    }

    public int[] findHorizontalSeam() {
        return findSeam(true);
    }

    public int[] findVerticalSeam() {
        return findSeam(false);
    }

    private int[] findSeam(boolean horizontal) {
        AStarGraph<Point> pg = horizontal ? new HorizontalBitmapGraph(picture, seamPoints) : new VerticalBitmapGraph(picture, seamPoints);
        Point start = horizontal ? new Point(-1, 0) : new Point(0, -1);
        Point end = horizontal ? new Point(picture.getWidth(), 0) : new Point(0, picture.getHeight());
        AStarSolver<Point> seam = new AStarSolver<>(pg, start, end);
        seamPoints.addAll(seam.solution());
        return solutionToInt(seam.solution(), horizontal);
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
