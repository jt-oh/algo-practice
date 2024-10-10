import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.lang.Math;
import java.util.Queue;
import java.util.LinkedList;

public class Main {
    private class Node {
        private int idx;
        private int population;
        private int ctdNodeCount;
        private int[] ctdNodeIdxs;

        public Node(int idx, int population) {
            this.idx = idx;
            this.population = population;
        }

        public void setCtdNodes(int count, int[] nodeIdxs) {
            ctdNodeCount = count;
            ctdNodeIdxs = nodeIdxs;
        }

        public int[] getCtdNodeIdxs() {
            return ctdNodeIdxs;
        }

        public int getPopulation() {
            return population;
        }
    }

    private int n;
    private Node[] nodes;

    public static void main(String... args) throws IOException {
        Main main = new Main();

        main.getInputs();

        System.out.println(main.getMinDiffBetweenTwoSets());
    }

    private void getInputs() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line;
        String[] tokens;

        line = br.readLine();
        n = Integer.parseInt(line);
        nodes = new Node[n];

        line = br.readLine();
        tokens = line.split(" ");
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, Integer.parseInt(tokens[i]));
        }

        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            int edgeCount = Integer.parseInt(tokens[0]);
            int[] ctdNodeIdxs = new int[edgeCount];
            for (int j = 0; j < edgeCount; j++) {
                ctdNodeIdxs[j] = Integer.parseInt(tokens[j + 1]) - 1;
            }

            nodes[i].setCtdNodes(edgeCount, ctdNodeIdxs);
        }

        br.close();

        return;
    }

    private int getMinDiffBetweenTwoSets() {
        int minDiff = -1;
        Set<Set<Integer>> checkedSets = new HashSet<> ();
        Set<Integer> theOtherNodes = new HashSet<> ();
        Queue<Integer> queue = new LinkedList<> ();


        Set<Set<Integer>> subsets = getSubsetsWithoutEmptyOrFullSizeOfNodes();
        for (Set<Integer> theNodes: subsets) {
            // System.out.println(Arrays.toString(theNodes.toArray()));


            if (checkedSets.contains(theNodes)) {
                continue;
            }
            
            theOtherNodes.clear();
            for (int i = 0; i < n; i++) {
                if (! theNodes.contains(i)) {
                    theOtherNodes.add(i);
                }
            }
            

            boolean[] visited = new boolean[n];
            int theNodesCheckedCount = 0;
            int theNodesPopulation = 0;
            int theOtherNodesCheckedCount = 0;
            int theOtherNodesPopulation = 0;

            queue.clear();
            queue.add(theNodes.iterator().next());
            while (!queue.isEmpty()) {
                int idx = queue.poll();
                if (visited[idx]) {
                    continue;
                }


                Node node = nodes[idx];
                visited[idx] = true;
                theNodesCheckedCount++;
                theNodesPopulation += node.getPopulation();

                
                for (int adjacentIdx: node.getCtdNodeIdxs()) {
                    if (theNodes.contains(adjacentIdx)) {
                        queue.add(adjacentIdx);
                    }
                }
            }

            queue.clear();
            queue.add(theOtherNodes.iterator().next());
            while (!queue.isEmpty()) {
                int idx = queue.poll();
                if (visited[idx]) {
                    continue;
                }


                Node node = nodes[idx];
                visited[idx] = true;
                theOtherNodesCheckedCount++;
                theOtherNodesPopulation += node.getPopulation();


                for (int adjacentIdx: node.getCtdNodeIdxs()) {
                    if (theOtherNodes.contains(adjacentIdx)) {
                        queue.add(adjacentIdx);
                    }
                }
            }


            if (theNodesCheckedCount == theNodes.size() && theOtherNodesCheckedCount == theOtherNodes.size()) {
                int diff = Math.abs(theNodesPopulation - theOtherNodesPopulation);

                if (minDiff == -1 || diff < minDiff) {
                    minDiff = diff;
                }
            }

            checkedSets.add(theNodes);
            checkedSets.add(theOtherNodes);
        }


        return minDiff;
    }

    private Set<Set<Integer>> getSubsetsWithoutEmptyOrFullSizeOfNodes() {
        Set<Integer> original = new HashSet<> ();
        for (int i = 0; i < n; i++) {
            original.add(i);
        }


        Set<Set<Integer>> subsets = new HashSet<> ();
        recursivelyGetSubsets(original, new HashSet<Integer> (), subsets);

        return subsets;
    }

    private void recursivelyGetSubsets(Set<Integer> carry, Set<Integer> current, Set<Set<Integer>> subsets) {
        if (carry.isEmpty()) {
            int currentSize = current.size();
            if (currentSize == 0 || currentSize == n) {
                // System.out.println("filtered subset!");
                // System.out.println(Arrays.toString(current.toArray()));

                return;
            }

            // System.out.println("add subset!");
            // System.out.println(Arrays.toString(current.toArray()));

            subsets.add(new HashSet<> (current));
            return;
        }


        Iterator<Integer> it = carry.iterator();
        Integer e = it.next();
        it.remove();


        Set<Integer> copiedCurrent = new HashSet<> (current);

        recursivelyGetSubsets(new HashSet<> (carry), copiedCurrent, subsets);

        copiedCurrent.add(e);
        recursivelyGetSubsets(new HashSet<> (carry), copiedCurrent, subsets);
    }
}