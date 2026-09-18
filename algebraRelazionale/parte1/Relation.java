
import java.util.*;

public class Relation {
    List<String> header;
    List<Row> rows;

    public Relation() {
        
    }

    public void addRow(Row rowToAdd){
        this.rows.add(rowToAdd);
    }

    public void setHeader(List<String> header){
        this.header = header;
    }
    
}
