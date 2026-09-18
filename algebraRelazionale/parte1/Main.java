import java.util.*;
public class Main {
    
    public static void main(String[] args) {
        Relation relation = new Relation();
        relation.setHeader(List.of("Name", "Age", "City"));
        relation.addRow(new Row(List.of("Alice", "30", "New York")));
        relation.addRow(new Row(List.of("Bob", "25", "Los Angeles")));
        Relation selectedRelation = relation.select("Name", "Alice");
        System.out.println(selectedRelation);
    }
}
