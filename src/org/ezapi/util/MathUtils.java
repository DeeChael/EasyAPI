package org.ezapi.util;

public final class MathUtils {

    public static double square(double a) {
        return a * a;
    }

    public static double cuboidArea(double length, double width, double height) {
        return 2 * ((square(length)) + (square(width)) + (square(height)));
    }

    public static double hypotenuse(double squareEdge, double theOtherSquareEdge) {
        return Math.sqrt(square(squareEdge) + square(theOtherSquareEdge));
    }

}
