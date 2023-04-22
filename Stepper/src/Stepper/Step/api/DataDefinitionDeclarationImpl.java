package Stepper.Step.api;

import Stepper.DataDefinitions.api.DataDefinitionInterface;

import java.util.Objects;

public class DataDefinitionDeclarationImpl implements DataDefinitionsDeclaration{

    String name;
    String userString;
    DataNecessity dataNecessity;

    DataDefinitionInterface dataDefinition;
    public DataDefinitionDeclarationImpl(String name, String userString,DataNecessity dataNecessity,DataDefinitionInterface dataDefinition){
        this.name=name;
        this.userString=userString;
        this.dataNecessity=dataNecessity;
        this.dataDefinition=dataDefinition;
    }

    @Override
    public void setAliasName(String name) {
        this.name = name;
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
    public DataDefinitionInterface dataDefinition() {
        return dataDefinition;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        DataDefinitionsDeclaration other = (DataDefinitionsDeclaration) obj;
        return this.dataDefinition.getName().equals(other.dataDefinition().getName()) && this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataDefinition);
    }
}
