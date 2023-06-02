package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Step.api.DataDefinitionDeclarationImpl;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Step.api.StepDefinitionAbstract;
import StepperEngine.Step.api.StepStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        Instant start = Instant.now();
        String command=context.getDataValue("COMMAND",String.class);
        Optional<String> arg=Optional.ofNullable(context.getDataValue("ARGUMENTS",String.class));
        context.addLog(stepName,"About to invoke "+command+" "+arg.orElse(""));
        try {
            ProcessBuilder processBuilder=new ProcessBuilder(command);
            arg.ifPresent(processBuilder::command);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            context.storeValue("RESULT",output.toString());
            context.setInvokeSummery(stepName,"The command was executed successfully");
        } catch (IOException e) {
            context.setInvokeSummery(stepName,"The command failed");
        }
        context.setStepStatus(stepName,StepStatus.SUCCESS);

        context.setTotalTime(stepName, Duration.between(start, Instant.now()));
        return context.getStepStatus(stepName);
    }
}
