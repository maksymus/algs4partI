import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private Map<String, Integer> teams = new HashMap<String, Integer>();
    private int[] wins;
    private int[] loss;
    private int[] left;
    private int[][] matrix;

    private int[] teamToVertexIndex;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        try {
            int size = in.readInt();
            wins = new int[size];
            loss = new int[size];
            left = new int[size];
            matrix = new int[size][size];

            for (int i = 0; i < size; i++) {
                String team = in.readString();
                teams.put(team, i);
                wins[i] = in.readInt();
                loss[i] = in.readInt();
                left[i] = in.readInt();

                for (int j = 0; j < size; j++) {
                    matrix[i][j] = in.readInt();
                }
            }
        } finally {
            in.close();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return Collections.unmodifiableSet(teams.keySet());
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();

        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();

        return loss[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();

        return left[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException();

        return matrix[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();

        if (certificateOfElimination(team) != null)
            return true;

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();

        List<String> trivial = new ArrayList<String>();
        for (String other : teams.keySet()) {
            if (isTriviallyEliminated(team, other)) {
                trivial.add(other);
            }
        }

        if (!trivial.isEmpty())
            return trivial;

        FlowNetwork flowNetwork = buildFlowNetwork(team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);

        if (!isEliminated(flowNetwork))
            return null;

        List<String> cert = new ArrayList<String>();
        for (String other : teams.keySet()) {
            int i = teams.get(other);
            if (i != teams.get(team) && fordFulkerson.inCut(teamToVertexIndex[i])) {
                cert.add(other);
            }
        }

        return cert;
    }

    private boolean isTriviallyEliminated(String team1, String team2) {
        int idx1 = teams.get(team1);
        int idx2 = teams.get(team2);

        if (idx1 == idx2)
            return false;

        return wins[idx1] + left[idx1] < wins[idx2];
    }

    private FlowNetwork buildFlowNetwork(String team) {
        int size = teams.size();
        int index = teams.get(team);
        int numVertex = 2 + (size - 1)+ ((size * size - size) / 2 - (size - 1));

        FlowNetwork flowNetwork = new FlowNetwork(numVertex);

        int teamVertexStart = numVertex - size;
        teamToVertexIndex = new int[size];
        for (int i = 0; i < size; i++) {
            if (i == index)
                continue;

            teamToVertexIndex[i] = teamVertexStart;
            flowNetwork.addEdge(new FlowEdge(teamVertexStart, numVertex - 1, wins[index] + left[index] - wins[i]));
            teamVertexStart++;
        }

        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i != j && i != index && j != index) {
                    flowNetwork.addEdge(new FlowEdge(0, count, matrix[i][j]));

                    flowNetwork.addEdge(new FlowEdge(count, teamToVertexIndex[i], Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(count, teamToVertexIndex[j], Double.POSITIVE_INFINITY));

                    count++;
                }
            }
        }

        return flowNetwork;
    }

    // If some edges pointing from s (0 vertex) are not full,
    // then there is no scenario in which team x can win the division.
    private boolean isEliminated(FlowNetwork flowNetwork) {
        Iterable<FlowEdge> adj = flowNetwork.adj(0);
        for (FlowEdge flowEdge : adj) {
            if (flowEdge.residualCapacityTo(flowEdge.other(0)) > 0) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        FlowNetwork flowNetwork = division.buildFlowNetwork("Detroit");
//        System.out.println(flowNetwork.toString());


        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }


    }
}
