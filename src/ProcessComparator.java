import java.util.Comparator;


//ProcessComparator class implements comparator
//Class is used to compare priorities of the processes in the Priority Queue
//Uses if, else if, to compare the priority of a specific process vs. another process
public class ProcessComparator implements Comparator<Process> {
    @Override
    public int compare(Process a, Process b){
        if (a.getPriority() > b.getPriority())
        return 1;
        else if (a.getPriority() < b.getPriority())
            return - 1;
        return 0;
    }
}
