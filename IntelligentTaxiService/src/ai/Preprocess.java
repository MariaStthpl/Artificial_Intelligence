package ai;

import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Preprocess {

    //public static HashMap< Integer, Road > Roads = new HashMap<>();

    public static class Result{
        HashMap< Pair <Double,Double>, ArrayList<Integer>> Junctions;
        HashMap< Integer, Road > Roads;
    }

    public static Result readNodes() throws IOException{
    	FileInputStream fis = new FileInputStream("GivenData/nodes.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        HashMap< Integer, Road > Roads = new HashMap<>();
        HashMap< Pair <Double,Double>, ArrayList<Integer>> Junctions = new HashMap<>();

        Road tempRoad;

        Pair<Double, Double> pair;
        double[] coord;
        int id;
        String name;

        String nline;
        br.readLine();
        String[] line;
        while ((nline = br.readLine()) != null){
            line = nline.split(",");


            coord = new double[2];
            coord[0] = Double.parseDouble(line[0]);
            coord[1] = Double.parseDouble(line[1]);
            pair = new Pair<>(coord[0], coord[1]);
            id   = Integer.parseInt(line[2]);
            if (line.length == 4)
                name = line[3];
            else
                name = "Unknown";

            if (Roads.containsKey(id)){
                tempRoad = Roads.remove(id);
                tempRoad.coordinates.add(coord);
                Roads.put(id, tempRoad);
            } else {
                tempRoad = new Road();
                tempRoad.id = id;
                tempRoad.coordinates.add(coord);
                tempRoad.name = name;
                Roads.put(id, tempRoad);
            }

            ArrayList<Integer> tempJunction = new ArrayList<>();
            if (Junctions.get(pair) == null){
                tempJunction.add(id);
                Junctions.put(pair, tempJunction);
            }
            else {
                tempJunction = Junctions.remove(pair);
                tempJunction.add(id);
                Junctions.put(pair, tempJunction);
            }

        }
        br.close();


        Junctions.keySet().removeIf(key -> Junctions.get(key).size() == 1);

        /*
        Iterator<Pair<Double, Double>> iterator = Junctions.keySet().iterator();
        while(iterator.hasNext()){
            Pair<Double, Double> key = iterator.next();
            if (Junctions.get(key).size() == 1){
                iterator.remove();
            }
        }
        */
        Result res = new Result();
        res.Junctions = Junctions;
        res.Roads = Roads;
        return(res);
    }

    public static AStarNode readClient(HashMap<Integer, Road> Roads) throws IOException{
        FileInputStream fis = new FileInputStream("GivenData/client.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        br.readLine();
        String[] line;
        line = br.readLine().split(",");

        double x = Double.parseDouble(line[0]);
        double y = Double.parseDouble(line[1]);
        int id = -1;
        double nx = x;
        double ny = y;

        Iterator<Integer> iterator = Roads.keySet().iterator();
        double min = Double.MAX_VALUE;
        while(iterator.hasNext()){
            Integer key = iterator.next();
            for (int i=0; i<Roads.get(key).coordinates.size(); i++){
                double[] pair = new double[2];
                pair = Roads.get(key).coordinates.get(i);
                double temp = sqrt(pow((pair[0]-x),2) + pow((pair[1]-y),2));
                if (temp < min ){
                    id = key;
                    nx = pair[0];
                    ny = pair[1];
                    min = temp;
                }
            }
        }

        AStarNode client = new AStarNode();
        client.x = nx;
        client.y = ny;
        client.id = id;

        return(client);
    }

    public static HashMap< Integer, AStarNode > readTaxis(HashMap<Integer, Road> Roads) throws IOException{
    	FileInputStream fis = new FileInputStream("GivenData/taxis.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String nline;
        br.readLine();
        String[] line;

        HashMap<Integer, AStarNode> Taxis= new HashMap<>();

        while ((nline = br.readLine()) != null){
            line = nline.split(",");

            double x = Double.parseDouble(line[0]);
            double y = Double.parseDouble(line[1]);
            Integer idt = Integer.parseInt(line[2]);
            int id = -1;
            double nx = x;
            double ny = y;

            Iterator<Integer> iterator = Roads.keySet().iterator();
            double min = Double.MAX_VALUE;
            while(iterator.hasNext()){
                Integer key = iterator.next();
                for (int i=0; i<Roads.get(key).coordinates.size(); i++){
                    double[] pair = Roads.get(key).coordinates.get(i);
                    double temp = sqrt(pow((pair[0]-x),2) - pow((pair[1]-y),2));
                    if (temp < min ){
                        id = key;
                        nx = pair[0];
                        ny = pair[1];
                        min = temp;
                    }
                }
            }

            AStarNode taxi = new AStarNode();
            taxi.x = nx;
            taxi.y = ny;
            taxi.id = id;

            Taxis.put(idt, taxi);
        }

        return(Taxis);
    }

    public static void writeFile(HashMap<Integer, Pair<Double, LinkedList<AStarNode>>> Taxis) throws IOException{
    	FileWriter fw = new FileWriter("Output/out.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for (Integer key : Taxis.keySet() ){
            bw.write(String.valueOf(key)+"\n");
            for (AStarNode node : Taxis.get(key).getValue()){
                bw.write(String.valueOf(node.x)+","+String.valueOf(node.y)+",0\n");
            }
            bw.write("\n\n\n\n");
        }
        bw.close();
        fw.close();
    }
}
