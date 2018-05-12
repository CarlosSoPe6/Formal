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

        private State(){

        }

        /**
         * Creates a new state
         * @param c The char
         */
        private State(char c){
            this.c = c;
            this.out0 = getNill();
            this.out1 = getNill();
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

        private State getNill(){
            return new State();
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
        }

        /**
         * Creates a new NodeFactory with the referenced linked lists
         * @param s
         * @param list
         */
        private NodeFactory(State s, LinkedList<State> list){
            this.start = s;
            this.outs = list;
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
                    // TODO: Lists
                    concat(e1.outs, e2.start);
                    f = new NodeFactory(e1.start, e2.outs);
                    nodeFactoryStack.push(f);
                    break;

                // Union
                case ',':
                    e2 = nodeFactoryStack.pop();
                    e1 = nodeFactoryStack.pop();
                    split = new State(State.EPSILON, e1.start, e2.start);
                    // TODO: Lists
                    f = new NodeFactory(split, union(e1.outs, e2.outs));
                    nodeFactoryStack.push(f);
                    break;

                // Zero or more
                case '*':
                    e1 = nodeFactoryStack.pop();
                    split = new State(State.EPSILON, e1.start, null);
                    // TODO: Lists
                    kleene(e1.outs);
                    f = new NodeFactory(split, null);
                    nodeFactoryStack.push(f);
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
                    f = new NodeFactory(s);
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
     * Concat two nodes. Connects all the nodes outputList to e2
     * @param outputList Nodes to change
     * @param e2 Next node
     */
    private void concat(LinkedList<State> outputList, State e2){
        Iterator<State> ite = outputList.iterator();
        State iteratorElement;

        while(ite.hasNext()){
            iteratorElement = ite.next();
            iteratorElement.out1 = e2;
            iteratorElement.out0 = null;
        }
    }

    private LinkedList<State> union(LinkedList<State> e1, LinkedList<State> e2){
        LinkedList<State> newList = new LinkedList<>();

        newList.addAll(e1);
        newList.addAll(e2);

        return newList;
    }

    private void kleene(LinkedList<State> e){

    }

    private void positive() {

    }
}