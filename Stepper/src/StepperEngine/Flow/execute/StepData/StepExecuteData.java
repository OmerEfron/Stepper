package StepperEngine.Flow.execute.StepData;

import StepperEngine.Flow.api.StepUsageDeclerationInterface;
import StepperEngine.Step.api.StepStatus;
import javafx.util.Pair;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StepExecuteData{
    private StepUsageDeclerationInterface step;
    private String finalName;
    private String name;
    private Duration totalTime;
    private String invokeSummery;
    private StepStatus stepStatus;
    private List<Pair<String, String>> logs=new LinkedList<>();




    public StepExecuteData(StepUsageDeclerationInterface step) {
        this.step=step;
        this.finalName =step.getStepFinalName();
        this.name=step.getStepDefinition().getName();

    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }


    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    public String getInvokeSummery() {
        return invokeSummery;
    }

    public void setInvokeSummery(String invokeSummery) {
        this.invokeSummery = invokeSummery;
    }

    public StepStatus getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(StepStatus stepStatus) {
        this.stepStatus = stepStatus;
    }

    public List<Pair<String, String>> getLogs() {
        return logs;
    }

    public void addLog(String log) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String formattedTime = currentTime.format(formatter);
        this.logs.add(new Pair<>(log,formattedTime));
    }



}
