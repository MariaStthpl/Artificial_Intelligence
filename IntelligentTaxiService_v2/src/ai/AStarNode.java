package ai;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.IOException;

public class AStarNode implements Comparable<AStarNode> {

    public AStarNode pathParent;
    public double g;
    public double h;

    public double x;
    public double y;

    public int id;

    public double getf(){
        return this.g+this.h;
    }

    @Override
    public int compareTo(AStarNode object) {
        return (object.getf() < this.getf())?-1:1;
    }

    //euclidean
    public double getEstimatedCost(AStarNode goalNode) throws JIPSyntaxErrorException, IOException{
        JIPEngine jip = new JIPEngine();
        jip.consultFile("input/client.pl");
        jip.consultFile("input/lines.pl");
        jip.consultFile("input/nodes.pl");
        jip.consultFile("input/AI.pl");

        JIPTermParser parser = jip.getTermParser();

        JIPQuery jipQuery;
        JIPTerm term;

        int F = 0, T=0;
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("parametroi_A_star("+id+",T,F)."));
        term = jipQuery.nextSolution();
        if (term != null) {
            T = Integer.parseInt(term.getVariablesTable().get("T").toString());
            F = Integer.parseInt(term.getVariablesTable().get("F").toString());
        }
        double eucl = sqrt(pow((this.x - goalNode.x),2) + pow(this.y - goalNode.y,2));
        double ret = eucl/T+F;
        return ret;
    }

    public ArrayList<AStarNode> getNeighbors() throws JIPSyntaxErrorException, IOException{
        ArrayList<AStarNode> neighbors = new ArrayList<>();

        JIPEngine jip = new JIPEngine();
        jip.consultFile("input/client.pl");
        jip.consultFile("input/lines.pl");
        jip.consultFile("input/nodes.pl");
        jip.consultFile("input/AI.pl");

        JIPTermParser parser = jip.getTermParser();

        JIPQuery jipQuery, jipQuery1;
        JIPTerm term;

        int X;
        int Y;

        jipQuery = jip.openSynchronousQuery(parser.parseTerm("all_geitones("+id+"X,Y)."));
        term = jipQuery.nextSolution();
        while (term != null) {
            X = Integer.parseInt(term.getVariablesTable().get("X").toString());
            Y = Integer.parseInt(term.getVariablesTable().get("Y").toString());

            if (X!=0){
                AStarNode neighbor = new AStarNode();
                neighbor.id = X;
                jipQuery1 = jip.openSynchronousQuery(parser.parseTerm("nodes2(NX,NYY,LID,"+Integer.toString(X)+",_)."));
                term = jipQuery1.nextSolution();
                if (term != null){
                    neighbor.x = Double.parseDouble(term.getVariablesTable().get("NX").toString());
                    neighbor.y = Double.parseDouble(term.getVariablesTable().get("NY").toString());
                }
                neighbors.add(neighbor);
            }
            if (Y!=0){
                AStarNode neighbor = new AStarNode();
                neighbor.id = Y;
                jipQuery1 = jip.openSynchronousQuery(parser.parseTerm("nodes2(NX,NYY,LID,"+Integer.toString(Y)+",N)."));
                term = jipQuery1.nextSolution();
                if (term != null){
                    neighbor.x = Integer.parseInt(term.getVariablesTable().get("NX").toString());
                    neighbor.y = Integer.parseInt(term.getVariablesTable().get("NY").toString());
                }
                neighbors.add(neighbor);
            }
        }

    return (neighbors);
    }
}