import java.util.*;
 

class TokenLexer {
    private final Map<String, barbieDFA> dfaTable;
        private boolean inMultilineComment = false;
        private static final String RED = "\033[31m"; 
        private static final String RESET = "\033[0m";
        private final SymbolTable symbolTable;

    public TokenLexer() {
        dfaTable = new HashMap<>();
        symbolTable = new SymbolTable();

        dfaTable.put("KEYWORD", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createKeywordNFA()));
        dfaTable.put("INPUT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createInputNFA()));
        dfaTable.put("OUTPUT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createOutputNFA()));
        dfaTable.put("CONSTANT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createConstantNFA()));
        dfaTable.put("INTEGER", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createIntegerNFA()));
        dfaTable.put("DECIMAL", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createDecimalNFA()));
        dfaTable.put("BOOLEAN", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createBooleanNFA()));
        dfaTable.put("CHARACTER", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createCharacterNFA()));
        dfaTable.put("ARITHMETIC_OPERATOR", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createArithmeticOperatorNFA()));
        dfaTable.put("EXPONENT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createExponentNFA()));
        dfaTable.put("ONELINE_COMMENT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createSingleLineCommentNFA()));
        dfaTable.put("MULTILINE_COMMENT", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createMultiLineCommentNFA()));
        dfaTable.put("WHITESPACE", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createWhitespaceNFA()));
        dfaTable.put("STRING", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createStringNFA()));
        dfaTable.put("ASSIGNMENT_OPERATOR", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createAssignmentOperatorNFA()));
        dfaTable.put("IDENTIFIER", BarbieScriptDFA.transformNFAtoDFA(BarbieScriptRE.createIdentifierNFA()));
    }

public List<String> tokenize(String input) {
    List<String> tokens = new ArrayList<>();
    Stack<Integer> bracketStack = new Stack<>();
    int i = 0;
    int lineNumber = 1;

    while (i < input.length()) {
        boolean matched = false;

        if (input.charAt(i) == '\n') {
            lineNumber++; 
            i++;
            continue;
        }
        
        if (input.charAt(i) == '"') { 
            int start = i;
            i++; 
            boolean closed = false;

            while (i < input.length()) {
                if (input.charAt(i) == '"') {
                    closed = true;
                    i++; 
                    break;
                }
                if (input.charAt(i) == '\n') lineNumber++; 
                i++;
            }

            String stringLiteral = input.substring(start, i);

            if (closed) {
                tokens.add("STRING: " + stringLiteral);
            } else {
                tokens.add(RED+"ERROR at line " + lineNumber + ": Unclosed string -> " + stringLiteral+RESET);
            }

            matched = true;
        }

        else if (input.charAt(i) == '(') {
            bracketStack.push(i);
            tokens.add("BRACKET: (");
            i++;
            matched = true;
        }
        else if (input.charAt(i) == ')') {
            if (!bracketStack.isEmpty()) {
                bracketStack.pop();
                tokens.add("BRACKET: )");
            } else {
                tokens.add(RED+"ERROR at line " + lineNumber + ": Unmatched )"+RESET);
            }
            i++;
            matched = true;
        }
        else if (input.startsWith("askqueen", i)) {
            tokens.add("INPUT: askqueen");
            i += 8; 
            matched = true;
        }
        else if (input.startsWith("showoff", i)) {
            tokens.add("OUTPUT: showoff");
            i += 7; 
            matched = true;
        }

        else if (input.startsWith("$/", i)) {
            int start = i;
            i += 2;
         
            boolean closed = false;

            while (i < input.length() - 1) {
                if (input.charAt(i) == '\n') lineNumber++; 
                if (input.charAt(i) == '/' && input.charAt(i + 1) == '$') {
                    closed = true;
                    i += 2;
                    break;
                }
                i++;
            }

            if (closed) {
                tokens.add("MULTILINE_COMMENT: " + input.substring(start, i));
            } else {
                tokens.add(RED+"ERROR at line " + lineNumber + ": Unclosed multi-line comment -> " + input.substring(start, i)+RESET);
            }
            matched = true;
        }

        else if (Character.isDigit(input.charAt(i))) {
            int start = i;
            boolean hasDecimal = false;
            int decimalPlaces = 0;

            while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                if (input.charAt(i) == '.') {
                    if (hasDecimal) break; 
                    hasDecimal = true;
                } else if (hasDecimal) {
                    decimalPlaces++;
                }
                i++;
            }

            String number = input.substring(start, i);

            if (hasDecimal) {
                if (decimalPlaces > 5) {
                    tokens.add(RED+"ERROR at line " + lineNumber + ": Decimal precision exceeds limit -> " + number+RESET);
                } else {
                    tokens.add("DECIMAL: " + number);
                }
            } else {
                tokens.add("INTEGER: " + number);
            }

            matched = true;
        }

        else if (input.charAt(i) == '#') {
            StringBuilder comment = new StringBuilder("#"); 
            i++;

            while (i < input.length() && input.charAt(i) != '\n') { 
                comment.append(input.charAt(i));
                i++;
            }

            tokens.add("ONELINE_COMMENT: " + comment.toString());
            matched = true;
        }

        else if (input.charAt(i) == '\'') { 
            int start = i;
            i++;

            if (i < input.length() && input.charAt(i) != '\'') {
                char charInside = input.charAt(i);
                i++;

                if (i < input.length() && input.charAt(i) == '\'') {
                    tokens.add("CHARACTER: '" + charInside + "'");
                    i++;
                } else {
                    tokens.add(RED+"ERROR at line " + lineNumber + ": Invalid character literal -> '" + charInside+RESET);
                }
            } else { 
                tokens.add(RED+"ERROR at line " + lineNumber + ": Missing character inside single quotes"+RESET);
            }
            
            matched = true;
        }

        else {
            for (Map.Entry<String, barbieDFA> entry : dfaTable.entrySet()) {
                String tokenType = entry.getKey();
                barbieDFA dfa = entry.getValue();

                int matchLength = matchDFA(dfa, input, i);
                if (matchLength > 0) {
                    String token = input.substring(i, i + matchLength);
                    i += matchLength;

                    if (!tokenType.equals("WHITESPACE")) {
                        tokens.add(tokenType + ": " + token);
                        if (tokenType.equals("ONELINE_COMMENT")) {
                            while (i < input.length() && input.charAt(i) != '\n') {
                                i++;
                            }
                        }
                    }

                    matched = true;
                    break;
                }
            }
        }

        if (!matched) {
            int start = i;

            while (i < input.length() && Character.isLetterOrDigit(input.charAt(i))) {
                i++;
            }

            String token = input.substring(start, i);

            if (token.matches("[a-z][a-z]*")) {  
                tokens.add("IDENTIFIER: " + token);
            } else {
                tokens.add(RED+"ERROR at line " + lineNumber + ": Invalid identifier -> " + token+RESET);
            }
        }

    }

    return tokens;
}

   
 public void printSymbolTable() {
       symbolTable.printSymbolTable();
    }

    private int matchDFA(barbieDFA dfa, String input, int start) {
        DFAState currentState = dfa.startState;
        int length = 0;
        int lastAcceptingLength = 0;

        for (int i = start; i < input.length(); i++) {
            char c = input.charAt(i);

            if (currentState.transitions.containsKey(c) || currentState.transitions.containsKey('\n')) {
                currentState = currentState.transitions.getOrDefault(c, currentState.transitions.get('\n'));
                length++;

                if (currentState.isAccepting) {
                    lastAcceptingLength = length; 
                }
            } else {
                break; 
            }
        }

        return lastAcceptingLength; 
    }


}
