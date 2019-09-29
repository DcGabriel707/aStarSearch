import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    static Queue<Long> openList;
    static ArrayList<Long> closedList;
    static Map<Long, Double> g; //cost of getting from the initial node to n
    static Map<Long, Double> h; //the estimate, eccording to the heuristic function, of the cost of getting n to the goal node
    static Map<Long, Double> f; // g(n) + h(n) estimate of the best solution that foes through n
    static long startingNode = 105050228; //change later
    static long goalNode = 106108549;
    static ArrayList<Node> allNodes;

    public static void main(String[] args) throws IOException {
        g = new HashMap<>();
        f = new HashMap<>();
        h = new HashMap<>();
        openList = new LinkedList<>();
        closedList = new ArrayList<>();
        allNodes = new ArrayList<>();

        readInput();
        aStarSearch();
    }

    public static void readInput() throws FileNotFoundException, NumberFormatException {

        Scanner s = new Scanner(new File("edges.txt"));


        ArrayList<Long> tempSourceNodes = new ArrayList<>();
        ArrayList<Long> tempDestinationNodes = new ArrayList<>();
        ArrayList<Double> tempDistances = new ArrayList<>();
        //todo find better way to check if node source node already exists
        ArrayList<Long> anotherSourceNodeList = new ArrayList<>();

        while (s.hasNext()) {
            tempSourceNodes.add(Long.parseLong(s.next()));
            tempDestinationNodes.add(Long.parseLong(s.next()));
            tempDistances.add(Double.parseDouble(s.next()));
        }

        //todo: find more convenient way to check if node source node already exists
        for (int i = 0; i < tempSourceNodes.size(); i ++){
            if (tempSourceNodes.contains(tempSourceNodes)) {
                tempSourceNodes.add(Long.parseLong(sourceNode));
                System.out.println("yay");
            }
            allNodes.add(new Node(sourceNode, destNode, distance));
        }

        System.out.println(allNodes);
    }

    public static void aStarSearch() {
        openList.add(startingNode);
        g.put(startingNode, 0.0);
        while (!openList.isEmpty()) {

        }
    }

    public static double findLowest(ArrayList<Double> arrayList) {
        double lowestCost = 999999;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) < lowestCost) {
                lowestCost = arrayList.get(i);
            }
        }
        return lowestCost;
    }
}

class Node {
    private long nodeId;
    private ArrayList<Long> nodeChildIds;
    private ArrayList<Double> nodeChildDistances;

    Node(long nodeId, long nodeChildId, double nodeChildDistance) {
        this.nodeChildIds = new ArrayList<Long>();
        this.nodeChildDistances = new ArrayList<Double>();
        this.nodeId = nodeId;
        this.nodeChildIds.add(nodeChildId);
        this.nodeChildDistances.add(nodeChildDistance);
    }

    public void addChildNode(long childNodeID, double distance) {
        nodeChildIds.add(childNodeID);
        nodeChildDistances.add(distance);
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public ArrayList<Long> getNodeChildIds() {
        return nodeChildIds;
    }

    public void setNodeChildIds(ArrayList<Long> nodeChildIds) {
        this.nodeChildIds = nodeChildIds;
    }

    public ArrayList<Double> getNodeChildDistances() {
        return nodeChildDistances;
    }

    public void setNodeChildDistances(ArrayList<Double> nodeChildDistances) {
        this.nodeChildDistances = nodeChildDistances;
    }
}
