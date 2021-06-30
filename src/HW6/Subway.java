package HW6;

import java.io.*;

public class Subway {
    public static SubwayGraph graph;
    public static BufferedWriter bw;

    public static void main(String[] args) {
        String dataPath = args[0];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter(new OutputStreamWriter(System.out));
        graph = SubwayGraph.buildGraph(dataPath);

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0) {
                    bw.close();
                    break;
                }
                command(input);
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }

        }
    }

    private static void command(String input) {
        try {
            String[] stations = input.split(" ");
            if (stations.length == 2) {
                bw.write(graph.printShortestRoute(stations[0], stations[1]));
                bw.newLine();
                bw.flush();
            } else {
                System.out.println("Invalid command\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
