package DefaultPackage;

import processing.core.*;

class Grid {
	
	static public PVector origin = new PVector(500,350);
	public static PVector dimension = new PVector(600,300);
	static float notchDistance = 5;

	static int tipThickness = 6;
	int tipColor;
	int gridColor1;
	int gridColor2;
	int reachableAreaStrokeColor;
	int reachableAreaFillColor;
	int unreachableAreaFillColor;
	int unreachableAreaStrokeColor;
	int axisColor;
	
	static PGraphics paperSheet;
	
	PGraphics pg;
	PImage back;
	
	Arm arm;
	
	PApplet parent; // The parent PApplet that we will render ourselves onto
	
	Grid(PApplet aParent) {
		
		setParent(aParent);
		
		back = getParent().loadImage("logo_poli.png");
		
		gridColor1 =  getParent().color(200,200,200);
		gridColor2 =  getParent().color(150,150,150);
		tipColor = getParent().color(0,0,0);
		axisColor = getParent().color(25,25,25);
		reachableAreaStrokeColor = getParent().color(200,200,0,200);
		reachableAreaFillColor = getParent().color(255,255,0,50);
		unreachableAreaStrokeColor = getParent().color(200,0,0,200);
		unreachableAreaFillColor = getParent().color(255,0,0,100);
		arm = ((MainApplication)(getParent())).getArm();
		
		paperSheet = getParent().createGraphics((int)(dimension.x), (int)(dimension.y), getParent().JAVA2D);
		cleanPaperSheet();
		
	
	}
	
	void cleanPaperSheet()
	{
		//paperSheet = getParent().createGraphics((int)(dimension.x), (int)(dimension.y), getParent().JAVA2D);
		getPaperSheet().beginDraw();
		getPaperSheet().background(getParent().color(255,255,255,0));
		getPaperSheet().endDraw();
		
	}
	
	public static PGraphics getPaperSheet()
	{
		return paperSheet;
	}
	
	public static void drawPoint(float aX, float aY)
	{
		Grid.getPaperSheet().beginDraw();
		getPaperSheet().fill(0);
		Grid.getPaperSheet().ellipse(aX, aY, tipThickness, tipThickness);
		Grid.getPaperSheet().endDraw();
	}
	
	public static void drawLine(float aX1, float aY1, float aX2, float aY2)
	{
		Grid.getPaperSheet().beginDraw();
		getPaperSheet().fill(0);
		getPaperSheet().strokeWeight(5);
		Grid.getPaperSheet().line(aX1, aY1, aX2, aY2);
		Grid.getPaperSheet().endDraw();
	}
	
	
	
	
	PApplet getParent()
	{
		return parent;
	}
	
	static PVector getArmOrigin()
	{
		return new PVector(origin.x, origin.y);
	}
	
	void setParent(PApplet aParent)
	{
		parent = aParent;
	}
	
	void draw()
	{
		PApplet drawer = getParent();		
		
		int verticalNotchNo =  ((int) dimension.y) / ((int)notchDistance);
		int horizontalNotchNo =  ((int) dimension.x) / ((int)notchDistance);
		
		drawer.stroke(gridColor1);
		drawer.strokeWeight(1);
		
		for (int i = 0; i < verticalNotchNo; i += 1)
		{
			drawer.line(origin.x - dimension.x/2, origin.y - notchDistance * i, origin.x + dimension.x/2, origin.y - notchDistance * i);
		}
		
		for (int i = 0; i < horizontalNotchNo/2; i += 1)
		{
			drawer.line(origin.x - i * notchDistance, origin.y, origin.x - i * notchDistance, origin.y - dimension.y);
			drawer.line(origin.x + i * notchDistance, origin.y, origin.x + i * notchDistance, origin.y - dimension.y);
		}
		
		drawer.stroke(gridColor2);
		
		for (int i = 0; i < verticalNotchNo; i += notchDistance)
		{
			drawer.line(origin.x - dimension.x/2, origin.y - notchDistance * i, origin.x + dimension.x/2, origin.y - notchDistance * i);
		}
		
		for (int i = 0; i < horizontalNotchNo/2; i += notchDistance)
		{
			drawer.line(origin.x - i * notchDistance, origin.y, origin.x - i * notchDistance, origin.y - dimension.y);
			drawer.line(origin.x + i * notchDistance, origin.y, origin.x + i * notchDistance, origin.y - dimension.y);
		}
		drawer.image(back,origin.x - 110,origin.y - dimension.y + 40);
		drawer.stroke(reachableAreaStrokeColor);
		drawer.strokeWeight(2);
		drawer.fill(reachableAreaFillColor);
		
		drawer.arc(origin.x, origin.y, arm.getLength()*2, arm.getLength()*2, PApplet.PI, PApplet.TWO_PI);
		
		drawer.stroke(unreachableAreaStrokeColor);
		drawer.strokeWeight(2);
		drawer.fill(unreachableAreaFillColor);
		
		drawer.arc(origin.x, origin.y, 60, 60, PApplet.PI, PApplet.TWO_PI); 
		
		drawer.stroke(axisColor);
		drawer.noFill();
		drawer.line(origin.x, origin.y, origin.x, origin.y - dimension.y);
		drawer.line(origin.x - dimension.x/2, origin.y, origin.x + dimension.x/2, origin.y);
		
		getPaperSheet().beginDraw();
		getParent().image(getPaperSheet(),origin.x - dimension.x/2, origin.y - dimension.y);
		getPaperSheet().endDraw();
	}
}