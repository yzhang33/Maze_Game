package graph;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */






	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;
	private Map<V,Map<V,Integer>> graph;
	private HashMap<V,Integer> cost;    //cost of one node to another
	Map<V,V> pred; //predecessor
	Set<V> unSettledNode;  //unsettled node
	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {

		graph=new HashMap<V,Map<V,Integer>>();
		observerList=new ArrayList<GraphAlgorithmObserver<V>>();
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if(containsVertex(vertex)){
			throw new IllegalArgumentException();
		}
		Map<V,Integer> tmp=new HashMap<V,Integer>();
		graph.put(vertex,tmp);
	}

	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		return graph.containsKey(vertex);
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if(!containsVertex(from)||!containsVertex(to)||weight<0){
			throw new IllegalArgumentException();
		}

		graph.get(from).put(to, weight);

	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if(!graph.containsKey(from)||!graph.containsKey(to)){
			throw new IllegalArgumentException();
		}

		return graph.get(from).get(to);
	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		for(GraphAlgorithmObserver<V> e:observerList){
			e.notifyBFSHasBegun();
		}
		//a queue add start
		Queue<V> que=new LinkedList<V>();
		Set<V> finishedSet=new LinkedHashSet<V>();
		que.offer(start);
		//while loop go through BFS
		while(!que.isEmpty()){
			V vertex=que.poll();
			//if finished set do not contain this vertex
			if(!finishedSet.contains(vertex)){
				//visit vertex
				for(GraphAlgorithmObserver<V> e:observerList){
					e.notifyVisit(vertex);
				}
				if(vertex.equals(end)){
					//call search is end vertex
					for(GraphAlgorithmObserver<V> element:observerList){
						element.notifySearchIsOver();
					}	
					break;
				}
				//add vertex to finished set
				finishedSet.add(vertex);
				ArrayList<V> list=neighbour(vertex);
				//for every children vertex has
				for(V e:list){
					if(!finishedSet.contains(e)){
						que.offer(e);
						//if reached end vertex

					}
				}
			}

		}

	}



	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		for(GraphAlgorithmObserver<V> e:observerList){
			e.notifyDFSHasBegun();
		}
		Set<V> visitedSet=new LinkedHashSet<V>();
		Stack<V> s=new Stack<V>();
		s.push(start);

		while(!s.isEmpty()){
			V vertex=s.pop();
			//not in finished set
			if(!visitedSet.contains(vertex)){
				for(GraphAlgorithmObserver<V> e:observerList){
					e.notifyVisit(vertex);
				}
				if(vertex.equals(end)){
					//call search is end vertex
					for(GraphAlgorithmObserver<V> e:observerList){
						e.notifySearchIsOver();
					}
					break;
				}
				//add to visited set
				visitedSet.add(vertex);
				ArrayList<V> list=neighbour(vertex);
				for(V element:list){
					if(!visitedSet.contains(element)){
						s.push(element);

					}
				}
			}

		}
	}


	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {

		for(GraphAlgorithmObserver<V> observer:observerList){
			observer.notifyDijkstraHasBegun();
		}
		int numofVer=graph.size();
		Set<V> finished=new LinkedHashSet<V>();
		cost=new HashMap<V,Integer>(numofVer);
		unSettledNode=new HashSet<V>();
		pred=new HashMap<V,V>();

		cost.put(start, 0);
		unSettledNode.add(start);
		while(unSettledNode.size()>0){
			//find min vertex
			V minNode=getMinNode(unSettledNode);
			//find min cost
			Integer shorest=cost.get(minNode);
			//add to finished set
			finished.add(minNode);
			//notify that minNode added to finished set
			for(GraphAlgorithmObserver<V> observer:observerList){
				observer.notifyDijkstraVertexFinished(minNode,shorest);
			}
			//remove this node from unsettled nodes set
			unSettledNode.remove(minNode);
			//find the shortest path from this node to other vertex
			findMinPath(minNode);

		}
		//find path and notify it
		for(GraphAlgorithmObserver<V> observer:observerList){
			observer.notifyDijkstraIsOver(getPath(end));
		}
	}
	/**
	 * find the min path in Dijsktra
	 * @param minNode
	 */
	private void findMinPath(V minNode) {
		ArrayList<V> adjacentNodes = neighbour(minNode);
		for (V target : adjacentNodes) {
			//if path is larger
			if (getShortestDistance(target) > getShortestDistance(minNode)
					+ getDistance(minNode, target)) {
				cost.put(target, getShortestDistance(minNode)
						+ getDistance(minNode, target));
				pred.put(target, minNode);
				unSettledNode.add(target);
			}
		}
	}
	/**
	 * get the weight between two nodes
	 * @param minNode
	 * @param target
	 * @return
	 */
	private int getDistance(V minNode, V target) {
		return graph.get(minNode).get(target);
	}
	/**
	 * find the min vertex in unSettledNode
	 * @param unSettledNode
	 * @return
	 */
	private V getMinNode(Set<V> unSettledNode) {
		V minimum = null;
		for (V vertex : unSettledNode) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;

	}
	/**
	 * find shortest distance 
	 * if cost is null return max Integer
	 * @param destination
	 * @return
	 */
	private int getShortestDistance(V destination) {
		Integer d = cost.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}
	/**
	 * find the path of Dijkstra
	 * @param target
	 * @return
	 */
	public LinkedList<V> getPath(V target) {
		LinkedList<V> path = new LinkedList<V>();
		V step = target;
		// check if a path exists
		if (pred.get(step) == null) {
			return null;
		}
		path.add(step);
		while (pred.get(step) != null) {
			step = pred.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

	/**
	 * helper to find neighbors of a node
	 * @param vertex
	 * @return
	 */
	private ArrayList<V> neighbour(V vertex) {
		ArrayList<V> list=new ArrayList<V>();
		for(Map.Entry<V, Integer> entry:graph.get(vertex).entrySet()){
			V ver=entry.getKey();
			list.add(ver);
		}
		return list;
	}


}
