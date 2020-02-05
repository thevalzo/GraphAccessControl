package it.unimib.disco.janusgraphac.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.*;
import org.apache.tinkerpop.gremlin.process.traversal.*;

import org.apache.tinkerpop.gremlin.structure.Graph;

public class GraphAccessControl {
	
	private Graph graph;
	private GraphTraversalSource g;
	private List <Map<String, Object>> temp_users;
	private Map<String, Object> users;
	private Map<String, Object> user;
	private int clearance;
	
	public GraphAccessControl(Graph graph) {
		 this.graph= graph;
		 g = graph.traversal();
		 temp_users = g.V().has("label_vp","user").valueMap().toList();
		 users =  new HashMap<String, Object>();;
		 for(int i=0;i<temp_users.size();i++) {
			 String username=temp_users.get(i).get("username").toString().replace("[", "").replace("]", "");
			 users.put(username, temp_users.get(i));
		 }
	}
	
	public GraphTraversalSource getCivilianStrategy(String username) throws UserNotFoundException{
		if(users.containsKey(username) == true) {
			user = (Map<String, Object>) users.get(username);
		}
		else {
			throw new UserNotFoundException();
		}
 		clearance = Integer.parseInt(user.get("level").toString().replace("[", "").replace("]", ""));
 		
 		List vertices = g.V().has("label_vp","resource").has("level",P.lte(clearance)).aggregate("total").V().has("username",username).outE().has("label_ep","part_of").inV().outE().has("access_rights","cannot").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).cap("x").select("total").unfold().where(P.without("x")).id().toList();
 		List exceptions = g.V().has("username",username).outE().has("access_rights","can").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).has("level",P.lte(clearance)).cap("x").unfold().id().toList();
 		vertices.addAll((List) exceptions);
        GraphTraversalSource sg=graph.traversal().withStrategies(SubgraphStrategy.build().vertices(__.hasId(P.within(vertices))).create());        
        return sg;
 	}
	
 	public GraphTraversalSource getMilitaryStrategy(String username) throws UserNotFoundException{
 		if(users.containsKey(username) == true) {
			user = (Map<String, Object>) users.get(username);
		}
		else {
			throw new UserNotFoundException();
		}
 		clearance = Integer.parseInt(user.get("level").toString().replace("[", "").replace("]", ""));
 		
 		 List vertices = g.V().has("username",username).outE().has("label_ep","part_of").inV().outE().has("access_rights","can").inV().aggregate("x").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("x")).until(__.not(__.outE().has("access_rights","extends_rights"))).V().has("username",username).outE().has("access_rights","cannot").inV().aggregate("y").repeat(__.outE().has("access_rights","extends_rights").inV().aggregate("y")).until(__.not(__.outE().has("access_rights","extends_rights"))).cap("y").select("x").unfold().where(P.without("y")).has("label_vp","resource").has("level",P.lte(clearance)).id().toList();
	     GraphTraversalSource sg=graph.traversal().withStrategies(SubgraphStrategy.build().vertices(__.hasId(P.within(vertices))).create());
         return sg;
 	}
 	
 	public class UserNotFoundException extends Exception {
 		  
 		  public UserNotFoundException() { 
 			  super("User not found in the graph"); }

 		}
}
