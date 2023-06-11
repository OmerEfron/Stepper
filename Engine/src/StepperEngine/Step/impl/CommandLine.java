package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class CommandLine extends StepDefinitionAbstract {
    public CommandLine(){
        super("Command Line",false);
        this.addInput(new DataDefinitionDeclarationImpl("COMMAND","Command", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        this.addInput(new DataDefinitionDeclarationImpl("ARGUMENTS","Command arguments", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        this.addOutput(new DataDefinitionDeclarationImpl("RESULT","Command output", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        String command=context.getDataValue(nameToData.get("COMMAND"),String.class);
        Optional<String> arg=Optional.ofNullable(context.getDataValue(nameToData.get("ARGUMENTS"),String.class));
        context.addLog(step,"About to invoke "+command+" "+arg.orElse(""));
        try {
            StringBuilder output = new StringBuilder();
            ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe", "/c",command);
            arg.ifPresent(argument -> processBuilder.command().add(argument));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            context.storeValue(nameToData.get("RESULT"),output.toString() );
            context.setInvokeSummery(step,"The command was executed successfully");
        } catch (IOException e) {
            context.setInvokeSummery(step,"The command failed");
        }
        context.setStepStatus(step,StepStatus.SUCCESS);

        context.setTotalTime(step, Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }
}
