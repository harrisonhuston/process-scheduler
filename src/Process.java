//Process class defines Process object
//Process object includes int id, priority, duration, arrivalTime, waitTime
public class Process {
    public int id;
    public int priority;
    public int duration;
    public int arrivalTime;
    public int waitTime;






    //Process tokens to corresponding elements, parse to int
    //Used to tokenize the input file and obtain the id, priority, duration, and arrivalTime, defines waitTime as 0
    public Process(String[] tokens){
        id = Integer.parseInt(tokens[0]);
        priority = Integer.parseInt(tokens[1]);
        duration = Integer.parseInt(tokens[2]);
        arrivalTime = Integer.parseInt(tokens[3]);
        waitTime = 0;


    }



    //Override to string, get elements to print objects
    //Allows to print Id, priority, duration, arrival time of process objects, this.get method to get object elements
    @Override
    public String toString() {
        return ("Id = "+this.getId()+
                " priority = "+ this.getPriority()+
                " duration = "+ this.getDuration() +
                " arrival Time =  " + this.getArrivalTime());
    }
    //get methods return elements for priority, arrivalTime, id, duration, waitTime
    public int getPriority(){return priority;}
    public int getArrivalTime(){return arrivalTime;}
    public int getId(){return id;}
    public int getDuration(){return duration;}
    public int getWaitTime(){return waitTime;}




}

