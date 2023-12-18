package railway;

import java.util.LinkedHashSet;
import java.util.Set;

public class Station {
	int id;
    Set<Integer> to;

    public Station(int id) {
        this.id = id;
        this.to = new LinkedHashSet<>();
    }

}
