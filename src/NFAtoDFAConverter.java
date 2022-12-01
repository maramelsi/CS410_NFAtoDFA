import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NFAtoDFAConverter {
    private InputParser inputParser;
    private String fileName;

    private ArrayList<State> parsedInput;
    private ArrayList<State> outputStates;
    private LinkedList<State> statesQueue;
    private Map<String, HashSet<State>> dfaTransitions;


    public NFAtoDFAConverter() {
        parsedInput = new ArrayList<>();
        outputStates = new ArrayList<>();
        statesQueue = new LinkedList<>();
        dfaTransitions = new HashMap<>();

    }

    public void convert() throws IOException {
        // Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name: ");
        //fileName = scanner.nextLine();
        inputParser = new InputParser("NFA1.txt");
        parsedInput = inputParser.parse();

        System.out.println("Printing NFA transition table...");
        for(State state: parsedInput) {
            state.printTransitions();
        }

        //main logic
        statesQueue.add(parsedInput.get(inputParser.getStartIndex()));

        //entry is the transition table for the first state in the queue. It should contain an entry for each symbol.
        int symbolSize = inputParser.getSymbols().size();
        System.out.println("No. of symbols: "+ symbolSize);
        int loopCounter = 0;
        while((loopCounter==0) && !statesQueue.isEmpty()){
            for(Map.Entry<String, HashSet<State>> entry: statesQueue.getFirst().getTransitions().entrySet()){
                loopCounter++;
                System.out.println("Taking first element of the queue: " + statesQueue.getFirst());
                //We join all values in the Hashset for "every" key i.e. concatenate the states
                String concatState = entry.getValue().stream().map(Object::toString).collect(Collectors.joining(""));
                System.out.println("Transition from state: " +statesQueue.getFirst().getStateID()+" at symbol: " + entry.getKey() + " to state: " + concatState);
                State newState = new State(concatState);

                char[] splitStates = concatState.toCharArray();

                String tempString = "";
                State tempState;
                HashSet<State> tempStates = new HashSet<>();
                Map<String, HashSet<State>> newTransitions =  new HashMap<>();

                for (String symbol: inputParser.getSymbols()) {
                    Boolean isFinal = false;
                    for(char splitState : splitStates){
                        for (State inputState : parsedInput) {
                            if (inputState.getStateID().equals(Character.toString(splitState))) {
                                //tempString += inputState.getTransitions().get(symbol).stream().map(Object::toString).collect(Collectors.joining(""));
                                if(inputState.isFinalState()){
                                    isFinal = true;
                                }
                                System.out.println("Transition from state: "+ concatState + " at symbol: "+symbol + " to state: "+ tempString);
                            }
                        }
                        if(isFinal){
                            newState.setFinalState();
                        }
                    }
                    //we need to clean up duplicate states
                    tempString = removeDuplicateAndSort(tempString);
                    tempState = new State(tempString);
                    tempStates.add(tempState);
                    newTransitions.put(symbol, tempStates);
                }

                newState.setTransitions(newTransitions);

                if(!containsElement(concatState) && !concatState.isEmpty()){
                    statesQueue.add(newState);
                    System.out.println("Added "+ newState.getStateID() + " state to the queue.");
                }

                statesQueue.getFirst().replace(entry.getKey(), newState);
                if(loopCounter==symbolSize){
                    outputStates.add(statesQueue.poll());
                    System.out.println("Printing DFA transition table...");
                    for(State state: outputStates) {
                        state.printTransitions();
                    }
                    loopCounter=0;
                }

            }
        }
        printToFile("output.txt");

        System.out.println("Done.");
    }

    void printToFile(String fileName){
        try {
            File newFile = new File(fileName);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("ALPHABET\n");
            System.out.println("ALPHABET");
            for(String input : inputParser.getSymbols()){
                myWriter.write(input+"\n");
                System.out.println(input);
            }

            myWriter.write("STATES\n");
            System.out.println("STATES");
            for(State state : outputStates){
                myWriter.write(state.getStateID()+"\n");
                System.out.println(state.getStateID());
            }
            myWriter.write("START\n");
            System.out.println("START");
            for(State state : outputStates){
                if(state.isStartState()){
                    myWriter.write(state.getStateID()+"\n");
                    System.out.println(state.getStateID());
                }
            }

            myWriter.write("FINAL\n");
            System.out.println("FINAL");
            for(State state : outputStates){
                if(state.isFinalState()){
                    myWriter.write(state.getStateID()+"\n");
                    System.out.println(state.getStateID());
                }
            }
            myWriter.write("TRANSITIONS\n");
            System.out.println("TRANSITIONS");
            for(State state : outputStates){
                state.printTransitions();
                state.writeTransitions(myWriter);
            }
            myWriter.write("END\n");
            System.out.println("END");

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    static String removeDuplicateAndSort(String string)
    {
        //Creating index variable to use it as index in the modified string
        char str[] = string.toCharArray();
        int length = str.length;
        int index = 0;

        // Traversing character array
        for (int i = 0; i < length; i++)
        {

            // Check whether str[i] is present before or not
            int j;
            for (j = 0; j < i; j++)
            {
                if (str[i] == str[j])
                {
                    break;
                }
            }

            // If the character is not present before, add it to resulting string
            if (j == i)
            {
                str[index++] = str[i];
            }
        }
        System.out.println(String.valueOf(Arrays.copyOf(str, index)));
        Arrays.sort(str);
        return (new String(str));
    }
    Boolean containsElement(String concatState){
        if (outputStates.isEmpty()) {

            for (State state1 : statesQueue) {
                if (state1.getStateID().equals(concatState)) {
                    return true;
                }

            }

        } else {
            for (State state1 : statesQueue) {
                for (State state2 : outputStates) {
                    if (state1.getStateID().equals(concatState) || state2.getStateID().equals(concatState)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
