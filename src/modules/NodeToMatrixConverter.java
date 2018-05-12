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
        Queue<RPNToNFDE.State> toVisit = new PriorityQueue<>();

        LinkedList<Integer>[][] matrixBuilder = new LinkedList[RPNToNFDE.getCurrentNode()][];
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
        matrixBuilder[0] = column;


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
                out1 = currentNode.getOut1();
                buildRow(currentNode, out1, matrixBuilder, visited, toVisit, processed);
            }
        }

        finalMatrix = new LinkedList[matrixBuilder.length][];

        for(int i = 0; i < matrixBuilder.length; i++){
            finalMatrix[i] = matrixBuilder[i];
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
            LinkedList<Integer>[][] matrixBuilder,
            TreeSet<Integer> visited,
            Queue<RPNToNFDE.State> toVisit,
            TreeSet<Integer> processed){
	    if(out == null) return;

        toVisit.offer(out);
        System.out.println((out.getStateNumber() + ":") + currentState.getStateNumber() + " " + visited.size());
        if(!processed.contains(out.getStateNumber())) {
            processed.add(out.getStateNumber());
            LinkedList<Integer>[] column = new LinkedList[Character.MAX_VALUE];
            for (int k = 0; k < column.length; k++) {
                column[k] = new LinkedList<Integer>();
            }
            matrixBuilder[out.getStateNumber()] = column;
        }
        matrixBuilder[currentState.getStateNumber()][out.getC()].add(out.getStateNumber());

    }
}
