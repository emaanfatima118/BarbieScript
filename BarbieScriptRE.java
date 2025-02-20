import java.util.*;

class NFAState {
    int id;
    Map<Character, Set<NFAState>> transitions;

    public NFAState(int id) {
        this.id = id;
        this.transitions = new HashMap<>();
    }

    public void addTransition(char symbol, NFAState state) {
        transitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(state);
    }
}

class barbieNFA {
    NFAState start;
    NFAState accept;

    public barbieNFA(NFAState start, NFAState accept) {
        this.start = start;
        this.accept = accept;
    }
}

class DFAState {
    int id;
    Map<Character, DFAState> transitions;
    boolean isAccepting;

    public DFAState(int id, boolean isAccepting) {
        this.id = id;
        this.transitions = new HashMap<>();
        this.isAccepting = isAccepting;
    }

    public void addTransition(char symbol, DFAState state) {
        transitions.put(symbol, state);
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    @Override
    public String toString() {
        return "State " + id + (isAccepting ? " (Accepting)" : "");
    }
}

class barbieDFA {
    DFAState startState;
    Set<DFAState> allStates;
    Set<DFAState> acceptingStates;

    public barbieDFA(DFAState startState, Set<DFAState> acceptingStates) {
        this.startState = startState;
        this.acceptingStates = acceptingStates;
        this.allStates = new HashSet<>();
        populateAllStates(startState);
    }

    private void populateAllStates(DFAState state) {
        if (!allStates.contains(state)) {
            allStates.add(state);
            for (DFAState nextState : state.transitions.values()) {
                populateAllStates(nextState);
            }
        }
    }
}


public class BarbieScriptRE {
    public static final String IDENTIFIER = "[a-z]+";
    public static final String KEYWORD = "gem|sparkle|glossy|chatter|pinktruth";
    public static final String INTEGER = "\\d+";
    public static final String DECIMAL = "\\d+\\.\\d{1,5}";
    public static final String BOOLEAN = "sotrue|noway";
    public static final String CHARACTER = "'[a-z]|[A-Z]'";
    public static final String ARITHMETIC_OPERATOR = "[+\\-*/%\\^]";
    public static final String EXPONENT = "\\d+(\\.\\d{1,5})?\\^\\d+(\\.\\d{1,5})?";
    public static final String SINGLE_LINE_COMMENT = "#.*?";
    public static final String MULTI_LINE_COMMENT = "\\$/[\\s\\S]*?/\\$";
    public static final String WHITESPACE = "\\s+";
    public static final String STRING = "\".*?\"";
    public static final String INPUT_OUTPUT = "askqueen|showoff";
    public static final String CONSTANT = "fix";
    public static final String GLOBAL = "queenbee";

    public static void main(String[] args) {
        barbieNFA identifierNFA = createKeywordNFA();
        barbieNFA keywordNFA = createKeywordNFA();
        barbieNFA integerNFA = createIntegerNFA();
        barbieNFA decimalNFA = createDecimalNFA();
        barbieNFA booleanNFA = createBooleanNFA();
        barbieNFA characterNFA = createCharacterNFA();
        barbieNFA arithmeticOperatorNFA = createArithmeticOperatorNFA();
        barbieNFA exponentNFA = createExponentNFA();
        barbieNFA singleLineCommentNFA = createSingleLineCommentNFA();
        barbieNFA multiLineCommentNFA = createMultiLineCommentNFA();
        barbieNFA whitespaceNFA = createWhitespaceNFA();
        barbieNFA stringNFA = createStringNFA();
        barbieNFA outputNFA = createOutputNFA();
        barbieNFA inputNFA = createInputNFA();
        barbieNFA constantNFA = createConstantNFA();
        barbieNFA assignmentOperatorNFA = createAssignmentOperatorNFA();
    }
    
    static barbieNFA createIdentifierNFA() {
        barbieNFA nfa = BarbieScriptNFA.createBasicNFA('a');
        for (char c = 'b'; c <= 'z'; c++) {
            nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA(c));
        }
        return BarbieScriptNFA.createKleeneStarNFA(nfa);
    }
    static barbieNFA createKeywordNFA() {
        barbieNFA keywordNFA = BarbieScriptNFA.createSequenceNFA("gem");
        keywordNFA = BarbieScriptNFA.createUnionNFA(keywordNFA, BarbieScriptNFA.createSequenceNFA("sparkle"));
        keywordNFA = BarbieScriptNFA.createUnionNFA(keywordNFA, BarbieScriptNFA.createSequenceNFA("glossy"));
        keywordNFA = BarbieScriptNFA.createUnionNFA(keywordNFA, BarbieScriptNFA.createSequenceNFA("chatter"));
        keywordNFA = BarbieScriptNFA.createUnionNFA(keywordNFA, BarbieScriptNFA.createSequenceNFA("pinktruth"));
        keywordNFA = BarbieScriptNFA.createUnionNFA(keywordNFA, BarbieScriptNFA.createSequenceNFA("queenbee"));
        return keywordNFA;
    }

    static barbieNFA createDelimiterNFA() {
        barbieNFA semicolon = BarbieScriptNFA.createBasicNFA(';');
        barbieNFA comma = BarbieScriptNFA.createBasicNFA(',');
        barbieNFA openParen = BarbieScriptNFA.createBasicNFA('(');
        barbieNFA closeParen = BarbieScriptNFA.createBasicNFA(')');
        return BarbieScriptNFA.createUnionNFA(semicolon,
                BarbieScriptNFA.createUnionNFA(comma, BarbieScriptNFA.createUnionNFA(openParen, closeParen)));
    }

    static barbieNFA createIntegerNFA() {
        barbieNFA digit = BarbieScriptNFA.createBasicNFA('0'); 
        for (char c = '1'; c <= '9'; c++) {
            digit = BarbieScriptNFA.createUnionNFA(digit, BarbieScriptNFA.createBasicNFA(c));
        }
        return BarbieScriptNFA.createConcatenationNFA(digit, BarbieScriptNFA.createKleeneStarNFA(digit));
    }



    static barbieNFA createDecimalNFA() {
        barbieNFA intt = createIntegerNFA();
        barbieNFA dot = BarbieScriptNFA.createBasicNFA('.');
        barbieNFA decc = createIntegerNFA(); 
        return BarbieScriptNFA.createConcatenationNFA(intt, BarbieScriptNFA.createConcatenationNFA(dot, decc));
    }

    static barbieNFA createBooleanNFA() {
        barbieNFA sahi = BarbieScriptNFA.createSequenceNFA("sotrue");
        barbieNFA ghalat = BarbieScriptNFA.createSequenceNFA("noway");
        return BarbieScriptNFA.createUnionNFA(sahi, ghalat);
    }

    static barbieNFA createCharacterNFA() {
        barbieNFA start = BarbieScriptNFA.createBasicNFA('\'');
        barbieNFA charr = createKeywordNFA(); 
        barbieNFA end = BarbieScriptNFA.createBasicNFA('\'');
        return BarbieScriptNFA.createConcatenationNFA(start, BarbieScriptNFA.createConcatenationNFA(charr, end));
    }

    static barbieNFA createArithmeticOperatorNFA() {
        barbieNFA nfa = BarbieScriptNFA.createBasicNFA('+');
        nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA('-'));
        nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA('*'));
        nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA('/'));
        nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA('%'));
        nfa = BarbieScriptNFA.createUnionNFA(nfa, BarbieScriptNFA.createBasicNFA('^'));
        return nfa;
    }
static barbieNFA createAssignmentOperatorNFA() {
        barbieNFA nfa = BarbieScriptNFA.createBasicNFA('=');
        return nfa;
    }

    static barbieNFA createExponentNFA() {
        barbieNFA base = createDecimalNFA(); 
        barbieNFA caret = BarbieScriptNFA.createBasicNFA('^');
        barbieNFA exponent = createDecimalNFA(); 
        return BarbieScriptNFA.createConcatenationNFA(base, BarbieScriptNFA.createConcatenationNFA(caret, exponent));
    }
    static barbieNFA createSingleLineCommentNFA() {
        barbieNFA hash = BarbieScriptNFA.createBasicNFA('#');
        barbieNFA anyChar = BarbieScriptNFA.createKleeneStarNFA(BarbieScriptNFA.createBasicNFA('.')); 
        return BarbieScriptNFA.createConcatenationNFA(hash, anyChar);
    }

    static barbieNFA createMultiLineCommentNFA() {
        barbieNFA dollarSlash1 = BarbieScriptNFA.createBasicNFA('$');
        barbieNFA slash = BarbieScriptNFA.createBasicNFA('/');
        barbieNFA anyChar = BarbieScriptNFA.createKleeneStarNFA(BarbieScriptNFA.createBasicNFA('.')); 
        barbieNFA slash2 = BarbieScriptNFA.createBasicNFA('/');
        barbieNFA dollarSlash2 = BarbieScriptNFA.createBasicNFA('$');
        return BarbieScriptNFA.createConcatenationNFA(dollarSlash1, BarbieScriptNFA.createConcatenationNFA(slash, BarbieScriptNFA.createConcatenationNFA(anyChar, BarbieScriptNFA.createConcatenationNFA(slash2, dollarSlash2))));
    }

    static barbieNFA createWhitespaceNFA() {
        barbieNFA space = BarbieScriptNFA.createBasicNFA(' ');
        barbieNFA tab = BarbieScriptNFA.createBasicNFA('\t');
        barbieNFA newline = BarbieScriptNFA.createBasicNFA('\n');
        barbieNFA whitespace = BarbieScriptNFA.createUnionNFA(space, BarbieScriptNFA.createUnionNFA(tab, newline));
        return BarbieScriptNFA.createKleeneStarNFA(whitespace);
    }

    static barbieNFA createStringNFA() {
        barbieNFA start = BarbieScriptNFA.createBasicNFA('"');
        barbieNFA anyChar = BarbieScriptNFA.createKleeneStarNFA(BarbieScriptNFA.createBasicNFA('.')); 
        barbieNFA end = BarbieScriptNFA.createBasicNFA('"');
        return BarbieScriptNFA.createConcatenationNFA(start, BarbieScriptNFA.createConcatenationNFA(anyChar, end));
    }

    static barbieNFA createOutputNFA() {
        barbieNFA outputNFA = BarbieScriptNFA.createSequenceNFA("showoff");
        return outputNFA;
    }
    static barbieNFA createInputNFA() {
        barbieNFA inputNFA = BarbieScriptNFA.createSequenceNFA("askqueen");
        return inputNFA;
    }
    static barbieNFA createConstantNFA() {
        return BarbieScriptNFA.createSequenceNFA("fix");
    }

    static void printNFA(barbieNFA nfa) {
        System.out.println("Start State: " + nfa.start.id);
        System.out.println("Accept State: " + nfa.accept.id);
      
    }
}