import java.io.*;
import java.util.*;



public class ProcessScheduling {

    //Set tokens used in the input file by lines, ensuring 4 tokens per line
    //Method is used to split the tokens and trim them to ensure process objects can be properly populated by tokens
    public static int getTokens(String line, String[] tokens) {
        //initialize boolean
        boolean useStringTokenizer = true;
        //String tokenizer use delimiter "\\s+" all whitespace
        if (useStringTokenizer) {
            StringTokenizer st = new StringTokenizer(line, " ");
            int i = 0;
            //While the string has more tokens to iterate over and trim the spaces to get numbered tokens
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                tokens[i++] = (token.trim());
            }
        } else {
            //Split for tokens with regex, if not equal to 4 return 0
            String[] split_tokens = line.split("\\s+");
            if (split_tokens.length != 4) {
                return 0;
            }
            //for i less than the length of tokens, the token at position i = split tokens at i
            for (int i = 0; i < split_tokens.length; i++) {
                tokens[i] = split_tokens[i];
            }
        }
        //returns the tokens length
        return tokens.length;
    }

    //processObjectArray arrayList of Process objects
    private static ArrayList<Process> processObjectArray;

    //populateProcesses method reads the text file and uses getTokens method to access elements of text file and
    //populates the contents to an arrayList (processObjectArray), which holds object processes
    private static void populateProcesses() throws IOException, FileNotFoundException {
        //New arrayList named processObjectArray of 10 process objects
        processObjectArray = new ArrayList<>();
        //Reads file - process_scheduling_input.txt
        FileReader f = new FileReader("process_scheduling_input.txt");
        BufferedReader in = new BufferedReader(f);
        Scanner scan = new Scanner(in);
        int i = 0;
        //while the scan has next line, scans next line
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            //tokens array of 4 items for each object
            String[] tokens = new String[4];
            int numTokens = getTokens(line, tokens);
            //if the tokens do not equal 0
            if (numTokens != 0) {
                //instantiates the processObjectArray with new process tokens
                processObjectArray.add(i++, new Process(tokens));
            }
        }
        //close file
        in.close();

    }


    //Main method, implements the bulk of the logic in the program
    //First tries populateProcesses(); method to get all process objects from the file with their elements
    //It creates a priority queue and utilizes while, for loops, and if statements along with the currentTime, waitTime,
    //arrivalTime, removalTime, creates a temp and waitTime arrayLists to facilitate the process of running a process,
    //from  its inception to ultimate finish and write to file
    public static void main(String[] args) throws IOException {
        //try to run method populateProcess to get the process objects
        try {
            populateProcesses();
        }
        //Catch file not found and print string
        catch (FileNotFoundException e) {
            System.out.println("The file was not found");
        }
        //Catch IO exception and print string
        catch (IOException e) {
            System.out.println("There was an issue running the program.");
        }
        //try to create a file writer to write to new file "process_scheduling_output.txt"
        try {
            FileWriter myWriter = new FileWriter("process_scheduling_output.txt");
            {
        //Iterate through processObjectArrays, print to file with override method from Process class
        myWriter.write("All processes:");
        for (int i = 0; i < processObjectArray.size(); i++) {
            myWriter.write("\n" + processObjectArray.get(i).toString());
        }
                myWriter.write("\n\nMaximum wait time = 30");

        //New PriorityQueue names priorityQueue, new ProcessComparator from ProcessComparator class
        PriorityQueue<Process> priorityQueue = new PriorityQueue<>(new ProcessComparator());
        //Initialize currentTime, removalTime, totalWaitTime = 0, create boolean running set to false
        int currentTime = 0;
        int removalTime = 0;
        int totalWaitTime = 0;
        boolean running = false;
        //New ArrayList of process objects called temp and ArrayList of type Integer called waitArray
        ArrayList<Process> temp = new ArrayList<>();
        ArrayList<Integer> waitArray = new ArrayList<>();

        //Int runTime initialized is set to 0
        int runTime = 0;

                //While processObjectArray is not empty, if the first element in the processObjectArray is less than
                //or equal to current time, add the process to priorityQueue and remove from processObjectArray
                while (processObjectArray.size() != 0) {
                    if (processObjectArray.get(0).getArrivalTime() <= currentTime) {
                        priorityQueue.add(processObjectArray.get(0));
                        processObjectArray.remove(0);
                    }

                    //if priorityQueue is not empty and running is false, remove process from priorityQueue, set it's
                    //removal time to current time and add to temp ArrayList, add to waitArray, the waitTime of the
                    //process being added to temp ArrayList, set totalWaitTime to 0
                    if (!priorityQueue.isEmpty() && running == false) {
                        Process a = priorityQueue.remove();
                        removalTime = currentTime;
                        temp.add(a);
                        waitArray.add(temp.get(0).getWaitTime());
                        totalWaitTime = 0;

                        //Iterate over waitArray and totalWaitTime += elements in waitArray
                        for (int i = 0; i < waitArray.size(); i++) {
                            totalWaitTime += waitArray.get(i);
                        }

                        //Initialize totalWaitFloat as float and = to totalWaitTime
                        float totalWaitFloat;
                        totalWaitFloat = totalWaitTime;

                        //Write to file string and process from temp(0) get Id, removalTime, WaitTime, and totalWaitFloat
                        myWriter.write("\nProcess removed from queue is: id = " + temp.get(0).getId() + ", at time "
                                + removalTime + ", wait time = " + temp.get(0).getWaitTime() + " Total wait time = " +
                                totalWaitFloat);
                        //Write to file string and process from temp(0) get Id, Priority, ArrivalTime and Duration
                        myWriter.write("\nProcess ID = " + temp.get(0).getId() + "\n\tPriority = " +
                                temp.get(0).getPriority() + "\n\tArrival = " + temp.get(0).getArrivalTime() + "\n\tDuration = "
                                + temp.get(0).getDuration());
                        //Set running to true
                        running = true;
                    }

                    //if running equals true and runTime equals temp(0) duration with .get
                    if (running == true && runTime == temp.get(0).getDuration()) {

                        //Write to file string + the Id of temp(0) and string with the current time
                        myWriter.write("\nProcess " + temp.get(0).getId() + " finished at time " +
                                currentTime);

                        //remove temp from file index 0
                        temp.remove(0);

                        //set running to false, runTime = 1
                        running = false;
                        runTime = 1;
                    }

                    //if running equals false, ArrayList pQCopy new ArrayList, while priortyQueue is not empty
                    //poll priority queue and add to PQCopy
                    if (running == false) {
                        ArrayList<Process> pQCopy = new ArrayList<>();
                        while (!priorityQueue.isEmpty()) {
                            pQCopy.add(priorityQueue.poll());
                        }

                        //Iterate over PQCopy and define Process p as element in pQCopy at i
                        for (int i = 0; i < pQCopy.size(); i++) {
                            Process p = pQCopy.get(i);
                            //If the p's waitTime is greater than 30
                            if (p.getWaitTime() > 30) {
                                //Write to file update priority, write to file string + Id, WaitTime, Priority
                                //Write both update and process information for each process with greater than 30 wait
                                myWriter.write("\n\nUpdate priority:");
                                myWriter.write("\nPID = " + p.getId() + ", wait time = " + p.getWaitTime() +
                                        ", current priority = " + p.getPriority());

                                //p.priority = the process's .get priority -1 to update
                                p.priority = p.getPriority() - 1;

                                //Write to file string plus Id, string plus the new Priority
                                myWriter.write("\nPID = " + p.getId() + ", new priority = " + p.getPriority());

                            }
                        }

                        //Iterate over pqCopy and add to priorityQueue for i
                        for (int i = 0; i < pQCopy.size(); i++) {
                            priorityQueue.add(pQCopy.get(i));
                        }
                    }

                    //  if process is running, add 1 to runTime
                    if (running == true) {
                        runTime++;
                    }

                    //If priorityQueue is not empty and the running is set to false process a = priorityQueue.remove
                    //removalTime = currentTime and add process a to temp
                    if (!priorityQueue.isEmpty() && running == false) {
                        Process a = priorityQueue.remove();
                        removalTime = currentTime;
                        temp.add(a);
                        //Add temp(0) WaitTime to waitArray
                        waitArray.add(temp.get(0).getWaitTime());
                        //totalWaitTime = 0, iterate over waitArray, total WaitTime += waitArray at i
                        totalWaitTime = 0;
                        for (int i = 0; i < waitArray.size(); i++) {
                            totalWaitTime += waitArray.get(i);
                        }
                        //float totalWaitFloat = totalWaitTime
                        float totalWaitFloat;
                        totalWaitFloat = totalWaitTime;

                        //Write to file string plus temp(0) get Id, the removalTime, temp(0) get waitTime
                        //and totalWaitFloat
                        //Write to file string plus temp(0) get Id plus string temp(0) Priority, plus string
                        //temp(0) get ArrivalTime, plus string, temp(0) get Duration
                        //Set running to true
                        myWriter.write("\n\nProcess removed from queue is: id = " + temp.get(0).getId() + ", at time "
                                + removalTime + ", wait time = " + temp.get(0).getWaitTime() + " Total wait time = " +
                                totalWaitFloat);
                        myWriter.write("\nProcess ID = " + temp.get(0).getId() + "\n\tPriority = " +
                                temp.get(0).getPriority() + "\n\tArrival = " + temp.get(0).getArrivalTime() + "\n\tDuration = "
                                + temp.get(0).getDuration());
                        running = true;
                    }

                    //if processObjectArray size equals 0, write to file Array is empty and current time plus new line
                    if (processObjectArray.size() == 0) {
                        myWriter.write("\n\nArray is empty at time = " + currentTime + "\n");
                    }

                    //currentTime plus 1
                    currentTime++;

                    //Iterator of process type name priorityIter equals priorityQueue iterator
                    Iterator<Process> priorityIter = priorityQueue.iterator();
                    //While the iterator has next
                    while (priorityIter.hasNext()) {
                        //Process p = the next and the waitTime of p = get p's waitTime and add 1
                        Process p = priorityIter.next();
                        p.waitTime = p.getWaitTime() + 1;
                    }
                }

                //While the priorityQueue is not empty
                while (!priorityQueue.isEmpty()) {
                    //if priority queue is not empty and running is false, process a = remove from priority queue
                    //removalTime equals current time and add Process a to temp
                    if (!priorityQueue.isEmpty() && running == false) {
                        Process a = priorityQueue.remove();
                        removalTime = currentTime;
                        temp.add(a);

                        //Add temp(0) get WaitTime to waitArray and totalWaitTime = 0
                        waitArray.add(temp.get(0).getWaitTime());
                        totalWaitTime = 0;

                        //iterate over waitArray, totalWaitTime += waitArray at i
                        for (int i = 0; i < waitArray.size(); i++) {
                            totalWaitTime += waitArray.get(i);
                        }
                        //float totalWaitFloat = totalWaitTime
                        float totalWaitFloat;
                        totalWaitFloat = totalWaitTime;

                        //Write to file string, plus temp(0) get Id, plus string, removalTime, plus string, temp(0)
                        //get WaitTime + string, totalWaitFloat
                        myWriter.write("\n\nProcess removed from queue is: id = " + temp.get(0).getId() + ", at time "
                                + removalTime + ", wait time = " + temp.get(0).getWaitTime() + " Total wait time = " +
                                totalWaitFloat);
                        //Write to file string, temp(0) get Id, plus string, temp(0) get Priority, string plus temp(0)
                        //get Arrival time, plus string temp(0) get duration
                        myWriter.write("\nProcess ID = " + temp.get(0).getId() + "\n\tPriority = " +
                                temp.get(0).getPriority() + "\n\tArrival = " + temp.get(0).getArrivalTime() + "\n\tDuration = "
                                + temp.get(0).getDuration());

                        //set running to true
                        running = true;
                    }



                    // if running equals true and runTime equals temp(0) get duration
                    if (running == true && runTime == temp.get(0).getDuration()) {

                        //Write to file string, plus temp(0) get Id, plus string and currentTime
                        myWriter.write("\nProcess " + temp.get(0).getId() + " finished at time " +
                                currentTime);

                        //remove from temp
                        temp.remove(0);

                        //running set to false runTime equals 1
                        running = false;
                        runTime = 1;
                    }

                    //if running equals false, ArrayList pQCopy new ArrayList, while priortyQueue is not empty
                    //poll priority queue and add to PQCopy
                    if (running == false) {
                        ArrayList<Process> pQCopy = new ArrayList<>();
                        while (!priorityQueue.isEmpty()) {
                            pQCopy.add(priorityQueue.poll());
                        }

                        //Iterate over pQCopy, process p equals pQCopy get at i
                        for (int i = 0; i < pQCopy.size(); i++) {
                            Process p = pQCopy.get(i);

                            //if get WaitTime of p is > 30
                            if (p.getWaitTime() > 30) {

                                //Write to file update priority, write to file string + Id, WaitTime, Priority
                                //Write both update and process information for each process with greater than 30 wait
                                myWriter.write("\n\nUpdate priority:");
                                myWriter.write("\nPID = " + p.getId() + ", wait time = " + p.getWaitTime() + ", current priority = "
                                        + p.getPriority());

                                //p.priority equals get priority and decrease by 1
                                p.priority = p.getPriority() - 1;

                                //Write to file string plus p get Id, plus string and p get Priority, the new priority
                                myWriter.write("\nPID = " + p.getId() + ", new priority = " + p.getPriority());

                            }

                        }

                        //iterate over pQCopy, add element at i to priorityQueue
                        for (int i = 0; i < pQCopy.size(); i++) {
                            priorityQueue.add(pQCopy.get(i));
                        }
                    }

                    //If running equals true, add to runTime
                    if (running == true) {
                        runTime++;

                    }

                    //If priorityQueue is not empty and running is false, process a equals priorityQueue removal
                    //RemovalTime equals current time, add process a to temp
                    if (!priorityQueue.isEmpty() && running == false) {
                        Process a = priorityQueue.remove();
                        removalTime = currentTime;
                        temp.add(a);
                        //Add temp(0) get waitTime to waitArray
                        waitArray.add(temp.get(0).getWaitTime());
                        //set totalWaitTime to equal zero
                        totalWaitTime = 0;

                        //iterate over waitArray, totalWaitTime += waitArray get i
                        for (int i = 0; i < waitArray.size(); i++) {
                            totalWaitTime += waitArray.get(i);
                        }

                        //floate totalWaitTime = totalWaitTime
                        float totalWaitFloat;
                        totalWaitFloat = totalWaitTime;

                        //Write to file string plus temp(0) get Id, plus string, removalTime, plus string, temp(0)
                        // get waitTime, plus string, totalWaitFloat
                        myWriter.write("\n\nProcess removed from queue is: id = " + temp.get(0).getId() + ", at time "
                                + removalTime + ", wait time = " + temp.get(0).getWaitTime() + " Total wait time = " +
                                totalWaitFloat);

                        //Write to file string plus temp(0) get Id, plus string, temp(0) get Priority, plus string
                        //temp(0) get ArrivalTime, plus string, temp(0) get Duration
                        myWriter.write("\nProcess ID = " + temp.get(0).getId() + "\n\tPriority = " +
                                temp.get(0).getPriority() + "\n\tArrival = " + temp.get(0).getArrivalTime() + "\n\tDuration = "
                                + temp.get(0).getDuration());

                        //set running to true
                        running = true;
                    }

                    //Add one to current time
                    currentTime++;

                    //Iterator process type name priorityIter equals priorityQueue iterator
                    Iterator<Process> priorityIter = priorityQueue.iterator();
                    //while iterator has next, Process p = priorityIter.next
                    //P.waitTime = get WaitTime of p plus 1
                    while (priorityIter.hasNext()) {
                        Process p = priorityIter.next();
                        p.waitTime = p.getWaitTime() + 1;
                    }
                }

                //While temp size ot equal to zero
                while (temp.size() != 0) {

                    // if running true and runTime equals temp(0) get Duration
                    if (running == true && runTime == temp.get(0).getDuration()) {
                        //Write to file string plus temp(0) get Id, plus string, and currentTime
                        myWriter.write("\nProcess " + temp.get(0).getId() + " finished at time " +
                                currentTime);

                        //remove from temp
                        temp.remove(0);

                        //running set to fals, runTime set to 1
                        running = false;
                        runTime = 1;
                    }

                    //running is true, add 1 to runTime
                    if (running == true) {
                        runTime++;

                    }
                    //Add 1 to current time
                    currentTime++;

                }
                //float totalWaitFloat equals totalWaitTime, write to file string plus totalWaitFloat
                float totalWaitFloat;
                totalWaitFloat = totalWaitTime;
                myWriter.write("\n\nTotal wait time = " + totalWaitFloat);

                //float avgWaitFloat = totalWaitFloat divided by 10 (number of processes)
                //Write to file string plus avgWaitFloat
                float avgWaitFloat;
                avgWaitFloat = totalWaitFloat / 10;
                myWriter.write("\nTotal wait time = " + avgWaitFloat);

            }
            //close myWriter
            myWriter.close();

            //IO catc exception
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

