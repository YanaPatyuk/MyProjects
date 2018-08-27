package animations;
/**
 * @author YanaPatyuk
 *
 * @param <T>
 */
public interface Task<T> {
    /**
     * run the task.
     * @return statement.
     */
    T run();
 }
