package it.unimib.disco.janusgraphac.application;
import it.unimib.disco.janusgraphac.application.GraphAccessControl;
import java.util.*;
import java.io.*;

import java.util.*;
import java.io.*;

import org.apache.tinkerpop.gremlin.jsr223.JavaTranslator;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.*;
import org.apache.tinkerpop.gremlin.process.traversal.*;


import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.io.graphml.*;

import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.janusgraph.core.JanusGraphFactory;

public class Runable {
public static void main(String[] args) {
		// TODO Auto-generated method stub
				//Graph graph = JanusGraphFactory.open("conf/janusgraph-hbase-es.properties");
		        try{

			        Graph graph =  TinkerGraph.open();
			        graph.io(GraphMLIo.build()).readGraph("C:\\Users\\marco\\Documents\\Workspaces\\JanusgraphAccessControl\\JanusgraphAccessControl\\data\\users-privileges-civilian.graphml");
			        GraphTraversalSource g=graph.traversal();       
	                GraphAccessControl gac = new GraphAccessControl(graph);
	                
	                GraphTraversalSource sg = gac.getCivilianStrategy("Rita Levi Montalcini");
	                System.out.println(sg.V().next(10).toString());
	                
	                Graph graphm =  TinkerGraph.open();
			        graphm.io(GraphMLIo.build()).readGraph("C:\\Users\\marco\\Documents\\Workspaces\\JanusgraphAccessControl\\JanusgraphAccessControl\\data\\users-privileges-military.graphml");   
			        GraphAccessControl gacm = new GraphAccessControl(graphm);
	                
			        GraphTraversalSource sgm = gacm.getMilitaryStrategy("Rita Levi Montalcini");
	                System.out.println(sgm.V().next(10).toString());
	                
	              
		        }catch (Exception e){
		        e.printStackTrace();
		        }
			}
	}



