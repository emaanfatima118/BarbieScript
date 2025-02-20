public class BarbieScriptNFA {

    // Create an barbieNFA for a single character input
    public static barbieNFA createBasicNFA(char symbol) {
        NFAState initialState = new NFAState(0);
        NFAState finalState = new NFAState(1);
        initialState.addTransition(symbol, finalState);
        return new barbieNFA(initialState, finalState);
    }

    // Generate an barbieNFA for concatenation of two NFAs
    public static barbieNFA createConcatenationNFA(barbieNFA firstNFA, barbieNFA secondNFA) {
        firstNFA.accept.addTransition('ε', secondNFA.start);
        return new barbieNFA(firstNFA.start, secondNFA.accept);
    }

    // Generate an barbieNFA for union operation (e.g., a|b)
    public static barbieNFA createUnionNFA(barbieNFA firstNFA, barbieNFA secondNFA) {
        NFAState initialState = new NFAState(0);
        NFAState finalState = new NFAState(1);

        initialState.addTransition('ε', firstNFA.start);
        initialState.addTransition('ε', secondNFA.start);

        firstNFA.accept.addTransition('ε', finalState);
        secondNFA.accept.addTransition('ε', finalState);

        return new barbieNFA(initialState, finalState);
    }

    // Generate an barbieNFA for Kleene star operation (e.g., a*)
    public static barbieNFA createKleeneStarNFA(barbieNFA baseNFA) {
        NFAState initialState = new NFAState(0);
        NFAState finalState = new NFAState(1);

        initialState.addTransition('ε', baseNFA.start);
        initialState.addTransition('ε', finalState);

        baseNFA.accept.addTransition('ε', baseNFA.start);
        baseNFA.accept.addTransition('ε', finalState);

        return new barbieNFA(initialState, finalState);
    }

    // Generate an barbieNFA for a given sequence of characters
    public static barbieNFA createSequenceNFA(String inputSequence) {
        barbieNFA nfa = createBasicNFA(inputSequence.charAt(0));
        for (int i = 1; i < inputSequence.length(); i++) {
            nfa = createConcatenationNFA(nfa, createBasicNFA(inputSequence.charAt(i)));
        }
        return nfa;
    }
}
