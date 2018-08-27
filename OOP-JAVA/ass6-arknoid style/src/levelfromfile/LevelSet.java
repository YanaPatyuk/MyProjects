package levelfromfile;
/**
 * @author YanaPatyuk
 *
 */
public class LevelSet {
private String   key;
private String message;
private String path;
/**
 * create LevelSet informtion.
 * @param k key string choice.
 * @param m name of level.
 */
public LevelSet(String k, String m) {
    this.key = k;
    this.message = m;
}
/**
 * @param p path of file.
 */
public void setPathForLevel(String p) {
    this.path = p;
}
/**
 * @return key.
 */
public String getKey() {
    return this.key;
}
/**
 * @return location of file.
 */
public String getPath() {
    return this.path;
}
/**
 * @return name of level.
 */
public String getMessage() {
    return this.message;
}
}
