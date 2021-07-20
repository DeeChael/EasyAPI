package org.ezapi.util;

public final class MathUtils {

    public static int number(String romanNumeral) {
        int[] array = new int[400];
        array['I'] = 1;
        array['V'] = 5;
        array['X'] = 10;
        array['L'] = 50;
        array['C'] = 100;
        array['D'] = 500;
        array['M'] = 1000;
        char[] number2Array = romanNumeral.toCharArray();
        int finalInt = array[number2Array[0]];
        for (int i = 0; i < number2Array.length - 1; i++) {
            if (array[number2Array[i]] >= array[number2Array[i + 1]]) {
                finalInt += array[number2Array[i + 1]];
            } else {
                finalInt = finalInt + array[number2Array[i + 1]] - 2 * array[number2Array[i]];
            }
        }
        return finalInt;
    }

    public static String romanNumeral(int number) {
        StringBuilder romanNumeral = new StringBuilder();
        int[] aArray = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] rArray = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X",
                "IX", "V", "IV", "I" };
        if (number < 1 || number > 3999) {
            romanNumeral = new StringBuilder("-1");
        } else {
            for (int i = 0; i < aArray.length; i++) {
                while (number >= aArray[i]) {
                    romanNumeral.append(rArray[i]);
                    number -= aArray[i];
                }
            }
        }
        return romanNumeral.toString();
    }

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
