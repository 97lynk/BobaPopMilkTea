package BoBaPop.Util;

import java.util.Map;
import java.util.TreeMap;

//The class calculte number of row, col, max cell 
public class ConfigureCell {

    //1 child node  600x1000
    private final Grid ONE = new Grid(1, 1);
    //2 child node  500x600
    private final Grid TWO = new Grid(1, 2);
    //[3,4] child node  300x500
    private final Grid FOUR = new Grid(2, 2);
    //[5, 6] child node 300x333
    private final Grid SIX = new Grid(2, 3);
    //[7, 9] child node 200x333
    private final Grid NINE = new Grid(3, 3);
    //[10, 12] child node 200x250
    private final Grid TWELVE = new Grid(3, 4);
    //[13, 15] child node 200x200
    private final Grid FIFTEEN = new Grid(3, 5);
    //[16, 18] child node 166x200
    private final Grid EIGHTEEN = new Grid(3, 6);
    //[19, 24] child node 166x4
    private final Grid TWENTY_FOUR = new Grid(4, 6);

    private final Map<Integer, Grid> info = new TreeMap<>();

    public ConfigureCell() {
        info.put(ONE.getCountCell(), ONE);
        info.put(TWO.getCountCell(), TWO);
        info.put(FOUR.getCountCell(), FOUR);
        info.put(SIX.getCountCell(), SIX);
        info.put(NINE.getCountCell(), NINE);
        info.put(TWELVE.getCountCell(), TWELVE);
        info.put(FIFTEEN.getCountCell(), FIFTEEN);
        info.put(EIGHTEEN.getCountCell(), EIGHTEEN);
        info.put(TWENTY_FOUR.getCountCell(), TWENTY_FOUR);

    }

    public Grid make(int amount) {
        if (amount <= 0 || amount > 24) {
            return null;
        }
        int min = 1;
        for (Map.Entry<Integer, Grid> entry : info.entrySet()) {
            Integer key = entry.getKey();
            Grid value = entry.getValue();
            if (min <= amount && amount <= value.getCountCell()) {
                return value;
            }
            min = key + 1;
        }
        return null;
    }

}
