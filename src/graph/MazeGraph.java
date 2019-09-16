package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/* STUDENTS:  SEE THE PROJECT DESCRIPTION FOR A MUCH
	 * MORE DETAILED EXPLANATION ABOUT HOW TO WRITE
	 * THIS CONSTRUCTOR
	 */
	
	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		super();
		//add all vertex
		for(int Y=0;Y<maze.getMazeHeight();Y++){
			for(int X=0;X<maze.getMazeWidth();X++){
				super.addVertex(new Juncture(X,Y));

			}
		}
		
		//add edge
		for(int Y=0;Y<maze.getMazeHeight();Y++){
			for(int X=0;X<maze.getMazeWidth();X++){
				Juncture juncture=new Juncture(X,Y);
				//add above
				
				if(!maze.isWallAbove(juncture)){
					Juncture above=new Juncture(X,Y-1);
					super.addEdge(juncture, above, maze.getWeightAbove(juncture));
				}
				
				//add below
				
				if(!maze.isWallBelow(juncture)){
					Juncture below=new Juncture(X,Y+1);
					super.addEdge(juncture, below, maze.getWeightBelow(juncture));
				}
				
				//add left
				
				if(!maze.isWallToLeft(juncture)){
					Juncture left=new Juncture(X-1,Y);
					super.addEdge(juncture, left, maze.getWeightToLeft(juncture));
				}
				
				//add right
				
				if(!maze.isWallToRight(juncture)){
					Juncture right=new Juncture(X+1,Y);
					super.addEdge(juncture, right, maze.getWeightToRight(juncture));
				}
				
				
			}
		}
		
	}
}
