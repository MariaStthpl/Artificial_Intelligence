package ai;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class Ask1 {
    public static void main(String[] args){

        HashMap< Integer, AStarNode > Taxis = null;
        Preprocess.Result res = null;

        AStarNode client = null;

        try {
            res = Preprocess.readNodes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert res != null;
            client = Preprocess.readClient(res.Roads);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Taxis = Preprocess.readTaxis(res.Roads);
        } catch (IOException e) {
            e.printStackTrace();
        }




        assert Taxis != null;

        HashMap<Integer, Pair<Double, LinkedList<AStarNode>>> Costs = new HashMap<>();

        LinkedList<AStarNode> Output;
        Pair<Double, LinkedList<AStarNode>> pair;
        Integer best_taxi = 0;

        double mincost = Double.MAX_VALUE;
        for (Integer key : Taxis.keySet()) {
            Output = AStar.findPath(Taxis.get(key), client, res.Junctions, res.Roads);
            if (Output!= null) {
                AStarNode cost = Output.getLast();
                pair = new Pair<>(cost.g, Output);
                Costs.put(key, pair);
                if (cost.g < mincost){
                    mincost = cost.g;
                    best_taxi = key;
                }
            }
        }

        try {
            Preprocess.writeFile(Costs);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Integer y = best_taxi;
    }
}