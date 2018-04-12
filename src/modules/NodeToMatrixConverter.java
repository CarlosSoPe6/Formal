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

        ArrayList<LinkedList<Integer>[]> matrixBuilder = new ArrayList<>();
        ArrayList<Integer> map = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        LinkedList<Integer>[] column;

        LinkedList<Integer>[][] finalMatrix;

        int realStateNumber = 0;

        // Real state number base case (root node)
        map.add(root.getStateNumber());
        column = new LinkedList[255];
        for(int k = 0; k < column.length; k++){
            column[k] = new LinkedList<Integer>();
        }
        matrixBuilder.add(column);


        toVisit.offer(root);

        // BFS
        RPNToNFDE.NFDENode currentNode;
        while (!toVisit.isEmpty()) {
            currentNode = toVisit.poll();
            if (!visited.contains(currentNode.getStateNumber())){
                visited.add(currentNode.getStateNumber());
                // Mapeamos los estados a filas de la matriz
                realStateNumber = map.indexOf(currentNode.getStateNumber());

                // Iteramos por cada posible salida
                for (int i = 0; i < currentNode.getAdjacentNodes().length; i++) {
                    // Iteramos por cada nodo de cada salida

                    for (RPNToNFDE.NFDENode n : currentNode.getAdjacentNodes()[i]) {
                        toVisit.offer(n);
                        System.out.println((n + ":") + realStateNumber + " " + visited.size());
                        if(!visited.contains(n.getStateNumber())) {
                            map.add(n.getStateNumber());
                            column = new LinkedList[255];
                            for(int k = 0; k < column.length; k++){
                                column[k] = new LinkedList<Integer>();
                            }
                            matrixBuilder.add(column);

                        }
                        matrixBuilder.get(realStateNumber)[i].add(map.indexOf(n.getStateNumber()));
                    }
                }
            }
        }

        finalMatrix = new LinkedList[matrixBuilder.size()][];

        for(int i = 0; i < matrixBuilder.size(); i++){
            finalMatrix[i] = matrixBuilder.get(i);
        }

        return finalMatrix;
    }
}
