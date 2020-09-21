import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.gui.DoubleField;
import acm.gui.IntField;
import acm.gui.TableLayout;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class bSim  extends GraphicsProgram{
	
	
	//initiate variables
	private static final int WIDTH = 1200; // n.b. screen coordinates
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;
	private static final double SCALE = HEIGHT/100; // pixels per meter
	private boolean simEnable;
	public volatile static boolean addTrace = false;
	public volatile static boolean addTraceRun = false;
	
	//initiate field
	private IntField numBalls;
	private DoubleField minSize;
	private DoubleField maxSize;
	private DoubleField lossMin;
	private DoubleField lossMax;
	private DoubleField minVel;
	private DoubleField maxVel;
	private DoubleField thetaMin;
	private DoubleField thetaMax;
	private JButton trace;
	private JButton run;
	private JButton stack;
	private JButton stop;
	private JButton clear;
	private JButton quit;
	public int nBalls;
	bTree myTree = new bTree();
	aBall redBall;
	RandomGenerator rgen = RandomGenerator.getInstance();
	

	
	
	
	
	
	
	
	
	//set up user interface
	public void run() {
		
		
		addTrace = false;
		
		
		TableLayout grid = new TableLayout(10,2);
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(grid);
		add(eastPanel, EAST);
		
		
		
		JLabel userLabel = new JLabel("Parameters");
		eastPanel.add(userLabel);
		
		JLabel userLabel2 = new JLabel("");
		eastPanel.add(userLabel2);
		
		eastPanel.add(new JLabel("NUMBALLS:"));
		numBalls = new IntField(30, 1, 60);
		eastPanel.add(numBalls);
		
		eastPanel.add(new JLabel("MIN SIZE:"));
		minSize = new DoubleField(3, 1, 25);
		eastPanel.add(minSize);
		
		eastPanel.add(new JLabel("MAX SIZE:"));
		maxSize = new DoubleField(5, 1, 25);
		eastPanel.add(maxSize);
		
		eastPanel.add(new JLabel("LOSS MIN:"));
		lossMin = new DoubleField(.6, 0, 1);
		eastPanel.add(lossMin);
		
		eastPanel.add(new JLabel("LOSS MAX:"));
		lossMax = new DoubleField(.8, 0, 1);
		eastPanel.add(lossMax);
		
		eastPanel.add(new JLabel("MIN VEL:"));
		minVel = new DoubleField(12, 1, 200);
		eastPanel.add(minVel);
		
		eastPanel.add(new JLabel("MAX VEL:"));
		maxVel = new DoubleField(30, 1, 200);
		eastPanel.add(maxVel);
		
		eastPanel.add(new JLabel("TH MIN:"));
		thetaMin = new DoubleField(45, 1, 180);
		eastPanel.add(thetaMin);
		
		eastPanel.add(new JLabel("TH MAX:"));
		thetaMax = new DoubleField(110, 1, 180);
		eastPanel.add(thetaMax);
		
		
		this.resize(WIDTH+140, HEIGHT + OFFSET + 50); //simulation size
		
		
		
		trace = new JButton("trace");
		add(trace, BorderLayout.SOUTH);
		
		run = new JButton("run");
		add(run , BorderLayout.NORTH);
		
		stack = new JButton("stack");
		add(stack , BorderLayout.NORTH);
		
		clear = new JButton("clear");
		add(clear , BorderLayout.NORTH);
		
		
		stop = new JButton("stop");
		add(stop , BorderLayout.NORTH);
		
		quit = new JButton("quit");
		add(quit , BorderLayout.NORTH);
		
	
		
		
		addActionListeners();
		
		//ground line
		GLine myLine = new GLine(0,600,1200,600); 
		add(myLine);
		
		
		
		while(true) {
			pause(200);
			if (simEnable) { // Run once, then stop
			doSim();
			simEnable=false;
			}
		}
		
	}
	
	
	/**
	 * actionPerformed method combines functions with user interface
	 */
	
	public void actionPerformed(ActionEvent e) {
			String source  = e.getActionCommand();
		
		
		
			if (source.equals("run")) {    //run button
				if(addTrace !=true) {
					addTraceRun = false;
				}
				
				simEnable = true;
				
			}
			else if (source.equals("stack")) {      //stack button
				doStack();
			}
			else if (source.equals("clear")) {      //clear button removes all but line
				addTrace = false; 
				addTraceRun = false;
				removeAll();
				GLine myLine = new GLine(0,600,1200,600); //ground line
				add(myLine);
				myTree.root = null;
				
				
				
				
			}
			else if (source.equals("stop")) {     //stop button pauses the balls
				aBall.TICK = 0;
				addTraceRun = false;
				
			}
			else if (source.equals("quit")) {    //quit button exits system
				System.exit(0);
			}
			else if (source.contentEquals("trace")) {     //trace button adds the trace
				addTrace = true; 
				addTraceRun = true;
				
				
			}
		
		
	
		
	}
	
	
	
	
	
	/**
	 * doSim method runs the program when the run button is pressed
	 */
	
	
	public void doSim() {
		aBall.TICK = .1;
		nBalls = numBalls.getValue();
		double sizeMin= minSize.getValue();
		double sizeMax = maxSize.getValue();
		double minLoss = lossMin.getValue();
		double maxLoss = lossMax.getValue();
		double velMin = minVel.getValue();
		double velMax = maxVel.getValue();
		double thMin = thetaMin.getValue();
		double thMax = thetaMax.getValue();
		
		rgen.setSeed( (long) 424242 ); //get random pattern(
		
		for(int i=0; i<nBalls; i++) {
			
			double bSize = rgen.nextDouble(sizeMin,sizeMax);
			Color bColor = rgen.nextColor();
			double bLoss = rgen.nextDouble(minLoss,maxLoss);
			double Vo = rgen.nextDouble(velMin,velMax);
			double theta = rgen.nextDouble(thMin,thMax);
			double Xi = 1200/2/SCALE;	
			double Yi =600/SCALE + bSize;
			bSim bLink = this;
			
			
			//add ball
			
			if (addTrace = true) {
				redBall = new aBall(Xi,Yi,Vo,theta,bSize,bColor,bLoss,bLink);
				
			}
			
			else {
				redBall = new aBall(Xi,Yi,Vo,theta,bSize,bColor,bLoss,bLink);
				bLink = null;   //no trace
			}
			
			add(redBall.getBall());    //aBall getter
			
			
			redBall.start();    //begins movement
			
			myTree.addNode(redBall); //creates first node in bTree
			
		}
	}
	
	
	/**
	 * doStack method stacks the balls when stack button is called
	 */
	
	public void doStack() {
		myTree.ballList.clear();
		myTree.inorder(); //generates list
		myTree.stackBalls(); //stacks ball from bTree method
		
	}
}
	


