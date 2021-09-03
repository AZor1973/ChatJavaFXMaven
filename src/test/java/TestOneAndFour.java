import lesson6.Lesson6;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestOneAndFour {
    private Lesson6 less;

    @BeforeEach
    public void init() {
        less = new Lesson6();
    }

    @Test
    public void test_empty_array() {
        Assertions.assertFalse(less.oneAndFour(new int[0]));
    }

    @Test
    public void test_only_one() {
        Assertions.assertFalse(less.oneAndFour(new int[]{1}));
    }

    @Test
    public void test_only_four() {
        Assertions.assertFalse(less.oneAndFour(new int[]{4}));
    }

    @Test
    public void test_without_one_and_four() {
        Assertions.assertFalse(less.oneAndFour(new int[]{2, 3, 5}));

    }

    @Test
    public void test_only_one_and_four() {
        Assertions.assertTrue(less.oneAndFour(new int[]{1, 4}));
    }

    @Test
    public void test_with_everybody() {
        Assertions.assertFalse(less.oneAndFour(new int[]{1, 2, 3, 4, 5}));

    }
}
