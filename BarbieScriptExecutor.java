import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BarbieScriptExecutor {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java BarbieScriptExecutor <filename>");
            return;
        }

        String fileName = args[0];
        List<String> tokens = new ArrayList<>();
        ErrorHandler errorHandler = new ErrorHandler();
        TokenLexer TokenLexer = new TokenLexer();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tokens.addAll(TokenLexer.tokenize(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading " + fileName + ": " + e.getMessage());
            return;
        }

        errorHandler.validateTokens(tokens);
    }
    // public static void main(String[] args) {
    //     String barbieScriptCode = """
    //    fix sparkle = 25
    //     fix glossy = ( 3.141596 )
    //     queenbee pinktruth = sotrue
    //     chatter message = "Hello, Barbie!" 
    //     askqueen Input # Taking input
    //     showoff message # Displaying output
    //     sparkle = sparkle + 5 
    //     glossy = glossy ^ 2 
    //     $/ This is
    //     a multiline
    //     comment /$
    //     """;

    //     TokenLexer tokenLexer = new TokenLexer();
    //     List<String> tokens = tokenLexer.tokenize(barbieScriptCode);
    //     for (String token : tokens) {
    //         System.out.println(token);
    //     }
    //     tokenLexer.printSymbolTable();
    // }
}