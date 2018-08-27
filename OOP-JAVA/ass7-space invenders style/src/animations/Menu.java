package animations;
/**
 * @author YanaPatyuk
 *
 * @param <T>
 */
public interface Menu<T> extends Animation {
    /**
     * @param key wait.
     * @param message line to print.
     * @param returnVal value.
     */
    void addSelection(String key, String message, T returnVal);
    /**
     * @return stasus.
     */
    T getStatus();
    /**
     * add subMenu.
     * @param key wait.
     * @param message message line to print.
     * @param subMenu option.
     */
    void addSubMenu(String key, String message, Menu<T> subMenu);
 }
