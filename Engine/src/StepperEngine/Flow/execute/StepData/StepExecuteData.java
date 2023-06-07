package StepperEngine.Flow.execute.StepData;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Step.api.StepStatus;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StepExecuteData implements Serializable {

    private final String finalName;
    private final String name;
    private Duration totalTime;
    private String invokeSummery;
    private StepStatus stepStatus;
    private List<Pair<String, String>> logs=new LinkedList<>();
    private final Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    Map<String, Object> dataMap = new HashMap<>();




    public StepExecuteData(StepUsageDecleration step) {
        this.finalName =step.getStepFinalName();
        this.name=step.getStepDefinition().getName();
        this.id=step.getIndex();
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }
    public String getFinalName() {
        return finalName;
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

    public void setEndTime() {
        this.endTime = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() throws IllegalAccessException{
        if(endTime!=null){
            return endTime;
        }
        throw new IllegalAccessException("step has not ended");
    }

    public void addStepData(String dataName, Object value){
        dataMap.put(dataName, value);
    }
}
