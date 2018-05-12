package modules;

import java.util.*;

public class NodeToMatrixConverter {
    /**
     * Cambia de represencación un grafo
     * @param root La raiz del grafo
     * @return una matriz de listas enlazadas que contiene al autómata
     */
    @SuppressWarnings("unchecked")
	public static LinkedList<Integer>[][] convert(RPNToNFDE.State root){
        Queue<RPNToNFDE.State> toVisit = new LinkedList<>();

        ArrayList<LinkedList<Integer>[]> matrixBuilder = new ArrayList<>();
        TreeSet<Integer> visited = new TreeSet<>();
        TreeSet<Integer> processed = new TreeSet<>();
        LinkedList<Integer>[] column;

        LinkedList<Integer>[][] finalMatrix;

        int realStateNumber = 0;

        // Real state number base case (root node)
        column = new LinkedList[Character.MAX_VALUE];
        for(int k = 0; k < column.length; k++){
            column[k] = new LinkedList<Integer>();
        }
        matrixBuilder.add(column);


        toVisit.offer(root);

        // BFS
        RPNToNFDE.State out0;
        RPNToNFDE.State out1;
        RPNToNFDE.State currentNode;
        while (!toVisit.isEmpty()) {
            currentNode = toVisit.poll();
            if (!visited.contains(currentNode.getStateNumber())){
                visited.add(currentNode.getStateNumber());
                out0 = currentNode.getOut0();
                buildRow(currentNode, out0, matrixBuilder, visited, toVisit, processed);
                out1 = currentNode.getOut0();
                buildRow(currentNode, out1, matrixBuilder, visited, toVisit, processed);
            }
        }

        finalMatrix = new LinkedList[matrixBuilder.size()][];

        for(int i = 0; i < matrixBuilder.size(); i++){
            finalMatrix[i] = matrixBuilder.get(i);
        }

        return finalMatrix;
    }

    /**
     * 
     * @param currentState
     * @param out
     * @param matrixBuilder
     * @param visited
     * @param toVisit
     * @param processed
     */
    private static void buildRow(
            RPNToNFDE.State currentState,
            RPNToNFDE.State out,
            ArrayList<LinkedList<Integer>[]> matrixBuilder,
            TreeSet<Integer> visited,
            Queue<RPNToNFDE.State> toVisit,
            TreeSet<Integer> processed){
	    if(out == null) return;

        toVisit.add(out);
        System.out.println((out.getStateNumber() + ":") + currentState.getStateNumber() + " " + visited.size());
        if(!visited.contains(out.getStateNumber()) && !processed.contains(out.getStateNumber())) {
            processed.add(out.getStateNumber());
            LinkedList<Integer>[] column = new LinkedList[Character.MAX_VALUE];
            for (int k = 0; k < column.length; k++) {
                column[k] = new LinkedList<Integer>();
            }
            matrixBuilder.add(column);
            matrixBuilder.get(currentState.getStateNumber())[out.getC()].add(out.getStateNumber());
        }

    }
}
