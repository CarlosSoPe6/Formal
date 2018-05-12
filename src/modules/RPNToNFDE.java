package modules;

import java.util.Iterator;
import java.util.LinkedList;
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
        private final char c;

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

        private State(){
            c = 0;
        }

        /**
         * Creates a new state
         * @param c The char
         */
        private State(char c){
            this.c = c;
            this.out0 = null;
            this.out1 = null;
        }

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

        public State getOut0() {
            return out0;
        }

        public State getOut1() {
            return out1;
        }

        public char getC() {
            return c;
        }
    }

    /**
     * Helper of nodes
     */
    private class NodeFactory {
        /**
         * Points at the start state for the fragment
         */
        private State start;

        /**
         * List of pointers to State that are not yet connected to anything
         */
        private LinkedList<State> outs;

        /**
         * Creates a new NodeFactory with a empty outs list
         * @param s Start
         */
        private NodeFactory(State s){
            this.start = s;
            this.outs = new LinkedList<>();
            this.outs.add(s);
        }

        private NodeFactory(State s, State out){
            this.start = s;
            this.outs = new LinkedList<>();
            this.outs.add(out);
        }

        private NodeFactory(State s, LinkedList<State> outs){
            this.start = s;
            this.outs = new LinkedList<>();
            this.outs.addAll(outs);
        }

    }

    /**
     * Converts a reversed polish notation string to a NFA.
     * @param regex Regex in reverse polish notation
     * @return The NFAs root state
     */
    public State convert(String regex){
        char element;

        // Stores the previous states
        Stack<NodeFactory> nodeFactoryStack = new Stack<>();

        NodeFactory f = null;
        NodeFactory e2;
        NodeFactory e1;

        State s;
        State split;

        // Iterate over the string regex
        for(int i = 0; i < regex.length(); i++){
            element = regex.charAt(i);
            switch (element){
                // Concatenation
                case ':':
                    e2 = nodeFactoryStack.pop();
                    e1 = nodeFactoryStack.pop();
                    concat(e1.outs, e2.start);
                    f = new NodeFactory(e1.start, e2.outs);
                    nodeFactoryStack.push(f);
                    break;
                // Union
                case ',':
                    e2 = nodeFactoryStack.pop();
                    e1 = nodeFactoryStack.pop();
                    split = new State(State.EPSILON, e1.start, e2.start);
                    f = new NodeFactory(split, joinOuts(e1.outs, e2.outs));
                    nodeFactoryStack.push(f);
                    break;

                // Zero or more
                case '*':
                    e1 = nodeFactoryStack.pop();
                    split = new State(State.EPSILON, e1.start, null);
                    concat(e1.outs, split);
                    f = new NodeFactory(split, split);
                    nodeFactoryStack.push(f);
                    break;

                // One or more
                case '+':
                    e1 = nodeFactoryStack.pop();
                    split = new State(State.EPSILON, e1.start, null);
                    concat(e1.outs, split);
                    f = new NodeFactory(e1.start, split);
                    nodeFactoryStack.push(f);
                    break;

                // Alphabet character
                default:
                    // Create  a new node and add it to nodeFactory
                    s = new State(element, null, null);
                    f = new NodeFactory(s);
                    nodeFactoryStack.push(f);
                    break;
            }
        }


        return nodeFactoryStack.pop().start;
    }

    /**
     * Concat two nodes. Connects all the nodes outputList to e2
     * @param l List of nodes
     * @param e2 Next node
     */
    private void concat(LinkedList<State> l, State e2){
        for(State s : l){
            if(s.out0 == null){
                s.out0 = e2;
            }
            if(s.out1 == null){
                s.out1 = e2;
            }
        }
    }

    private LinkedList<State> joinOuts(LinkedList<State> s1, LinkedList<State> s2){
        LinkedList<State> ret = new LinkedList<>();
        ret.addAll(s1);
        ret.addAll(s2);
        return ret;
    }
}