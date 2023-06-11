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
    boolean isInitial=false;

    public DataDefinitionDeclarationImpl(String name, String userString, DataNecessity dataNecessity, DataDefinition dataDefinition){
        this.name=name;
        this.userString=userString;
        this.dataNecessity=dataNecessity;
        this.dataDefinition=dataDefinition;
        this.alias=name;
    }

    @Override
    public boolean isInitial() {
        return isInitial;
    }
    @Override
    public void setInitial(boolean initial) {
        isInitial = initial;
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
//    @Override
//    public boolean equals(Object obj){
//        if(obj == this)
//            return true;
//        if(obj == null || obj.getClass() != this.getClass())
//            return false;
//
//        DataDefinitionDeclarationImpl other = (DataDefinitionDeclarationImpl) obj;
//        return (this.dataDefinition.getType().isAssignableFrom(other.dataDefinition.getType())
//                || other.dataDefinition.getType().isAssignableFrom(this.dataDefinition.getType())) && this.alias.equals(other.getAliasName());
//    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DataDefinitionDeclarationImpl other = (DataDefinitionDeclarationImpl) obj;

        // Condition 1: Check if the dataDefinition.getType() is assignable from the other instance or vice versa
        Class<?> thisType = this.dataDefinition().getType();
        Class<?> otherType = other.dataDefinition().getType();
        boolean isAssignable = thisType.isAssignableFrom(otherType) || otherType.isAssignableFrom(thisType);

        // Condition 2: Check if the alias of both instances is equal
        boolean isAliasEqual = this.alias.equals(other.alias);

        return isAssignable && isAliasEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, dataDefinition.getType().getCanonicalName());
    }

}
