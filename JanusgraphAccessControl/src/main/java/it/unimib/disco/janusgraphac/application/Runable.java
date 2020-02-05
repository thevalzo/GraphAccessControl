package it.unimib.disco.janusgraphac.application;

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
		        graph.io(GraphMLIo.build()).readGraph("../data/users-privileges-civilian.graphml");
		        GraphTraversalSource g=graph.traversal();       
                    
                    //CIVILIAN
		        List vertices = g.V().has("label_vp","resource").has("level",P.lte(2)).aggregate("total").V().has("name","Rita Levi Montalcini").outE().has("label_ep","part_of").inV().outE().has("access_rights","cannot").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).cap("x").select("total").unfold().where(P.without("x")).id().toList();
		        List exceptions = g.V().has("name","Rita Levi Montalcini").outE().has("access_rights","can").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).has("level",P.lte(2)).cap("x").unfold().id().toList();
		        vertices.addAll((List) exceptions);
		        GraphTraversalSource sg=graph.traversal().withStrategies(SubgraphStrategy.build().vertices(__.hasId(P.within(vertices))).create());
                    
                    
                    
                 //MILITARY
                    
                graph.io(GraphMLIo.build()).readGraph("../data/users-privileges-military.graphml");
		        GraphTraversalSource g=graph.traversal();     
             List vertices = g.V().has("name","Rita Levi Montalcini").outE().has("label_ep","part_of").inV().outE().has("access_rights","can").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).V().has("name","Rita Levi Montalcini").outE().has("access_rights","cannot").inV().aggregate("y").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("y")).until(__.not(__.outE().has("access_rights","extends_rights"))).cap("y").select("x").unfold().where(P.without("y")).id().toList();
		       
		        vertices.addAll((List) exceptions);
		        GraphTraversalSource sg=graph.traversal().withStrategies(SubgraphStrategy.build().vertices(__.hasId(P.within(vertices))).create());
                           
                    
                    
		        System.out.println(sg.V().next(10).toString());
                    
                    
                    
		        }catch (Exception e){
		        System.out.println(e.toString());
		        }
			}
	}
