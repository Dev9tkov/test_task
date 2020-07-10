import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Ilya Devyatkov
 * @since 10.07.2020
 */
public class TaskTest {

    @Test
    public void whenHaveWayThenHaveMap() {
        Task task = new Task();
        char[][] example = {
                {'.', '.', '.', '.', '@'},
                {'#', '.', '#', '#', '#'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', 'X', '.'},
                {'.', '.', '.', '.', '.'}
        };

        char[][] expected = {
                {'.', '+', '+', '+', '@'},
                {'#', '+', '#', '#', '#'},
                {'.', '+', '.', '.', '.'},
                {'.', '+', '+', 'X', '.'},
                {'.', '.', '.', '.', '.'}
        };
        assertThat(task.searchRoute(example), is(expected));
    }

    @Test
    public void whenHaveWayThenHaveMap2() {
        Task task = new Task();
        char[][] example = {
                {'.', '.', '.', '@', '.'},
                {'.', '#', '#', '#', '#'},
                {'.', '.', '.', '.', '.'},
                {'#', '#', '#', '#', '.'},
                {'.', 'X', '.', '.', '.'}
        };

        char[][] expected = {
                {'+', '+', '+', '@', '.'},
                {'+', '#', '#', '#', '#'},
                {'+', '+', '+', '+', '+'},
                {'#', '#', '#', '#', '+'},
                {'.', 'X', '+', '+', '+'}
        };
        assertThat(task.searchRoute(example), is(expected));
    }

    @Test
    public void whenNoWayThenHaveNotMap() {
        Task task = new Task();
        char[][] example = {
                {'.', '.', 'X', '.', '.'},
                {'#', '#', '#', '#', '#'},
                {'.', '.', '.', '.', '.'},
                {'.', '@', '.', '.', '.'},
                {'.', '.', '.', '.', '.'}
        };
        assertNull(task.searchRoute(example));
    }
}