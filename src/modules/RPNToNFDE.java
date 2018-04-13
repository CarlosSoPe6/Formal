package modules;

import java.util.Stack;

public class RPNToNFDE {

    private static int currentNode;

    /**
     * Node util
     */
    public class State {

        /**
         * Stores the char that points the text node
         */
        private char c;

        /**
         * Stores the first out
         */
        private State out0;

        /**
         * Stores the second out
         */
        private State out1;

        /**
         * Matrix mapper
         */
        private int stateNumber;

        /**
         * The empty character
         */
        private static final char EPSILON = 254;

        /**
         * Creates a new state
         * @param c the char
         * @param out0 The first output
         * @param out1 The second output
         */
        private State(char c, State out0, State out1){
            this.c = c;
            this.out0 = out0;
            this.out1 = out1;
            this.stateNumber = currentNode++;
        }

        /**
         * Gets the state number
         * @return The current node state number
         */
        public int getStateNumber() {
            return stateNumber;
        }
    }

    /**
     * Helper of nodes
     */
    private class NodeFactory {
        /**
         *
         */
        private State start;

        /**
         *
         */
        private State[] outs;

        /**
         * Creates a new NodeFactory
         * @param s Start
         * @param outs The current outputs
         */
        private NodeFactory(State s, State ... outs){
            this.start = s;
            this.outs = outs;
        }
    }

    /**
     * Converts a reversed polish notation string to a NFA.
     * @param post Regex in reverse polish notation
     * @return The NFAs root state
     */
    public State convert(String post){
        char element;

        // Stores the previous states
        Stack<NodeFactory> nodeFactoryStack = new Stack<>();

        NodeFactory f = null;
        NodeFactory e2;
        NodeFactory e1;

        State s;
        State split;

        // Iterate over the string post
        for(int i = 0; i < post.length(); i++){
            element = post.charAt(i);
            switch (element){
                // Concatenation
                case ':':
                    e2 = nodeFactoryStack.pop();
                    e1 = nodeFactoryStack.pop();
                    // TODO: Concat languages
                    f = new NodeFactory(e1.start, e2.outs);
                    nodeFactoryStack.push(f);
                    break;

                // Union
                case ',':
                    e2 = nodeFactoryStack.pop();
                    e1 = nodeFactoryStack.pop();
                    // TODO: Union languages
                    // nodeFactoryStack.push(f);
                    break;

                // Zero or more
                case '*':
                    e1 = nodeFactoryStack.pop();
                    // TODO: Kleene
                    // nodeFactoryStack.push(f);
                    break;

                // One or more
                case '+':
                    e1 = nodeFactoryStack.pop();
                    // TODO: Positive
                    // nodeFactoryStack.push(f);
                    break;

                // Normal character
                default:
                    // Create  a new node and add it to nodeFactory
                    s = new State(element, null, null);
                    f = new NodeFactory(s, s.out0);
                    nodeFactoryStack.push(f);
                    break;

            }
        }

        while(!nodeFactoryStack.isEmpty()){
            f = nodeFactoryStack.pop();
        }

        return f.start;
    }

    /**
     * Merges two lists
     * @param a first list
     * @param b second list
     * @return Two lists merged
     */
    private State[] stateMerge(State[] a, State[] b){
        State[] s = new State[a.length + b.length];

        System.arraycopy(a, 0, s, 0, a.length);
        System.arraycopy(b, a.length, s, a.length, b.length);

        return s;
    }
}
