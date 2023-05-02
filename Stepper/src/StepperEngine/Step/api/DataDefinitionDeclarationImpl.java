package StepperEngine.Step.api;

import StepperEngine.DataDefinitions.api.DataDefinitionInterface;

import java.util.Objects;

public class DataDefinitionDeclarationImpl implements DataDefinitionsDeclaration{

    String name;
    String userString;
    DataNecessity dataNecessity;
    String alias;
    DataDefinitionInterface dataDefinition;
    public DataDefinitionDeclarationImpl(String name, String userString,DataNecessity dataNecessity,DataDefinitionInterface dataDefinition){
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
    public DataDefinitionInterface dataDefinition() {
        return dataDefinition;
    }



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
