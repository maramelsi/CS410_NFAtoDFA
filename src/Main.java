import java.io.IOException;

public class Main {
    private static NFAtoDFAConverter converter;

    static {
        converter = new NFAtoDFAConverter();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Converting NFA to DFA");
        converter.convert();
    }
}
