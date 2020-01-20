package it.unimib.disco.janusgraphac.application;

import org.apache.tinkerpop.gremlin.jsr223.JavaTranslator;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
public class Runable {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = JanusGraphFactory.open("conf/janusgraph-hbase-es.properties");
	}
}
