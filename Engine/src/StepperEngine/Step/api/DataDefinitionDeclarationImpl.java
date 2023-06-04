package StepperEngine.Step.api;

import StepperEngine.DataDefinitions.api.DataDefinition;

import java.io.Serializable;
import java.util.Objects;

/***
 * A class that represents the inputs and outputs of each step.
 */
public class DataDefinitionDeclarationImpl implements DataDefinitionsDeclaration, Serializable {

    String name;
    String userString;//Displays the string to be displayed to the user when entering or exiting the information
    DataNecessity dataNecessity;
    String alias;
    DataDefinition dataDefinition;

    public DataDefinitionDeclarationImpl(String name, String userString, DataNecessity dataNecessity, DataDefinition dataDefinition){
        this.name=name;
        this.userString=userString;
        this.dataNecessity=dataNecessity;
        this.dataDefinition=dataDefinition;
        this.alias=name;
    }

    @Override
    public void setAliasName(String name) {
        this.alias = name;
    }

    @Override
    public String getAliasName() {
        return alias;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataNecessity necessity() {
        return dataNecessity;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }


    /***
     * Implementation of equals by name or the same class or a successor class
     * @param obj
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        DataDefinitionDeclarationImpl other = (DataDefinitionDeclarationImpl) obj;
        return (this.dataDefinition.getType().isAssignableFrom(other.dataDefinition.getType())
                || other.dataDefinition.getType().isAssignableFrom(this.dataDefinition.getType())) && this.alias.equals(other.getAliasName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataDefinition);
    }
}
