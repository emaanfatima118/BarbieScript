import java.util.*;

public class BarbieScriptDFA {

    private static Set<NFAState> computeEpsilonClosure(Set<NFAState> states) {
        Set<NFAState> closureSet = new HashSet<>(states);
        Deque<NFAState> stack = new ArrayDeque<>(states);

        while (!stack.isEmpty()) {
            NFAState currentState = stack.pop();
            for (NFAState nextState : currentState.transitions.getOrDefault('ε', Collections.emptySet())) {
                if (!closureSet.contains(nextState)) {
                    closureSet.add(nextState);
                    stack.push(nextState);
                }
            }
        }
        return closureSet;
    }

    private static Set<NFAState> computeMove(Set<NFAState> states, char symbol) {
        Set<NFAState> nextStates = new HashSet<>();
        for (NFAState state : states) {
            nextStates.addAll(state.transitions.getOrDefault(symbol, Collections.emptySet()));
        }
        return nextStates;
    }

    public static barbieDFA transformNFAtoDFA(barbieNFA nfa) {
        int stateCounter = 0;
        Map<Set<NFAState>, DFAState> stateMapping = new HashMap<>();
        Queue<Set<NFAState>> processingQueue = new LinkedList<>();

        Set<NFAState> initialClosure = computeEpsilonClosure(Collections.singleton(nfa.start));
        DFAState initialDFAState = new DFAState(stateCounter++, initialClosure.contains(nfa.accept));
        stateMapping.put(initialClosure, initialDFAState);
        processingQueue.add(initialClosure);

        while (!processingQueue.isEmpty()) {
            Set<NFAState> activeStates = processingQueue.poll();
            DFAState currentDFAState = stateMapping.get(activeStates);

            char symbol = 32;
            while (symbol <= 126) {
                Set<NFAState> transitionStates = computeEpsilonClosure(computeMove(activeStates, symbol));
                if (!transitionStates.isEmpty()) {
                    DFAState resultingDFAState = stateMapping.get(transitionStates);
                    if (resultingDFAState == null) {
                        resultingDFAState = new DFAState(stateCounter++, transitionStates.contains(nfa.accept));
                        stateMapping.put(transitionStates, resultingDFAState);
                        processingQueue.add(transitionStates);
                    }
                    currentDFAState.addTransition(symbol, resultingDFAState);
                }
                symbol++;
            }
        }

        Set<DFAState> finalStates = new HashSet<>();
        for (DFAState state : stateMapping.values()) {
            if (state.isAccepting) {
                finalStates.add(state);
            }
        }

        barbieDFA dfa = new barbieDFA(initialDFAState, finalStates);
        printCorrectedTransitionTable(dfa);
        return dfa;
    }

    public static void printCorrectedTransitionTable(barbieDFA dfa) {
        Set<Character> inputSymbols = new TreeSet<>();
        for (DFAState state : dfa.allStates) {
            inputSymbols.addAll(state.transitions.keySet());
        }

        // Print header row with inputs
        System.out.println("DFA Transition Table:");
        System.out.print("State | ");
        for (Character input : inputSymbols) {
            System.out.printf("%-4s | ", "'" + input + "'");
        }
        System.out.println("\n" + "-".repeat(10 + (inputSymbols.size() * 7)));

        for (DFAState state : dfa.allStates) {
            System.out.printf("%-6d| ", state.id);
            for (Character input : inputSymbols) {
                DFAState nextState = state.transitions.get(input);
                System.out.printf("%-4s | ", (nextState != null ? nextState.id : "-"));
            }
            System.out.println();
        }
        System.out.println("-".repeat(10 + (inputSymbols.size() * 8)));
    }
}
