package modules;

import java.util.LinkedList;

public class NFDeToNFD {
	
	private static final char EPSILON = 254;
	private LinkedList<Integer>[][] transitionMatrix = null;
	
	public NFDeToNFD(LinkedList<Integer>[][] tMatrix) {
		this.transitionMatrix = tMatrix;
	}
	
	// Algoritmos para hacer la conversión de la matriz
	
	@SuppressWarnings("unchecked")
	private LinkedList<Integer> eTransition(LinkedList<Integer> list) {
		LinkedList<Integer> statesSet = (LinkedList<Integer>) list.clone();
		boolean repeat = false;
		for(int i = 0; i < list.size(); i++) {
			int j = 0;
			while(j < this.transitionMatrix[list.get(i)][NFDeToNFD.EPSILON].size()) {
				if(!statesSet.contains(this.transitionMatrix[list.get(i)][NFDeToNFD.EPSILON].get(j))) {
					statesSet.add(this.transitionMatrix[list.get(i)][NFDeToNFD.EPSILON].get(j));
					repeat = true;
				}
				j ++;
			}
		}
		if(!repeat) return statesSet;
		return eTransition(statesSet);
	}
	
	@SuppressWarnings("unchecked")
	private LinkedList<Integer> lowerDeltaFunc(LinkedList<Integer> list, char c) {
		LinkedList<Integer> statesSet = new LinkedList<Integer>();
		for(int i = 0; i < list.size(); i++) {
			if(statesSet.isEmpty())
				statesSet = (LinkedList<Integer>) this.transitionMatrix[list.get(i)][c].clone();
			else {
				int j = 0;
				while(j < this.transitionMatrix[list.get(i)][c].size()) {
					if(!statesSet.contains(this.transitionMatrix[list.get(i)][c].get(j))) {
						statesSet.add(this.transitionMatrix[list.get(i)][c].get(j));
						j ++;
					}
				}
			}
		}
		return statesSet;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<Integer>[][] convert() {
		LinkedList<Integer>[][] deltaMatrix = new LinkedList[this.transitionMatrix.length][254];
		for(int i = 0; i < this.transitionMatrix.length; i++) {
			for(int j = 0; j < 254; j ++) {
				LinkedList<Integer> currentState = new LinkedList<>();
				currentState.add(i);
				deltaMatrix[i][j] = eTransition(lowerDeltaFunc(eTransition(currentState), (char) j));
			}
		}
		return deltaMatrix;
	}
	
}
