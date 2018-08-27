package primitives;
/**
 * @author YanaPatyuk
 * count thing class.
 */
public class Counter {
    private int countNumber = 0;
    /**
     * Contractor.
     * @param num of beginning number.
     */
    public Counter(int num) {
        this.countNumber = num;
    }
    /**
     * add number to current count.
     * @param number to add.
     */
    public void increase(int number) {
        this.countNumber += number;
    }
    /**
     * @param number subtract number from current count.
     */
    public void decrease(int number) {
        this.countNumber -= number;
    }
    /**
     * @return get current count.
     */
    public int getValue() {
        return this.countNumber;
    }
 }
