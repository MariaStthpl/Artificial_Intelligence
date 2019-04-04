package ai;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static ai.AStar.findPath;

public class Ask2 {
    public static void main(String[] args) throws JIPSyntaxErrorException, IOException{

        JIPEngine jip = new JIPEngine();
        jip.consultFile("input/client.pl");
        jip.consultFile("input/lines.pl");
        jip.consultFile("input/nodes.pl");
        jip.consultFile("input/taxis.pl");
        jip.consultFile("input/traffic.pl");
        jip.consultFile("input/AI.pl");


        JIPTermParser parser = jip.getTermParser();

        JIPQuery jipQuery;
        JIPTerm term;
/*
        System.out.println("Client!");
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("client(X,Y,DX,DY,Time,Persons,Language,Luggage)."));
        term = jipQuery.nextSolution();
        if (term != null)
            System.out.println("Client... X = "+ term.getVariablesTable().get("X").toString()+" Y = "+ term.getVariablesTable().get("Y").toString());
*/
        HashMap<Integer, Pair<Double, LinkedList<AStarNode>>> Costs =  new HashMap<>();
        LinkedList<AStarNode> Output;
        double mincost = Double.MAX_VALUE;
        Integer best_taxi = 0;

        System.out.println("Find all taxis by id!");
        int id;
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxiIsAvailable(I)."));
        term = jipQuery.nextSolution();
        while (term != null) {
            id = Integer.valueOf(term.getVariablesTable().get("I").toString());
            System.out.println(Integer.toString(id));
            Output = findPath(id);
            Pair<Double, LinkedList<AStarNode>> pair;
            if (Output!= null) {
                AStarNode cost = Output.getLast();
                pair = new Pair<>(cost.g, Output);
                Costs.put(id, pair);
                if (cost.g < mincost){
                    mincost = cost.g;
                    best_taxi = id;
                }
                System.out.println("Taxi with ID: "+ id+ " --- Cost:   "+ cost.g +" ");
            }
            term = jipQuery.nextSolution();
        }
        return;
    }
}