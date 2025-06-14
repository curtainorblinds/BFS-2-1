import java.util.*;

/**
 * Leetcode 690. Employee Importance
 * Link: https://leetcode.com/problems/employee-importance/description/
 */
//------------------------------------ Solution 1 -----------------------------------
public class EmployeeImportance {
    /**
     * BFS solution: based on the input given it appears it is a graph problem without more than 1
     * incoming or outgoing edges, which makes it an n-ary tree problem. We will use adjacency list
     * for O(1) lookup to find importance and subordinates of an employee. And using queue we will visit all
     * subordinates first before visiting their subordinates. And during this process we will maintain a running
     * sum to calculate total importance
     *
     * TC: O(V + E) since here it is an n-any tree it will be O(V) where V -> total nodes n
     * SC: same as above logic O(V) v -> total nodes n
     */
    public int getImportance(List<Employee> employees, int id) {
        //adjacency List
        Map<Integer, Employee> lookup = new HashMap<>();

        for (Employee e: employees) {
            lookup.put(e.id, e);
        }

        Employee given = lookup.get(id);

        if (given == null) {
            return 0;
        }

        Queue<Integer> processQueue = new LinkedList<>();
        int imp = 0;
        processQueue.add(id);

        while(!processQueue.isEmpty()) {
            Employee emp = lookup.get(processQueue.poll());
            imp += emp.importance;

            for (int sub: emp.subordinates) {
                processQueue.add(sub);
            }
        }
        return imp;
    }
}

//------------------------------------ Solution 2 -----------------------------------
class EmployeeImportance2 {
    /**
     * void based DFS - instead of processing all subordinates of an employee we start with any and perform
     * dfs, along the way we maintain running sum of all employees encountered in the dfs
     *
     * TC: O(V) Auxiliary SC: O(V)
     * Recursive stack SC: O(V)
     *
     * Note: logic to maintain the running sum of importance can be performed at both pre and post level traversals
     */
    int imp;
    public int getImportance(List<Employee> employees, int id) {
        //adjacency List
        Map<Integer, Employee> lookup = new HashMap<>();

        for (Employee e: employees) {
            lookup.put(e.id, e);
        }

        dfs(id, lookup);

        return imp;
    }

    private void dfs(int id, Map<Integer, Employee> lookup) {
        //base
        if (lookup.get(id) == null) {
            return;
        }

        //logic
        Employee emp = lookup.get(id);
        imp += emp.importance;

        for (int sub: emp.subordinates) {
            dfs(sub, lookup);
        }
    }
}

//------------------------------------ Solution 3 -----------------------------------
class EmployeeImportance3 {
    /**
     * int based DFS recursion - same as above logic, made changes to turn void to int based recursion
     * by summing up importance inside recursion for self and all subordinates and returning that as answer
     *
     * TC: O(V) Auxiliary SC: O(V)
     * Recursive stack SC: O(V)
     *
     * Note: logic to maintain the running sum of importance can be performed at both pre and post level traversals
     */
    public int getImportance(List<Employee> employees, int id) {
        //adjacency List
        Map<Integer, Employee> lookup = new HashMap<>();

        for (Employee e: employees) {
            lookup.put(e.id, e);
        }

        return dfs(id, lookup);
    }

    private int dfs(int id, Map<Integer, Employee> lookup) {
        //base
        if (lookup.get(id) == null) {
            return 0;
        }

        //logic
        Employee emp = lookup.get(id);
        int imp = emp.importance;

        for (int sub: emp.subordinates) {
            imp += dfs(sub, lookup);
        }
        return imp;
    }
}