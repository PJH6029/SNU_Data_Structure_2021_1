package HW6;

import java.io.*;
import java.util.*;

public class SubwayGraph {
    private Map<StationVertex, List<ListItem>> vertices; // Key: 고유한 역 id. 모든 역은 유일
    private Map<String, List<StationVertex>> sameNamesMap; // 같은 이름의 역들의 id들을 관리
    private Map<String, List<StationVertex>> sameLinesMap; // 같은 라인인 역들의 id들을 관리

    private Map<String, String> id2Name;

    private int numStations;
    private static final int INF = Integer.MAX_VALUE;
    private static final int INTERSECTION_TIME = 5;

    public SubwayGraph() {
        this.vertices = new HashMap<>();
        this.sameNamesMap = new HashMap<>();
        this.sameLinesMap = new HashMap<>();
        this.id2Name = new HashMap<>();
        numStations = 0;
    }

    private void insertVertex(String id, String name, String line) {
        StationVertex stationVertex = new StationVertex(id);
        vertices.putIfAbsent(stationVertex, new LinkedList<>());
        id2Name.putIfAbsent(id, name);
        numStations++;
        List<StationVertex> intersectionVertexList = sameNamesMap.get(name);
        List<StationVertex> sameLineVertexList = sameLinesMap.get(line);

        if (intersectionVertexList != null) {
            // 환승역 생김

            // connect intersection
            for (StationVertex intersectionVertex : intersectionVertexList) {
                insertEdge(stationVertex, intersectionVertex, INTERSECTION_TIME);
                insertEdge(intersectionVertex, stationVertex, INTERSECTION_TIME);
            }

            // insert될 역 ID 추가
            intersectionVertexList.add(stationVertex);
        } else {
            // 새로운 이름의 역
            List<StationVertex> vertexList = new LinkedList<>();
            vertexList.add(stationVertex);
            sameNamesMap.put(name, vertexList);
        }

        // line 관리
        if (sameLineVertexList != null) {
            sameLineVertexList.add(stationVertex);
        } else {
            List<StationVertex> newSameLineVertexList = new LinkedList<>();
            newSameLineVertexList.add(stationVertex);
            sameLinesMap.put(line, newSameLineVertexList);
        }
    }

    private void insertEdge(StationVertex departure, StationVertex destination, int time) {
        List<ListItem> vertexList = vertices.get(departure);
        if (vertexList != null) {
            vertexList.add(new ListItem(destination, time));
        } else {
            System.out.println(departure + " " + destination);
            System.out.println("Invalid Edge Information: station that does not exist");
        }
    }

    private void insertEdge(String departureID, String destinationID, int time) {
        StationVertex departureVertex = new StationVertex(departureID);
        StationVertex destinationVertex = new StationVertex(destinationID);
        insertEdge(departureVertex, destinationVertex, time);
    }

    private String getNameFromID(String id) {
        return id2Name.getOrDefault(id, "no such ID");
    }

    public String printShortestRoute(String departureName, String destinationName) {
        List<StationVertex> departureList = sameNamesMap.get(departureName);
        List<StationVertex> destinationList = sameNamesMap.get(destinationName);

        if (departureList != null && destinationList != null) {
            StationVertex departure = sameNamesMap.get(departureName).get(0);

            Pair<HashMap<StationVertex, Integer>, HashMap<StationVertex, StationVertex>> resultDijkstra = doDijkstra(departure); // distance, prevVertex

            // build route
            StringBuilder routesBuilder = new StringBuilder();
            int minDistance = Integer.MAX_VALUE;
            StationVertex minDestination = destinationList.get(0);
            for (StationVertex destination : destinationList) {
                int innerDistance = resultDijkstra.first.get(destination);
                if (innerDistance < minDistance) {
                    minDistance = innerDistance;
                    minDestination = destination;
                }
            }
            String prevVertexName = " ";

            Stack<StationVertex> routeStack = new Stack<>();
            StationVertex vertex = minDestination;
            StationVertex vtx;
            while ((vtx = resultDijkstra.second.get(vertex)) != null && !vertex.equals(vtx)) {
                routeStack.push(vertex);
                vertex = resultDijkstra.second.get(vertex);
            }
            routeStack.push(departure);

            boolean intersectionTwice = false;
            while(!routeStack.isEmpty()) {
                StationVertex poppedVertex = routeStack.pop();
                String poppedVertexName = getNameFromID(poppedVertex.id);
                if (poppedVertexName.equals(prevVertexName)) {
                    // 환승
                    if (!intersectionTwice) {
                        routesBuilder.insert(routesBuilder.length() - poppedVertexName.length() - 1, "[");
                        routesBuilder.insert(routesBuilder.length() - 1, "]");
                    }
                    intersectionTwice = true;
                } else {
                    routesBuilder.append(poppedVertexName).append(" ");
                    intersectionTwice = false;
                }
                prevVertexName = poppedVertexName;
            }

            if (routesBuilder.length() != 0) {
                // 처음 끝 환승 mark 제거
                if (routesBuilder.charAt(0) == '[') {
                    routesBuilder.deleteCharAt(0);
                    routesBuilder.deleteCharAt(routesBuilder.indexOf("]"));
                }
                if (routesBuilder.charAt(routesBuilder.length() - 2) == ']') {
                    routesBuilder.deleteCharAt(routesBuilder.length() - 2);
                    routesBuilder.deleteCharAt(routesBuilder.lastIndexOf("["));
                }

                return routesBuilder.toString().trim() + '\n' + minDistance;
            } else {
                return ("there is no route from " + departureName + " to " + destinationName);
            }
        } else {
            return ("Invalid Station: station that does not exist");
        }
    }



    private Pair<HashMap<StationVertex, Integer>, HashMap<StationVertex, StationVertex>> doDijkstra(StationVertex departure) {
        PriorityQueue<ListItem> priorityQueue = new PriorityQueue<>();
        HashMap<StationVertex, Integer> distance = new HashMap<>();
        HashMap<StationVertex, StationVertex> prevVertexToDeparture = new HashMap<>();
        Set<StationVertex> visited = new HashSet<>();

        String departureName = getNameFromID(departure.id);

        // initialize distance to INF
        for (StationVertex stationVertex : vertices.keySet()) {
            distance.put(stationVertex, INF);
        }

        // departure와 이름이 같은 역들은 거리를 0으로 초기화
        for (StationVertex adjVertex : sameNamesMap.get(departureName)) {
            distance.put(adjVertex, 0);
            priorityQueue.add(new ListItem(adjVertex, 0));
            prevVertexToDeparture.put(adjVertex, departure);
        }

        while(!priorityQueue.isEmpty()) {
            ListItem poll = priorityQueue.poll();
            StationVertex smallestDistanceVertex = poll.vertex;;
            visited.add(smallestDistanceVertex);

            for (ListItem adjVertexWithTime : vertices.get(smallestDistanceVertex)) {
                StationVertex adjVertex = adjVertexWithTime.vertex;
                int time = adjVertexWithTime.time;

                if(!visited.contains(adjVertex)) {
                    if (distance.get(adjVertex) > distance.get(smallestDistanceVertex) + time) {
                        // relaxation
                        int newTime = distance.get(smallestDistanceVertex) + time;
                        distance.put(adjVertex, newTime);
                        prevVertexToDeparture.put(adjVertex, smallestDistanceVertex);

                        // 이전에 들어있던 adjVertex와 longerTime은 어차피 if문을 통과하지 못해서 poll되고 끝남
                        priorityQueue.add(new ListItem(adjVertex, newTime));
                    }
                }
            }
        }

        return new Pair<>(distance, prevVertexToDeparture);
    }

    public void printDFS() {
        Set<StationVertex> unvisited = new HashSet<>(vertices.keySet());
        dfsHelper((StationVertex) unvisited.toArray()[0], unvisited);
    }

    private void dfsHelper(StationVertex vertex, Set<StationVertex> unvisited) {
        System.out.println(vertex);
        unvisited.remove(vertex);

        if (!unvisited.isEmpty()) {
            for (ListItem adjVertexItem : vertices.get(vertex)) {
                if (unvisited.contains(adjVertexItem.vertex)) {
                    System.out.println("time: " + adjVertexItem.time);
                    dfsHelper(adjVertexItem.vertex, unvisited);
                }
            }
        }
    }

    public static SubwayGraph buildGraph(String dataPath) {
        try {
            SubwayGraph subwayGraph = new SubwayGraph();

            BufferedReader br = new BufferedReader(new FileReader(dataPath));

            String stationString;

            // insert nodes
            while ((stationString = br.readLine()) != null) {
                if (stationString.equals("")) {
                    break;
                }
                String[] stationInfo = stationString.split(" ");
                subwayGraph.insertVertex(stationInfo[0], stationInfo[1], stationInfo[2]);
            }

            // insert edges
            String vertexString;

            while (((vertexString = br.readLine()) != null) && (!vertexString.equals(""))){
                String[] edgeInfo = vertexString.split(" ");
                subwayGraph.insertEdge(edgeInfo[0], edgeInfo[1], Integer.parseInt(edgeInfo[2]));
                // from 0, to 1, time: 2

            }
            return subwayGraph;

        } catch (IOException e) {
            System.out.println("Data File Not Found: " + e.toString());
        }
        return new SubwayGraph();
    }
}
