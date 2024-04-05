package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private final int s;
    private final int t;
    private final Maze maze;
    private final PriorityQueue<SearchNode> pq;
    private static final class SearchNode implements Comparable<SearchNode>{
        private final int v;
        private final int estimatedDistance;

        public int compareTo(SearchNode that) {
            return Integer.compare(this.estimatedDistance, that.estimatedDistance);
        }
        public SearchNode(int ve, int ed) {
            v = ve;
            estimatedDistance = ed;
        }
    }


    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        pq = new PriorityQueue<>();
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        SearchNode initialNode = new SearchNode(s, h(s));
        pq.add(initialNode);

        while (true) {
            int v = pq.remove().v;
            marked[v] = true;
            announce();

            if (v == t) {
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    pq.add(new SearchNode(w, distTo[w] + h(w)));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

