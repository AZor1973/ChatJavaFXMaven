import lesson6.Lesson6;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAfterFour {
    private Lesson6 less;

    @BeforeEach
    public void init() {
        less = new Lesson6();
    }

    @Test
    public void test_empty_array() {
        Assertions.assertThrows(RuntimeException.class, () -> less.afterFour(new int[0]));
    }

    @Test
    public void test_normal() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        Assertions.assertArrayEquals(new int[]{5, 6, 7, 8}, less.afterFour(arr));
    }

    @Test
    public void test_without_4() {
        int[] arr = {1, 2, 3, 5, 6, 7, 8};
        Assertions.assertThrows(RuntimeException.class, () -> less.afterFour(arr));

    }

    @Test
    public void test_with_empty_result() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 4};
        Assertions.assertArrayEquals(new int[0], less.afterFour(arr));
    }
}
