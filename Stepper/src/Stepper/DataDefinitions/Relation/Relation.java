package Stepper.DataDefinitions.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relation {

    public Relation(List<String> colNames){
        this.colNames = colNames;
    }
    private List<String> colNames;
    private  List<Row> rows = new ArrayList<>();

    private static class Row{
        Map<String, String> row = new HashMap();
        public void addData(String colName, String data){
            row.put(colName, data);
        }
    }

}
