package modules;

import java.util.*;

public class NodeToMatrixConverter {
    /**
     * Cambia de represencación un grafo
     * @param root La raiz del grafo
     * @return una matriz de listas enlazadas que contiene al autómata
     */
    public static LinkedList<Integer>[][] convert(RPNToNFDE.NFDENode root){
        Queue<RPNToNFDE.NFDENode> toVisit = new LinkedList<>();

        HashSet<Integer> visited = new HashSet<>();

        // BFS

        int nodeCount = 0;
        toVisit.offer(root);
        RPNToNFDE.NFDENode currentNode;
        while (!toVisit.isEmpty()) {
            currentNode = toVisit.poll();
            if (!visited.contains(currentNode.getStateNumber())){
                for (int i = 0; i < currentNode.getAdjacentNodes().length; i++) {
                    for (RPNToNFDE.NFDENode n : currentNode.getAdjacentNodes()[i]) {
                        toVisit.offer(n);
                        // TODO: add to matrix
                        System.out.println((n + ":") + i);
                    }
                }
                visited.add(currentNode.getStateNumber());
            }
        }

        return null;
    }

    /**
     * Crea el algorimo iterativo
     */
    private class NodeNavigator {
        private RPNToNFDE.NFDENode node;
        private char currentEvalation;
    }
}
