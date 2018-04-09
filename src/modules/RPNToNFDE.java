package modules;

import java.util.LinkedList;
import java.util.Stack;

public class RPNToNFDE {
	
	private NFDENode parentNode;
	
	public RPNToNFDE(){
        this.parentNode = new NFDENode();
    }
	
	public NFDENode getParentNode() {
		return parentNode;
	}
	
	public class NFDENode {
        private static final char EPSILON = 254;
		
		private LinkedList<NFDENode>[] adjacentNodes;
        private boolean isFinal = false;
		
        @SuppressWarnings("unchecked")
		private NFDENode(){
            this.adjacentNodes = new LinkedList[255];
            for(int i = 0; i < 255; i++) {
            	this.adjacentNodes[i] = new LinkedList<NFDENode>();
            }
        }
        
        public LinkedList<NFDENode>[] getAdjacentNodes() {
        	return this.adjacentNodes;
        }
    }
	
	private class RetHelper{
		NFDENode initialNode;
		NFDENode finalNode;
		
		private RetHelper(NFDENode initialNode, NFDENode finalNode) {
			this.initialNode = initialNode;
			this.finalNode = finalNode;
		}
	}
	
	/**
	 * 
	 * @param reversePolishNotation Cadena ya resuelta
	 */
	public NFDENode convert(String reversePolishNotation) {
		NFDENode currentNode = null;
		NFDENode nextNode = null;
		NFDENode n = null;
		NFDENode auxNode = null;
		
		Stack<Character> characterStack = new Stack<>();
		Stack<NFDENode> nodeFactory = new Stack<>();
		RetHelper helper;

		char[] elements = reversePolishNotation.toCharArray();
		char element;
		char aux1;
		char aux2;
		
		for(int i = 0; i < elements.length; i++) {
			element = elements[i];
			switch(element) {
				case ':':
					if(nodeFactory.size() > 1) {
						nextNode = nodeFactory.pop();
						n = nodeFactory.pop();
						auxNode = nodeFactory.pop();
						currentNode = nodeFactory.pop();
						helper = concatNodes(currentNode, auxNode, n, nextNode);
						nodeFactory.push(helper.initialNode);
						nodeFactory.push(helper.finalNode);
					}else if(nodeFactory.size() == 1){
						n = new NFDENode();
						nextNode = nodeFactory.pop();
						aux2 = characterStack.pop();
						nextNode.adjacentNodes[aux2 - ' '].add(n);
						nodeFactory.push(nextNode);
					}else {
						aux2 = characterStack.pop();
						aux1 = characterStack.pop();
						currentNode = new NFDENode();
						auxNode = new NFDENode();
						nextNode = new NFDENode();
						currentNode.adjacentNodes[aux1 - ' '].add(auxNode);
						auxNode.adjacentNodes[aux2 - ' '].add(nextNode);
					}
					break;
				case ',':
					if(nodeFactory.size() > 1) {
						nextNode = nodeFactory.pop();
						n = nodeFactory.pop();
						auxNode = nodeFactory.pop();
						currentNode = nodeFactory.pop();
						helper = mergeNodes(currentNode, auxNode, n, nextNode);
						nodeFactory.push(helper.initialNode);
						nodeFactory.push(helper.finalNode);
					}else if(nodeFactory.size() == 1){
						nextNode = nodeFactory.pop();
						currentNode = nodeFactory.pop();
						aux2 = characterStack.pop();
						currentNode.adjacentNodes[aux2 - ' '].add(nextNode);
					}else {
						aux2 = characterStack.pop();
						aux1 = characterStack.pop();
						currentNode = new NFDENode();
						nextNode = new NFDENode();
						currentNode.adjacentNodes[aux1 - ' '].add(nextNode);
						currentNode.adjacentNodes[aux2 - ' '].add(nextNode);
					}
					break;
				case '*':
					nextNode = nodeFactory.pop();
					currentNode = nodeFactory.pop();
					helper = kleene(currentNode, nextNode);
					nodeFactory.push(helper.initialNode);
					nodeFactory.push(helper.finalNode);
					break;
				case '+':
					nextNode = nodeFactory.pop();
					currentNode = nodeFactory.pop();
					helper = positive(currentNode, nextNode);
					nodeFactory.push(helper.initialNode);
					nodeFactory.push(helper.finalNode);
					break;
				default:
					characterStack.push((char) (element - ' '));
			}
			n = null;
		}
		
		while(!nodeFactory.isEmpty()) {
			n = nodeFactory.pop();
		}
		
		this.parentNode = n;
		return n;
	}
	
	private RetHelper kleene(NFDENode start, NFDENode end) {
		NFDENode newStart = new NFDENode();
		NFDENode newEnd = new NFDENode();
		nodeMerger(newStart, start);
		
		newStart.adjacentNodes[NFDENode.EPSILON].add(newEnd);
		end.adjacentNodes[NFDENode.EPSILON].add(newEnd);
		
		return new RetHelper(newStart, newEnd);
	}
	
	
	private RetHelper positive(NFDENode start, NFDENode end) {
		NFDENode newEnd = new NFDENode();
		end.adjacentNodes[NFDENode.EPSILON].add(newEnd);
		return new RetHelper(start, newEnd);
	}
	
	private void nodeMerger(NFDENode root, NFDENode toMergeRoot) {
		LinkedList<NFDENode> ite;
		for(int i = 0; i < toMergeRoot.adjacentNodes.length; i++) {
			ite = toMergeRoot.adjacentNodes[i];
			for(NFDENode nodeToAdd : ite){
				root.adjacentNodes[i].add(nodeToAdd);
			}
		}
	}
	
	private RetHelper mergeNodes(NFDENode root, NFDENode end, NFDENode toMergeRoot, NFDENode toMergeEnd) {
		nodeMerger(root, toMergeRoot);
		end.isFinal = end.isFinal || toMergeEnd.isFinal;
		toMergeEnd.adjacentNodes[NFDENode.EPSILON].add(end);
		return new RetHelper(root, end);
	}
	
	private RetHelper concatNodes(NFDENode root, NFDENode end, NFDENode toMergeRoot, NFDENode toMergeEnd) {
		end.adjacentNodes[NFDENode.EPSILON].add(toMergeRoot);
		toMergeEnd.isFinal = toMergeEnd.isFinal || end.isFinal;
		return new RetHelper(root, toMergeEnd);
	}
}
