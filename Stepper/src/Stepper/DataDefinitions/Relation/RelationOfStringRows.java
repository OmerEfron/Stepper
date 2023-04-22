package Stepper.DataDefinitions.Relation;

import java.util.List;

public class RelationOfStringRows extends Relation<List<String>> {

    public RelationOfStringRows(List<String> colNames){
        this.colNames=colNames;
    }
    public void addRow(List<String> rowToAdd){
        rows.add(rowToAdd);
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }
    @Override
    public String relationToCSV(){
        String CSV= String.join(",", colNames);
        for(List<String> row:rows){
            CSV=CSV+"\n"+String.join(",", row);
        }
        return CSV;
    }
    @Override
    public String createPropertiesExporter(Integer totalProperties) {
        String propertiesExporter="";
        Integer num=0;
        totalProperties=0;
        for(List<String> row:rows){
            num++;
            propertiesExporter+= "row-"+num.toString()+".";
            for(int currIndexRow=0,currIndexCol=0;currIndexRow<colNames.size();currIndexRow++,currIndexCol++){
                totalProperties++;
                propertiesExporter+= colNames.get(currIndexCol)+"="+row.get(currIndexRow);
            }
            propertiesExporter+="\n";
        }
        return propertiesExporter;
    }



//    private static class Row{
//        Map<String, String> row = new HashMap();
//        public void addData(String colName, String data){
//            row.put(colName, data);
//        }
//    }

}
