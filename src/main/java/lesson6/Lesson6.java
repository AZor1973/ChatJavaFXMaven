package lesson6;

import java.util.Arrays;

public class Lesson6 {

    public int[] afterFour(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                return Arrays.copyOfRange(arr, i + 1, arr.length);
            }
        }
        throw new RuntimeException();
    }

    public boolean oneAndFour(int[] arr) {
        boolean present1 = false;
        boolean present4 = false;
        for (int j : arr) {
            if (j == 1) {
                present1 = true;
            } else if (j == 4) {
                present4 = true;
            } else {
                return false;
            }
        }
        return present1 && present4;
    }
}
