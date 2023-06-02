package StepperEngine.DataDefinitions.Enumeration;




public class ZipperEnumeration extends EnumerationSet<String> {
    ZipperEnumeration(){
        this.set.add(ZipEnumerator.ZIP.toString());
        this.set.add(ZipEnumerator.UPZIP.toString());
    }

}
