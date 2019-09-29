import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    static Queue<Node> openList; //open nodes queue
    static ArrayList<Node> closedList;
    static Map<Long, Double> g; //cost of getting from the initial node to n
    static Map<Long, Double> h; //the estimate, eccording to the heuristic function, of the cost of getting n to the goal node
    static Map<Long, Double> f; // g(n) + h(n) estimate of the best solution that foes through n
    static long startingNode = 105050228; //change later
    static long goalNode = 106108549;
    //static ArrayList<Node> allNodes;
    static HashMap<Long, HashMap<Long, Double>> allNodes;

    public static void main(String[] args) throws IOException {
        allNodes = new HashMap<>();

        readInput();
        aStarSearch();
    }

    public static void readInput() throws FileNotFoundException, NumberFormatException {
        ArrayList<Long> tempSourceNodes = new ArrayList<>();
        ArrayList<Long> tempDestinationNodes = new ArrayList<>();
        ArrayList<Double> tempDistances = new ArrayList<>();

        Scanner s = new Scanner(new File("edges1000.txt"));
        while (s.hasNext()) {
            tempSourceNodes.add(Long.parseLong(s.next()));
            tempDestinationNodes.add(Long.parseLong(s.next()));
            tempDistances.add(Double.parseDouble(s.next()));
        }

        for (int i = 0; i < tempSourceNodes.size(); i++) {
            if (allNodes.containsKey(tempSourceNodes.get(i))) { //if the source node already exists in allNodes
                HashMap<Long, Double> childNodes = allNodes.get(tempSourceNodes.get(i)); //get the Hashmap of the existing sourceNode
                childNodes.put(tempDestinationNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the existing source node with destination node as key and distance as value
            } else {

                HashMap<Long, Double> childNodes = new HashMap<>();
                childNodes.put(tempDestinationNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the sourcenode
                allNodes.put(tempSourceNodes.get(i), childNodes); //
            }
        }

        System.out.println("s=");
        System.out.println("s=" + tempSourceNodes.size());
        System.out.println("d=" + tempDestinationNodes.size());
        System.out.println("D=" + tempDistances.size());
        //for(Map.Entry<Long, HashMap<Long, Double>> entry : allNodes.entrySet()){
        //    System.out.println(entry);
        //}
    }

    public static void aStarSearch() {
        //g = new HashMap<>();
        //f = new HashMap<>();
        // h = new HashMap<>();
        openList = new LinkedList<>();
        closedList = new ArrayList<>();

        openList.add(new Node(startingNode, 0, , allNodes.get(startingNode).keySet())); //insert starting node to queue. destination node is not relevant yet

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);
            if (currentNode.getNodeId() == goalNode) {
                System.out.println("we found it yay");
                return;
            }


            for (long childNodeId : currentNode.getChildNodesIds()) {

                double childTotalDistance = currentNode.getCurrentCost() + allNodes.get(currentNode.getNodeId()).get(childNodeId) + heuristic; //current distance + child distance + heuristic distance

                Node childNode = new Node(childNodeId, childTotalDistance);


                if (isInOpenList(childNode.getNodeId()) && isBetterInOpenList(childTotalDistance)){
                    break;
                }
                if (isInClosedList(childNode.getNodeId()) && isBetterInClosedList(childTotalDistance)){
                    break;
                }

                removeFromOpenList(childNode.getNodeId());
                removeFromClosedList(childNode.getNodeId());

                openList.add(childNode)

                //if n' is on the OPEN list and the existing one is as good or better
                // then discard n' and continue
                // if n' is on the CLOSED list and the existing one is as good or better
                // then discard n' and continue
                // Remove occurrences of n' from OPEN and CLOSED
                // Add n' to the OPEN list
            }
        }

    }

    public static boolean isInOpenList(long nodeId){
        for(Node nodeInList : openList){
            long idInList = nodeInList.getNodeId();
            if(nodeId == idInList) {
                return true;
            }
        }
        return false;
    }


    public static boolean isBetterInOpenList(double childTotalDistance){
        for(Node nodeInList : openList){
            if (nodeInList.getCurrentCost() <= childTotalDistance){
                return true;
            }
        }
        return false;
    }

    public static boolean isInClosedList(long nodeId){
        for(Node nodeInList : closedList){
            long idInList = nodeInList.getNodeId();
            if(nodeId == idInList) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBetterInClosedList(double childTotalDistance){
        for(Node nodeInList : closedList){
            if (nodeInList.getCurrentCost() <= childTotalDistance){
                return true;
            }
        }
        return false;
    }

    public static void  removeFromOpenList(long nodeId){
        for(Node nodeInList : openList){
            if (nodeInList.getNodeId() == nodeId ){
                openList.remove(nodeInList);
            }
        }
    }

    public static void  removeFromClosedList(long nodeId){
        for(Node nodeInList : closedList){
            if (nodeInList.getNodeId() == nodeId ){
                closedList.remove(nodeInList);
            }
        }
    }

}

class Node {
    private long nodeId;
    private double currentCost;
    private Set<Long> childNodesIds;

    public Node(long sourceNodeId, double currentCost) {
        this.nodeId = sourceNodeId;
        this.currentCost = currentCost;
    }

    public Node(long sourceNodeId, long currentCost,  Set<Long> childNodesIds) {
        this.nodeId = sourceNodeId;
        this.currentCost = currentCost;
        this.childNodesIds = childNodesIds;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long sourceNodeId) {
        this.nodeId = sourceNodeId;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public Set<Long> getChildNodesIds() {
        return childNodesIds;
    }

    public void setChildNodesIds(Set<Long> childNodesIds) {
        this.childNodesIds = childNodesIds;
    }

}
