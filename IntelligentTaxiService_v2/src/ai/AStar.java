package ai;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.IOException;

public class AStar {

    //Priority List (or priority queue): objects are ordered by their priority
    //minimum estimated cost to goalNode
    public static LinkedList<AStarNode> constructPath(AStarNode node){
        LinkedList<AStarNode> path = new LinkedList<>();
        while (node.pathParent != null){
            path.addFirst(node);
            node = node.pathParent;
        }
        return path;
    }


    public static LinkedList<AStarNode> findPath(int Tid) throws JIPSyntaxErrorException, IOException{
        PriorityQueue<AStarNode> openList = new PriorityQueue<>((x,y) -> (x.getf()<y.getf())?-1:1);
        LinkedList<AStarNode> closedList = new LinkedList<>();

        JIPEngine jip = new JIPEngine();
        jip.consultFile("input/client.pl");
        jip.consultFile("input/lines.pl");
        jip.consultFile("input/nodes.pl");
        jip.consultFile("input/taxis.pl");
        //jip.consultFile("input/traffic.pl");
        jip.consultFile("input/AI.pl");

        JIPTermParser parser = jip.getTermParser();

        JIPQuery jipQuery;
        JIPTerm term;

        System.out.println("Define StartNode");
        double X = 0, Y =0;
        AStarNode startNode = new AStarNode();
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxi2(X,Y,"+Tid+",A,C,La,R,L,T)."));
        term = jipQuery.nextSolution();
        if (term != null) {
            X = Double.parseDouble(term.getVariablesTable().get("X").toString());
            Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
        }
        double min = Double.MAX_VALUE;
        double temp_dist;
        int nid = 0;
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes2(NX,NY,_,NID,_)."));
        term = jipQuery.nextSolution();
        while (term != null) {
            temp_dist = sqrt(pow((X - Double.parseDouble(term.getVariablesTable().get("NX").toString())),2) + pow(Y - Double.parseDouble(term.getVariablesTable().get("NY").toString()),2));
            if (temp_dist < min){
                min = temp_dist;
                nid = Integer.parseInt(term.getVariablesTable().get("NID").toString());
            }
            term = jipQuery.nextSolution();
        }
        int tempnid=0;
        startNode.id= nid;
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes2(X,Y,_,NID,_)."));
        term = jipQuery.nextSolution();
        while (term != null) {
            tempnid = Integer.parseInt(term.getVariablesTable().get("NID").toString());
            if (tempnid==nid) {
                startNode.x = Double.parseDouble(term.getVariablesTable().get("X").toString());
                startNode.y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
                break;
            }
            term = jipQuery.nextSolution();
        }



        //*--------------------------------------------------------------------------------------------------------*//

        System.out.println("Define GoalNode");
        X = 0;
        Y =0;
        AStarNode goalNode = new AStarNode();
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("client2(X,Y,DX,DY,T,P,La,Lu)."));
        term = jipQuery.nextSolution();
        if (term != null) {
            X = Double.parseDouble(term.getVariablesTable().get("X").toString());
            Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
        }

        min = Double.MAX_VALUE;
        nid = 0;
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes2(NX,NY,_,NID,_)."));
        term = jipQuery.nextSolution();
        while (term != null) {
            temp_dist = sqrt(pow((X - Double.parseDouble(term.getVariablesTable().get("NX").toString())),2) + pow(Y - Double.parseDouble(term.getVariablesTable().get("NY").toString()),2));
            if (temp_dist < min){
                min = temp_dist;
                nid = Integer.parseInt(term.getVariablesTable().get("NID").toString());
            }
            term = jipQuery.nextSolution();
        }

        String ONID = Integer.toString(nid);
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("vres_kontinotero_dromo("+ONID+",NID)."));
        term = jipQuery.nextSolution();
        if (term != null) {
            goalNode.id = Integer.parseInt(term.getVariablesTable().get("NID").toString());
        }

        String GoalID = Integer.toString(goalNode.id);
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes2(X,Y,_,"+GoalID+",_)."));
        term = jipQuery.nextSolution();
        if (term != null) {
            goalNode.x = Double.parseDouble(term.getVariablesTable().get("X").toString());
            goalNode.y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
        }


        //*--------------------------------------------------------------------------------------------------------*//


        startNode.g = 0.0;
        startNode.h = startNode.getEstimatedCost(goalNode);
        startNode.pathParent = null;
        openList.add(startNode);

        while(!openList.isEmpty()){
            AStarNode node = openList.poll();
            if ((node.x == goalNode.x)&&(node.y == goalNode.y)){
                //construct the path from start to goal
                return constructPath(node);
            }

            ArrayList<AStarNode> neighbors = node.getNeighbors();
            assert neighbors!=null;
            for (AStarNode neighborNode : neighbors) {

                //boolean isOpen = openList.contains(neighborNode);
                boolean isOpen = false;
                for (AStarNode temp : openList){
                    if ((temp.x == neighborNode.x) && (temp.y == neighborNode.y)){
                        isOpen = true;
                        break;
                    }
                }

                //boolean isClosed = closedList.contains(neighborNode);
                boolean isClosed = false;
                for (AStarNode temp : closedList){
                    if ((temp.x == neighborNode.x) && (temp.y == neighborNode.y)){
                        isClosed = true;
                        break;
                    }
                }


                double g = node.g + node.getEstimatedCost(neighborNode);

                //check if the neighbor node has not been traversed or
                // if a shorter path to this neighbor node is found
                if ((!isOpen && !isClosed) || g < neighborNode.g) {
                    neighborNode.pathParent = node;
                    neighborNode.g = g;
                    neighborNode.h = neighborNode.getEstimatedCost(goalNode);
                    if (isClosed)
                        closedList.remove(neighborNode);
                    if (!isOpen)
                        openList.add(neighborNode);
                }
            }
            closedList.add(node);
        }

        //no path found
        return null;
    }
}