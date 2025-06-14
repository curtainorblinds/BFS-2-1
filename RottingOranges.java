import java.util.LinkedList;
import java.util.Queue;

/**
 * Leetcode 994. Rotting Oranges
 * Link: https://leetcode.com/problems/rotting-oranges/description/
 */
//------------------------------------ Solution 1 -----------------------------------
public class RottingOranges {
    /**
     * BFS Solution - Initially start with existing rotten ones and put them in processing queue.
     * At the same time also count total fresh. Start processing from the queue level by level and find
     * all fresh ones next the rotten one mark them immediately rotten and add to processing queue.
     * Doing this avoids duplicate in case more than 1 rotten at current level share the same fresh one
     * as their neighbor. At the end if fresh are left return -1 otherwise return total levels/time - 1
     * bcz last level processed did not add anything to the processing queue.
     *
     * TC: O(m*n) SC: O(m*n)
     */
    public int orangesRotting(int[][] grid) {
        int fresh = 0;
        Queue<Integer> processRotten = new LinkedList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    processRotten.add(i);
                    processRotten.add(j);
                }
                if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        if (fresh == 0) {
            return 0;
        }

        int time = 0;
        int[][] dirs = {{0, 1},{1, 0},{0, -1},{-1, 0}};

        while(!processRotten.isEmpty()) {
            int size = processRotten.size() / 2;
            time++;
            for (int i = 0; i < size; i++) {
                int row = processRotten.poll();
                int col = processRotten.poll();

                for (int[] dir: dirs) {
                    int nr = row + dir[0];
                    int nc = col + dir[1];

                    //bound check and logic
                    if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[nr].length && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2; //mark rotten, this reduces duplicacy in case multiple rotten at current bfs level have 1 same fresh as neighbor
                        fresh--;

                        processRotten.add(nr);
                        processRotten.add(nc);
                    }
                }
            }
        }

        if (fresh == 0) {
            return time - 1;
        }

        return -1;
    }
}

//------------------------------------ Solution 2 -----------------------------------
class RottingOranges2 {
    /**
     * DFS solution - for each original rotten orange perform dfs in all 4 directions and along the way
     * only visit the same cell if current time is less than previous time. Already visited does not need
     * to be stored separately as with above logic we are updating the original 1 in the matrix with offset of
     * 2 starting with time=2 and we increment time by 1 for the next set of neighbors which are 1
     *
     * TC: O(mn)^2 in worst case as each cell is exploring all other cells in the matrix in theory, but since
     * we are only visiting the paths where time is improving we will have less cells visited. Plus, as more
     * cell=2 original rotten ones are processed less and less cells will be processed
     * Hence amortized TC will be O(mn)
     * Auxiliary SC: O(1)
     * Recursive stack SC: same as time complexity logic O(mn)
     */
    public int orangesRotting(int[][] grid) {
        int[][] dirs = {{0, 1},{1, 0},{0, -1},{-1, 0}};

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    dfs(grid, dirs, i, j, 2);
                }
            }
        }

        int time = 2;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    return -1;
                }
                time = Math.max(time, grid[i][j]);
            }
        }
        return time - 2;
    }

    private void dfs(int[][] grid, int[][] dirs, int r, int c, int time) {
        //base
        if (r < 0 || c < 0 || r == grid.length || c == grid[r].length || grid[r][c] == 0) {
            return;
        }

        //logic
        if (grid[r][c] > 1 && grid[r][c] < time) {
            return;
        }
        grid[r][c] = time;

        for (int[] dir: dirs) {
            int nr = r + dir[0];
            int nc = c + dir[1];
            dfs(grid, dirs, nr, nc, time + 1);
        }
    }
}