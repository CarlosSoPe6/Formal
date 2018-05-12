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

        TreeSet<Integer>[][] matrixBuilder = new TreeSet[RPNToNFDE.getCurrentNode()][];
        TreeSet<Integer> visited = new TreeSet<>();
        TreeSet<Integer> processed = new TreeSet<>();
        LinkedList<Integer>[] column;
        TreeSet<Integer>[] buildCol;

        LinkedList<Integer>[][] finalMatrix;

        int realStateNumber = 0;

        // Real state number base case (root node)
        buildCol = new TreeSet[256];
        for(int k = 0; k < buildCol.length; k++){
            buildCol[k] = new TreeSet<Integer>();
        }
        matrixBuilder[0] = buildCol;


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
            column = new LinkedList[matrixBuilder[i].length];
            for(int k = 0; k < matrixBuilder[i].length; k++){
                column[k] = new LinkedList<>();
                for (Integer integer : matrixBuilder[i][k]) {
                    column[k].add(integer);
                }
            }
            finalMatrix[i] = column;
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
            TreeSet<Integer>[][] matrixBuilder,
            TreeSet<Integer> visited,
            Queue<RPNToNFDE.State> toVisit,
            TreeSet<Integer> processed){
	    if(out == null) return;

        toVisit.offer(out);
        System.out.println((out.getStateNumber() + ":") + currentState.getStateNumber() + " " + visited.size());
        if(!processed.contains(out.getStateNumber())) {
            processed.add(out.getStateNumber());
            TreeSet<Integer>[] column = new TreeSet[256];
            for (int k = 0; k < column.length; k++) {
                column[k] = new TreeSet<Integer>();
            }
            matrixBuilder[out.getStateNumber()] = column;
        }
        matrixBuilder[currentState.getStateNumber()][out.getC()].add(out.getStateNumber());
    }
}
