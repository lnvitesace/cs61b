package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solver {
    private final int moves;
    private final MinPQ<SearchNode> pq;
    private final Deque<WorldState> solvingSequence;

    private static class SearchNode implements Comparable<SearchNode> {
        private final WorldState worldState;
        private final int moves;
        private final SearchNode previous;
        private final int estimatedDistance;

        public SearchNode(WorldState w, int m, SearchNode p, int ed) {
            worldState = w;
            moves = m;
            previous = p;
            estimatedDistance = ed;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(
                    this.moves + this.estimatedDistance,
                    that.moves + that.estimatedDistance
            );
        }
    }

    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        solvingSequence = new ArrayDeque<>();
        SearchNode initialNode = new SearchNode(
                initial, 0, null, initial.estimatedDistanceToGoal()
        );
        SearchNode goalNode = solve(initialNode);
        moves = goalNode.moves;
        getSolvingSequence(goalNode);
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solvingSequence;
    }

    private SearchNode solve(SearchNode initialNode) {
        pq.insert(initialNode);

        while (true) {
            SearchNode currentNode = pq.delMin();
            if (currentNode.worldState.isGoal()) {
                return currentNode;
            }

            for (WorldState neighborState : currentNode.worldState.neighbors()) {
                if (currentNode.previous == null
                        || !neighborState.equals(currentNode.previous.worldState)) {
                    SearchNode neighborNode = new SearchNode(
                            neighborState,
                            currentNode.moves + 1,
                            currentNode,
                            neighborState.estimatedDistanceToGoal()
                    );
                    pq.insert(neighborNode);
                }
            }
        }
    }

    private void getSolvingSequence(SearchNode node) {
        while (node != null) {
            solvingSequence.addFirst(node.worldState);
            node = node.previous;
        }
    }

}
