package modules;

import java.util.LinkedList;
import java.util.Stack;

public class RPNToNFDE {
	
	private NFDENode parentNode;
	
	public RPNToNFDE(){
        this.parentNode = new NFDENode();
    }
	
	private class NFDENode {
        private static final char EPSILON = 254;
		
		private LinkedList<NFDENode>[] adjacentNodes;
        
        private NFDENode(){
            this.adjacentNodes = new LinkedList[255];
            for(int i = 0; i < 255; i++) {
            	this.adjacentNodes[i] = new LinkedList<NFDENode>();
            }
        }
    }
	
	public void convert(String reversePolishNotation) {
		NFDENode currentNode;
		NFDENode nextNode;
		NFDENode auxNode;
		
		Stack<Character> characterStack = new Stack<>();
		Stack<NFDENode> nodeFactory = new Stack<>();
		
		char[] elements = reversePolishNotation.toCharArray();
		char element;
		char aux1;
		char aux2;
		
		for(int i = 0; i < elements.length; i++) {
			element = elements[i];
			switch(element) {
				case '•':
					break;
				case ',':
					break;
				case '*':
					break;
				case '+':
					break;
				default:
					characterStack.push(element);
			}
		}
	}
}
