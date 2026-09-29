
import java.util.List;

public class Row {
    List<String> values;

    public Row(List<String> values) {
        this.values = values;
    }

    public String getValue(int index) {
        return values.get(index);
    }

    public List<String> getValues() {
        return values;
    }

    @Override 
    public String toString(){
        String out = "";
        for (int i = 0; i < values.size(); i++) {
            out += values.get(i) + " ";
        }
        return out;
    }
}
