
import java.util.List;

public class Row {
    List<String> values;

    public Row(List<String> values) {
        this.values = values;
    }

    //TODO: Implement the getValue method to return the value of the specified column name
    public String getValue(int columnIndex) {
        return values.get(columnIndex);
    }
    
}
