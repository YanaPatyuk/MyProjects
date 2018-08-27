package game;

import java.util.Comparator;
/**
 * @author YanaPatyuk
 * comperator class to check what object comes first.
 */
public class ObjectCompere implements Comparator<Object> {

    @Override
    public int compare(Object a, Object b) {
        if (a instanceof ScoreInfo && b instanceof ScoreInfo) {
            ScoreInfo c1 = (ScoreInfo) a;
            ScoreInfo c2 = (ScoreInfo) b;

            if (c1.getScore() < c2.getScore()) {
                return 1;
            } else if (c1.getScore() > c2.getScore()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

}
