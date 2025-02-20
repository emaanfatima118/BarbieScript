import java.util.*;

public class ErrorHandler {
    private List<String> errors;
    private boolean inMultilineComment;
    private Stack<Integer> parenthesisStack;

    public ErrorHandler() {
        errors = new ArrayList<>();
        inMultilineComment = false;
        parenthesisStack = new Stack<>();
    }

    public void validateTokens(List<String> tokens) {
        int lineNumber = 1;

        for (String token : tokens) {
            String[] parts = token.split(": ", 2);
            if (parts.length < 2) continue;

            String type = parts[0].trim();
            String value = parts[1].trim();

            if (!inMultilineComment) {
                switch (type) {
                    case "IDENTIFIER":
                        checkIdentifier(value, lineNumber);
                        break;
                    case "DECIMAL":
                        checkDecimalPrecision(value, lineNumber);
                        break;
                    case "BRACKET":
                        trackParentheses(value, lineNumber);
                        break;
                    case "STRING":
                        checkStringErrors(value, lineNumber);
                        break;
                    case "CHARACTER":
                        checkCharacterErrors(value, lineNumber);
                        break;
                }
            }

            if (type.equals("MULTILINE_COMMENT")) {
                if (value.startsWith("$/") && !value.endsWith("/$")) {
                    errors.add("Unmatched start of multi-line comment at line " + lineNumber);
                    inMultilineComment = true;
                } else if (value.endsWith("/$") && !value.startsWith("$/")) {
                    if (!inMultilineComment) {
                        errors.add("Unmatched end of multi-line comment at line " + lineNumber);
                    }
                    inMultilineComment = false;
                }
            }

            if (type.equals("NEWLINE")) {
                lineNumber++;
            }
        }

        checkUnmatchedParentheses();
        reportErrors(tokens);
    }

    private void checkIdentifier(String word, int lineNumber) {
        if (!word.matches("[a-z]+")) {
            errors.add("Invalid identifier '" + word + "' at line " + lineNumber + ". Identifiers must be lowercase.");
        }
    }

    private void checkDecimalPrecision(String word, int lineNumber) {
        if (word.matches("\\d+\\.\\d{6,}")) {
            errors.add("Decimal '" + word + "' exceeds the allowed precision of 5 decimal places at line " + lineNumber);
        }
    }

    private void checkStringErrors(String value, int lineNumber) {
        if (!value.startsWith("\"") || !value.endsWith("\"")) {
            errors.add("Syntax Error: Unmatched string literal at line " + lineNumber);
        }
    }

    private void checkCharacterErrors(String value, int lineNumber) {
        if (!value.matches("'\\w'")) {
            errors.add("Syntax Error: Invalid character literal at line " + lineNumber);
        }
    }

    private void trackParentheses(String bracket, int lineNumber) {
        if (bracket.equals("(")) {
            parenthesisStack.push(lineNumber);
        } else if (bracket.equals(")")) {
            if (parenthesisStack.isEmpty()) {
                errors.add("Unmatched closing parenthesis at line " + lineNumber);
            } else {
                parenthesisStack.pop();
            }
        }
    }

    private void checkUnmatchedParentheses() {
        while (!parenthesisStack.isEmpty()) {
            int line = parenthesisStack.pop();
            errors.add("Unmatched opening parenthesis at line " + line);
        }
    }

    private void reportErrors(List<String> tokens) {
        if (!errors.isEmpty()) {
            System.out.println("Syntax Errors Found:");
            for (String error : errors) {
                System.err.println(error);
            }
            System.out.println("Compilation failed due to syntax errors.");
            System.exit(1);
        } else {
            System.out.println("No syntax errors found. Tokenized output:");
            for (String token : tokens) {
                System.out.println(token);
            }
        }
    }
}
