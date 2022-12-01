import java.io.*;
import java.util.*;

public class InputParser {
    private String fileName;
    private ArrayList<State> inputStates;
    private int startIndex;
    private ArrayList<Integer> finalIndex;
    private Set<String> symbols;
    private String from;
    private String symbol;
    private String to;


    public InputParser(String fileName) {
        this.fileName = fileName;
        inputStates = new ArrayList<>();
        symbols = new HashSet<>();
        finalIndex = new ArrayList<>();
        }
    public ArrayList<State> parse() throws IOException {
        System.out.println("parse() function started\n");
        BufferedReader nfaReader = getBufferedReader(fileName);
        System.out.println("Reading first line...");
        String line = nfaReader.readLine();
        System.out.println(line);
        String element = line;

        if (line.equalsIgnoreCase("alphabet")){
            System.out.println("Checking alphabets...");
            element = nfaReader.readLine();
                while (element != null && !element.equalsIgnoreCase("states")){
                    System.out.println(element);
                    symbols.add(element);
                    element = nfaReader.readLine();
                }
        }


            System.out.println("Checking states...");
            element = nfaReader.readLine();
                while (element != null && !element.equalsIgnoreCase("start")){
                    System.out.println(element);
                    State state = new State(element);
                    inputStates.add(state);
                    element = nfaReader.readLine();
                }

            System.out.println("Checking start...");
            int startCount = 0;
            element = nfaReader.readLine();
                while (element != null && !element.equalsIgnoreCase("final")){
                    System.out.println(element);
                    for(State state: inputStates){
                        if(state.getStateID().equals(element) && startCount==0){
                            startIndex = inputStates.indexOf(state);
                            state.setStartState();
                            startCount++;
                            System.out.println("No. of Start states: " + startCount);
                        }
                    }
                    element = nfaReader.readLine();
                }

            System.out.println("Checking final...");
                int finalCount = 0;
            element = nfaReader.readLine();
                while (element != null && !element.equalsIgnoreCase("transitions")){
                    System.out.println(element);
                    for(State state: inputStates){
                        if(state.getStateID().equals(element)){
                            finalIndex.add(inputStates.indexOf(state));
                            state.setFinalState();
                            finalCount++;
                            System.out.println("No. of final states: " + finalCount);
                        }
                    }
                    element = nfaReader.readLine();
                }

            System.out.println("Checking transitions...");
                element = nfaReader.readLine();

                while (element != null && !element.equalsIgnoreCase("end")){
                    System.out.println(element);
                    StringTokenizer token = new StringTokenizer(element);
                    from = token.nextToken();
                    symbol = token.nextToken();
                    to = token.nextToken();

                    for(State fromState: inputStates){
                        if(fromState.getStateID().equals(from)){
                            for(State toState: inputStates){
                                if(toState.getStateID().equals(to)){
                                    fromState.put(symbol, toState);
                                }
                            }
                        }
                    }
                    element = nfaReader.readLine();
                }
        nfaReader.close();
        return inputStates;
    }

    private BufferedReader getBufferedReader(String fileName) {
        BufferedReader nfaReader = null;
        File file = new File (fileName);
        try {
            nfaReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return nfaReader;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<State> getInputStates() {
        return inputStates;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public ArrayList<Integer> getFinalIndex() {
        return finalIndex;
    }

    public Set<String> getSymbols() {
        return symbols;
    }
}
