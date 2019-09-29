import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    static Queue<Node> openList; //open nodes queue
    static ArrayList<Node> closedList;
    static HashMap<Long, Double> allHeuristic;
    static long startingNode; // = 105050228; //change later
    static long goalNode; // = 1602034048;
    //static ArrayList<Node> allNodes;
    static HashMap<Long, HashMap<Long, Double>> allNodes;


    public static void main(String[] args) throws IOException {
        allNodes = new HashMap<>();
        allHeuristic = new HashMap<>();

        //read input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter starting node:");
        startingNode = scanner.nextLong();
        System.out.println("Enter goal node:");
        goalNode = scanner.nextLong();

        readInputFile();
        aStarSearch();
    }

    public static void readInputFile() throws FileNotFoundException, NumberFormatException {
        ArrayList<Long> tempSourceNodes = new ArrayList<>();
        ArrayList<Long> tempDestinationNodes = new ArrayList<>();
        ArrayList<Double> tempDistances = new ArrayList<>();

        Scanner s = new Scanner(new File("edges.txt"));
        while (s.hasNext()) {
            tempSourceNodes.add(Long.parseLong(s.next()));
            tempDestinationNodes.add(Long.parseLong(s.next()));
            tempDistances.add(Double.parseDouble(s.next()));
        }
        s.close();

        for (int i = 0; i < tempSourceNodes.size(); i++) {
            if (allNodes.containsKey(tempSourceNodes.get(i))) { //if the source node already exists in allNodes
                HashMap<Long, Double> childNodes = allNodes.get(tempSourceNodes.get(i)); //get the Hashmap of the existing sourceNode
                childNodes.put(tempDestinationNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the existing source node with destination node as key and distance as value
            } else {
                HashMap<Long, Double> childNodes = new HashMap<>();
                childNodes.put(tempDestinationNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the destination node
                allNodes.put(tempSourceNodes.get(i), childNodes); // put the source node as key and the hashmap of destination node as value
            }

            //do the same thing again except the edge will be from destination node to source node. because roads are two way
            if (allNodes.containsKey(tempDestinationNodes.get(i))) { //if the destination node already exists in allNodes
                HashMap<Long, Double> childNodes = allNodes.get(tempDestinationNodes.get(i)); //get the Hashmap of the existing destination node
                childNodes.put(tempSourceNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the existing destination node with source node as key and distance as value
            } else { // if the source node is not existing yet in the allNodes
                HashMap<Long, Double> childNodes = new HashMap<>();
                childNodes.put(tempSourceNodes.get(i), tempDistances.get(i)); //put an entry to the hashmap of the destination node
                allNodes.put(tempDestinationNodes.get(i), childNodes); //set the destination node as key in edges and the newly created source node  map as values
            }
        }

        //read heuristic input
        s = new Scanner(new File("heuristic.txt"));
        while (s.hasNext()) {
            long nodeId = Long.parseLong(s.next());
            double heuristicValue = Double.parseDouble(s.next());
            allHeuristic.put(nodeId, heuristicValue);

            //System.out.println(nodeId + "=" + heuristicValue);
        }
        s.close();
    }

    public static void aStarSearch() {
        //double distance = 0;
        openList = new LinkedList<>(); //queue
        closedList = new ArrayList<>(); //explored nodes

        openList.add(new Node(startingNode, allHeuristic.get(startingNode), allNodes.get(startingNode).keySet(), null)); //insert starting node to queue.
        //System.out.println(allNodes.get(startingNode).keySet());
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);
            if (currentNode.getNodeId() == goalNode) {
                Node currentNodeInPath = currentNode;
                int numNodesInPath = 0;
                double distance = 0;
                while (currentNodeInPath.getNodeParent() != null) {
                    numNodesInPath++;
                    distance += allNodes.get(currentNodeInPath.getNodeParent().getNodeId()).get(currentNodeInPath.getNodeId()); // todo: fix this temporary inefficient calculation of distance.
                    currentNodeInPath = currentNodeInPath.getNodeParent();
                }
                System.out.println("Nodes visited=" + closedList.size());
                System.out.println("Nodes on Path=" + numNodesInPath);
                System.out.println("Distance=" + distance);
                return;
            }


            for (long childNodeId : currentNode.getChildNodesIds()) {
                double childTotalDistance = currentNode.getCurrentCost() + allNodes.get(currentNode.getNodeId()).get(childNodeId) + allHeuristic.get(childNodeId); //current distance + child distance + child heuristic distance
                //distance = childTotalDistance;
                Node childNode = new Node(childNodeId, childTotalDistance, allNodes.get(childNodeId).keySet(), currentNode);
                /*
                if (isInOpenList(childNode.getNodeId()) && isBetterInOpenList(childTotalDistance)) {
                    break;
                }
                if (isInClosedList(childNode.getNodeId()) && isBetterInClosedList(childTotalDistance)) {
                    break;
                }
                removeFromOpenList(childNode.getNodeId());
                removeFromClosedList(childNode.getNodeId());
                openList.add(childNode);
*/
                if (!isInOpenList(childNode.getNodeId()) && !isInClosedList(childNodeId)) {
                    openList.add(childNode);
                } else if (!isBetterInOpenList(childNode.getNodeId())) {
                    removeFromOpenList(childNode.getNodeId());
                    openList.add(childNode);
                }
            }
        }

        System.out.println("Did not find");
    }

    public static boolean switchNode(long nodeId) {
        for (Node nodeInList : openList) {
            long idInList = nodeInList.getNodeId();
            if (nodeId == idInList) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInOpenList(long nodeId) {
        for (Node nodeInList : openList) {
            long idInList = nodeInList.getNodeId();
            if (nodeId == idInList) {
                return true;
            }
        }
        return false;
    }


    public static boolean isBetterInOpenList(double childTotalDistance) {
        for (Node nodeInList : openList) {
            if (nodeInList.getCurrentCost() <= childTotalDistance) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInClosedList(long nodeId) {
        for (Node nodeInList : closedList) {
            long idInList = nodeInList.getNodeId();
            if (nodeId == idInList) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBetterInClosedList(double childTotalDistance) {
        for (Node nodeInList : closedList) {
            if (nodeInList.getCurrentCost() <= childTotalDistance) {
                return true;
            }
        }
        return false;
    }

    public static void removeFromOpenList(long nodeId) {
        for (Node nodeInList : openList) {
            if (nodeInList.getNodeId() == nodeId) {
                openList.remove(nodeInList);
            }
        }
    }

    public static void removeFromClosedList(long nodeId) {
        for (Node nodeInList : closedList) {
            if (nodeInList.getNodeId() == nodeId) {
                closedList.remove(nodeInList);
            }
        }
    }
}

class Node {
    private long nodeId;
    private double currentCost;
    private Set<Long> childNodesIds;
    private Node nodeParent;


    public Node(long sourceNodeId, double currentCost) {
        this.nodeId = sourceNodeId;
        this.currentCost = currentCost;
    }

    public Node(long sourceNodeId, double currentCost, Set<Long> childNodesIds, Node nodeParent) {
        this.nodeId = sourceNodeId;
        this.currentCost = currentCost;
        this.childNodesIds = childNodesIds;
        this.nodeParent = nodeParent;
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

    public Node getNodeParent() {
        return nodeParent;
    }

}
