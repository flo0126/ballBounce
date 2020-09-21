import java.awt.Color;

import acm.graphics.GOval;

public class aBall extends Thread {
	/**
	 * The constructor specifies the parameters for simulation. They are
	 *
	 * @param Xi double The initial X position of the center of the ball
	 * @param Yi double The initial Y position of the center of the ball
	 * @param Vo double The initial velocity of the ball at launch
	 * @param theta double Launch angle (with the horizontal plane)
	 * @param bSize double The bSize of the ball in simulation units
	 * @param bColor Color The initial color of the ball
	 * @param bLoss double Fraction [0,1] of the energy lost on each bounce
	 */
	
	//Initializing variables
	double Xi; //initial X
	double Yi; //initial Y
	double Vo; //initial velocity
	double theta; //launch angle
	double bSize; //radius
	Color bColor; //color
	double bLoss; //energy loss
	GOval myBall; //create ball
	GOval track;
	GOval pt;
	private bSim link;
	
	double currentSize; //returns ball size
	volatile boolean stillGoing = true; //if ball is moving or not
	
	
	
	
	
	private static final double Pi = 3.141592654;
	private static final double g = 9.8;
	public static double TICK = 0.1; // Clock tick duration (sec)
	private static final double ETHR = 0.01; // If either Vx or Vy < ETHR STOP
	private static final double k = 0.0001;
	private static final int WIDTH = 1200; // n.b. screen coordinates
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;
	private static final double SCALE = HEIGHT/100; // pixels per meter
	private static final int pointDiameter = 1;
	double ScrbSize = bSize * SCALE; //to set size of ball in pixels
	double ScrX;
	double ScrY;
	double ScrXlastBounce;
	
	
	/** constructor method for aBall
	 * 
	 * @param Xi initial x
	 * @param Yi initial y
	 * @param Vo initial velocity
	 * @param theta angle launched
	 * @param bSize radius
	 * @param bColor color
	 * @param bLoss energy loss
	 */
	
	public aBall(double Xi, double Yi, double Vo, double theta,double bSize, Color bColor, double bLoss) {
		this.Xi = Xi; // Get simulation parameters
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		myBall = new GOval(Xi*SCALE,HEIGHT-(Yi+bSize)*SCALE, 2*bSize*SCALE, 2*bSize*SCALE);
		myBall.setFilled(true);
		myBall.setFillColor(bColor);
		
		
	}
	
	/** constructor method for aBall
	 * 
	 * @param Xi initial x
	 * @param Yi initial y
	 * @param Vo initial velocity
	 * @param theta angle launched
	 * @param bSize radius
	 * @param bColor color
	 * @param bLoss energy loss
	 * @param link link to bSim
	 */
	
	public aBall(double Xi, double Yi, double Vo, double theta, double bSize, Color bColor, double bLoss, bSim link) {
		this.Xi = Xi; // Get simulation parameters
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		this.link = link;
		
		myBall = new GOval(Xi*SCALE,HEIGHT-(Yi+bSize)*SCALE, 2*bSize*SCALE, 2*bSize*SCALE);
		myBall.setFilled(true);
		myBall.setFillColor(bColor);
	}
	
	/**
	 * getBall method returns aBall for bSim
	 * @return the ball
	 */
	public GOval getBall() {
		return myBall;
		
	}
	
	
	
	
	/**
	 * this method sends radius to bTree
	 * @return bSize
	 */
	public double getCurrentSize() {
		return this.bSize;
	}
	/**
	 * this method will move ball when stacked
	 * @param x is x coordinate
	 * @param y is y coordinate
	 */
	public void moveTo(double x, double y) {
		myBall.setLocation(x,y);
	}
	
	
	
	/**
	 * the trace method adds the tracks in the same color as the ball
	 * @param x is x location
	 * @param y is y location
	 */
	private void trace(double x, double y) {
		double ScrX = x*SCALE;
		double ScrY = HEIGHT - y*SCALE;  
		pt = new GOval(ScrX,ScrY,pointDiameter,pointDiameter);  
		pt.setColor(bColor);
		pt.setFilled(true);
		link.add(pt);  }
	
	
	/**
	* The run method implements the simulation loop from Assignment 1.
	* Once the start method is called on the aBall instance, the
	* code in the run method is executed concurrently with the main
	* program.
	* @param void
	* @return void
	*/
	public void run() {
		
		
		
		
		
		
		//Initializing variables
				double Vt = g / (4*Pi*bSize*bSize*k); // Terminal velocity
				double Vox=Vo*Math.cos(theta*Pi/180); // X component of initial velocity
				double Voy=Vo*Math.sin(theta*Pi/180); // Y component of initial velocity
				double time = 0; 
				double X = Xi; //X position
				double Y = Yi; //Y position
				double Vx = Vox; //X component of V
				double Vy = Voy; //Y component of V
				double Xlast = X; //previous X location
				double Ylast = Y; //previous Y location
				ScrX = (X-bSize) *SCALE; //screen pixel X
				ScrY = HEIGHT - (Y+bSize) *SCALE; //screen pixel Y
				double KEx = .5 * Vx * Vx; //horizontal kinetic energy
				double KEy = .5 * Vy * Vy; //vertical kinetic energy
				double XlastBounce = Xi; //to calculate X of last bounce
				ScrXlastBounce = Xi * SCALE;
				double KElastBounce = KEx+KEy+1;
				
				
				//movement
				while ((KEx + KEy > ETHR )&&(KEy + KEx < KElastBounce)) {
					stillGoing = true;
					Xlast = X; //find change in x
					Ylast = Y; //find change in y
					X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)); // X position
					Y = bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; // Y position
					Vx = (X-Xlast)/TICK; //horizontal velocity
					Vy = (Y-Ylast)/TICK; //veritcal velocity
					
					if (Vy<0 && Y<bSize) { //to create bounce
						time = 0; //reset time
						KElastBounce = KEx+KEy;
						XlastBounce += X; //measured from X position of last bounce
						KEx = 0.5*Vx*Vx*(1-bLoss); // Kinetic energy in X direction after collision
						KEy = 0.5*Vy*Vy*(1-bLoss); // Kinetic energy in Y direction after collision
			
						if (Vox>=0) {
							Vox = Math.sqrt(2*KEx);} 
						else  Vox = -Math.sqrt(2*KEx); 
						
						Voy = Math.sqrt(2*KEy); // Resulting vertical velocity
						X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)); // X position
						Y = bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; // Y position
					}
					
					//convert from meters to screen pixels
					ScrX = (int) ((X-bSize)*SCALE);
					ScrY = (int) (HEIGHT-(Y+bSize)*SCALE);
					ScrXlastBounce = (int)(XlastBounce)*SCALE;
					//move ball
					myBall.setLocation(ScrX+ScrbSize+ScrXlastBounce,Math.min(ScrY, HEIGHT - (bSize*2.0))); // Screen units
					
					
					if(bSim.addTrace != true){
						link = null;
						
					}
						
					if(bSim.addTraceRun != false) {
						
						trace(XlastBounce+X,Y);
						
					}
					
					try { // pause for 50 milliseconds
						Thread.sleep(50);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
					time+=TICK;//creates continuous movement
					
					
					
					if ((KEx + KEy <= ETHR )&&(KEy + KEy >= KElastBounce)) {
						
						break; //stops ball when KE falls below threshold
					}
				
				}
				stillGoing = false; //lets isRunning method know when ball stops bouncing
				
	}
	}
