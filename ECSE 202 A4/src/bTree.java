import java.util.LinkedList;

/**
 * Implements a B-Tree class using a NON-RECURSIVE algorithm.
 * 
 *
 */

public class bTree {
	
	// Instance variables
	
	bNode root=null;
	double DELTASIZE = 0.1;
	double X = 0;
	double Y = 0;
	double lastSize =  0;
	int NUMBALLS = 1000;
	double SCALE = 6;
	double currentSize = 0;
	aBall myBall;
	LinkedList<aBall> ballList = new LinkedList();

	
	
	
/**
 * addNode method - adds a new node by descending to the leaf node
 *                  using a while loop in place of recursion.  Ugly,
 *                  yet easy to understand.
 */
	
	
	public void addNode(aBall redBall) {  //the argument is of type aBall
		
		bNode current;

// Empty tree
		
		if (root == null) {
			root = makeNode(redBall);
		}
		
// If not empty, descend to the leaf node according to
// the input data.  
		
		else {
			current = root;
			while (true) {
				
				if (redBall.getCurrentSize() < current.myBall.getCurrentSize()) {
					
// New data < data at node, branch left
					
					if (current.left == null) {				// leaf node
						current.left = makeNode(redBall);		// attach new node here
						break;
					}
					else {									// otherwise
						
						current = current.left;				// keep traversing
					}
				}
				else {
// New data >= data at node, branch right
					
					if (current.right == null) {			// leaf node	
						current.right = makeNode(redBall);		// attach
						break;
					}
					else {									// otherwise 
						
						current = current.right;			// keep traversing
					}
				}
			}
		}
		
	}
	
/**
 * makeNode
 * 
 * Creates a single instance of a bNode
 * 
 * @param	int data   Data to be added
 * @return  bNode node Node created
 */
	
	public bNode makeNode(aBall redBall) {
		bNode node = new bNode();							// create new object
		node.myBall = redBall;								// initialize data field
		node.left = null;									// set both successors
		node.right = null;									// to null
		return node;										// return handle to new object
	}
	
	/**
	 * isRunning tests if the balls are still bounving  or not
	 */
	
	int i = 0;
	public boolean isRunning() {
		while (i<NUMBALLS) { //goes through each ball at a time in list
			while(ballList.get(i).stillGoing != false); //waits while still moving
			i++; //next ball
			
		}
		return false; //begin next step
	}
	
	/**
	 * stackBalls method takes a list of balls in order and stacks them according to size
	 */
	
	public void stackBalls() {
		
		double firstRad = ballList.getFirst().getCurrentSize(); //gets first size
		X= 0;
		Y = 600-(firstRad*2*SCALE);
		ballList.getFirst().moveTo(X,Y); //places ball at bottom left corner
		for(int i=1; i<NUMBALLS; i++) {
			currentSize = ballList.get(i).getCurrentSize(); //gets current size
			lastSize = ballList.get(i-1).getCurrentSize();  //gets previous size
			if(currentSize - lastSize > DELTASIZE) { //if ball is larger
				X = X+(currentSize*2*SCALE); 
				Y = 600 -(currentSize*2*SCALE);
				ballList.get(i).moveTo(X, Y); //new row
				
			}
			else {
				Y = Y-(currentSize*2*SCALE);
				ballList.get(i).moveTo(X, Y); //placed on top of previous ball
				//put current ball on top of last ball
			}
			if (ballList.get(i) == ballList.getLast()) {
				break;
			}
		}
	
	}

	
/**
 * inorder method - inorder traversal via call to recursive method
 */
	
	public void inorder() {									// hides recursion from user
		traverse_inorder(root);
	}
	
/**
 * traverse_inorder method - recursively traverses tree in order (LEFT-Root-RIGHT) and creates linked list of balls
 */
	
	private LinkedList traverse_inorder(bNode root) {
		if (root.left != null) traverse_inorder(root.left);
		ballList.add(root.myBall); //creates list
		if (root.right != null) traverse_inorder(root.right);
		return ballList; //return list
	}
	
}
/**
 * A simple bNode class for use by bTree.  The "payload" can be
 * modified accordingly to support any object type.
 * 
 * @author ferrie
 *
 */

class bNode {
	aBall myBall; //node is ball
	bNode left; //bNode??
	bNode right; //bNode??
}


