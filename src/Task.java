import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.time.Period;
public class Task {
    private String taskTitle;
    private LocalDate inputDate, dueDate;
    private Integer duration;

    public Task(String taskTitle,LocalDate inputDate, LocalDate dueDate){
        this.taskTitle = taskTitle;
        this.inputDate = inputDate;
        this.dueDate = dueDate;
    }

    public Task getTask() {
        return this;
    }
    public String getTaskTitle() {
        return this.taskTitle;
    }

    //date
    public LocalDate getInputDate() {
        return inputDate;
    }
    public LocalDate getDueDate() {
        return this.dueDate;
    }

}
