
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

    private int getColumnIndex(String columnName){
        return this.header.indexOf(columnName);
    }
    
    public Relation select(String columnName, String value){
        Relation newRelation = new Relation();
        newRelation.setHeader(this.header);
        for(Row row : this.rows){
            if(row.getValue(getColumnIndex(columnName)).equals(value)){
                newRelation.addRow(row);
            }
        }
        return newRelation;
    }

    public Relation project(List<String> columnNames){
        Relation newRelation = new Relation();
        List<String> newHeader = new ArrayList<>();
        for(String columnName : columnNames){
            if(this.header.contains(columnName)){
                newHeader.add(columnName);
            }
        }
        newRelation.setHeader(newHeader);
        for(Row row : this.rows){
            List<String> newRowValues = new ArrayList<>();
            for(String columnName : columnNames){
                if(this.header.contains(columnName)){
                    newRowValues.add(row.getValue(getColumnIndex(columnName)));
                }
            }
            Row newRow = new Row(newRowValues);
            newRelation.addRow(newRow);
        }
        return newRelation;
    }

    public Relation union(Relation otherRelation){
        Relation newRelation = new Relation();
        newRelation.setHeader(this.header);
        for(Row row : this.rows){
            newRelation.addRow(row);
        }
        for(Row row : otherRelation.rows){
            newRelation.addRow(row);
        }
        return newRelation;
    }

    public Relation difference(Relation otherRelation){
        Relation newRelation = new Relation();
        newRelation.setHeader(this.header);
        for(Row row : this.rows){
            if(!otherRelation.rows.contains(row)){
                newRelation.addRow(row);
            }
        }
        return newRelation;
    }
}
