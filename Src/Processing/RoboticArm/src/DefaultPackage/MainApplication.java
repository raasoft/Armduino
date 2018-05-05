package DefaultPackage;

import processing.core.*;
import controlP5.*;
import java.util.*;
import processing.serial.*;
import gnu.io.*;
//TODO: Implementare il pulitore della point queue
//TODO: Implementare la point queue con ricordo del tasto draw'
//TODO: Automode -> Disegnare un fiore


public class MainApplication extends PApplet
{
	ControlP5 controlP5;
	
	SerialCommunication serial;
	
	public CheckBox chOnline; 
	
	Textlabel myTextlabelA;
	Textarea plotterQueueDump;
	ArrayList<String> listBoxPointQueueNames;
	
	int listBoxPointQueueNo;
	int listBoxPointQueueFirst;
	
	PFont font;
	Arm arm;
	Grid grid;
	
	RadioButton modeSelector;
	
	static public int colorSliderFore;
	static public int colorSliderBack;
	static public int colorSliderActive;
	
	static PVector dimension = new PVector(950,370);
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "DefaultPackage.MainApplication" });
	}
	
	public ControlP5 getP5Controller()
	{
		return controlP5;
	}
	
	public Arm getArm()
	{
		return arm;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public void setup()
	{
		
		serial = new SerialCommunication(this);
		
		size((int)(dimension.x), (int)(dimension.y));
		  
		smooth();

		arm = new Arm(this);
		grid = new Grid(this);

		controlP5 = new ControlP5(this);

		listBoxPointQueueNo = 0;
		listBoxPointQueueFirst = 0;

		colorSliderBack = color(100,100,100);
		colorSliderFore = color(150,150,150);
		colorSliderActive  = color(200,200,200);

		int yOrigin = 10;
		int xOrigin = 10;

		int i = 0;

		int sliderHeight = 11;
		int sliderWidth = 100;
		int vSpacing = 6;

		myTextlabelA = controlP5.addTextlabel("SplashTitle","Arm Controller GUI v. 0.45", xOrigin, yOrigin); i++;
		myTextlabelA.setFont(ControlP5.standard56);
		myTextlabelA.setColorValueLabel(color(0,0,0));

		i++;

		Slider slider = null;

		Textlabel label = null;

		label = controlP5.addTextlabel("Joint0Label","Joint 0", xOrigin,  yOrigin + i * (sliderHeight + vSpacing)); i++;
		label.setFont(ControlP5.standard56);
		label.setColorValueLabel(color(0,0,0));  

		slider = controlP5.addSlider("sliderJoint1BaseX",-Grid.dimension.x/2,Grid.dimension.x/2,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("base x");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1BaseY",0,Grid.dimension.y,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("base y");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1TipX",-Grid.dimension.x/2,Grid.dimension.x/2,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("tip x");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1TipY",0,Grid.dimension.y,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("tip y");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1Theta",0,180,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("rel. direction");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1ThetaAim",0,180,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("aim direction");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint1ThetaStepAim",0,638,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("aim direction step");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		label = controlP5.addTextlabel("Joint1Label","Joint 1", xOrigin,  yOrigin + i * (sliderHeight + vSpacing)); i++;
		label.setFont(ControlP5.standard56);
		label.setColorValueLabel(color(0,0,0));  
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);

		slider = controlP5.addSlider("sliderJoint2BaseX",-Grid.dimension.x/2,Grid.dimension.x/2,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("base x");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint2BaseY",0,Grid.dimension.y,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("base y");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);

		slider = controlP5.addSlider("sliderJoint2TipX",-Grid.dimension.x/2,Grid.dimension.x/2,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("tip x");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint2TipY",0,Grid.dimension.y,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("tip y");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint2Theta",0,360,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("rel. direction");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint2ThetaAim",0,360,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("aim direction");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderJoint2ThetaStepAim",0,638,0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("aim direction step");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);

		label = controlP5.addTextlabel("PlotterLabel","Plotter", xOrigin,  yOrigin + i * (sliderHeight + vSpacing)); i++;
		label.setFont(ControlP5.standard56);
		label.setColorValueLabel(color(0,0,0));  
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);

		slider = controlP5.addSlider("sliderPointsQueue",0,(int)(Arm.maxPointsToPlot), 0, xOrigin, yOrigin + i * (sliderHeight + vSpacing),sliderWidth,sliderHeight); i++;
		slider.setLabel("Points in Queue");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		i+=2;

		//
		//		  

		slider = controlP5.addSlider("sliderMouseX",-Grid.dimension.x/2,+Grid.dimension.x/2,0,(int)(Grid.origin.x + 5 - Grid.dimension.x/2),(int)(Grid.origin.y + 5  - Grid.dimension.y),sliderWidth,sliderHeight);
		slider.setLabel("Target X");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		slider = controlP5.addSlider("sliderMouseY",0,Grid.dimension.y,0,(int)(Grid.origin.x + 5 - Grid.dimension.x/2),(int)(Grid.origin.y + 20  - Grid.dimension.y),sliderWidth,sliderHeight);
		slider.setLabel("Target Y");
		slider.setColorCaptionLabel(color(0,0,0));
		slider.setColorForeground(colorSliderFore);
		slider.setColorBackground(colorSliderBack);
		slider.setColorActive(colorSliderFore);

		modeSelector = controlP5.addRadioButton("radioButtonMode",(int)(Grid.origin.x + Grid.dimension.x/6),(int)(Grid.origin.y + 5  - Grid.dimension.y));
		modeSelector.setColorForeground(colorSliderBack);
		modeSelector.setColorBackground(colorSliderBack);
		modeSelector.setColorActive(colorSliderFore);
		modeSelector.setColorLabel(color(255));
		modeSelector.setItemsPerRow(5);
		modeSelector.setSpacingColumn(50);

		addToRadioButton(modeSelector,"Setup",1);
		addToRadioButton(modeSelector,"Track",2);
		addToRadioButton(modeSelector,"Auto",3);

		modeSelector.deactivateAll();
		modeSelector.activate("Setup");
		modeSelector.setNoneSelectedAllowed(false);

		Controller button = controlP5.addButton("cleanPaperSheet",1, (int)(Grid.origin.x + Grid.dimension.x/4 + 10),(int)(Grid.origin.y + 20 - Grid.dimension.y),60,12);
		button.setLabel("Clean");
		button.setColorActive(colorSliderActive);
		button.setColorForeground(colorSliderFore);
		button.setColorBackground(colorSliderBack);

		ListBox l = controlP5.addListBox("myList",(int)(dimension.x - 130 - xOrigin), yOrigin + 50,120,10);
		l.setItemHeight(15);
		l.setBarHeight(15);
		l.captionLabel().toUpperCase(true);
		l.captionLabel().set("plotter queue");
		l.captionLabel().style().marginTop = 3;
		l.valueLabel().style().marginTop = 3;
		l.actAsPulldownMenu(false);
		l.setColorForeground(colorSliderFore);
		l.setColorBackground(colorSliderBack);

		plotterQueueDump = controlP5.addTextarea("plotterQueueDump","",
				(int)(dimension.x - 130 - xOrigin), yOrigin + 50,120,270);
		plotterQueueDump.scroll(1);
		plotterQueueDump.setColorLabel(color(0));
		plotterQueueDump.setColorActive(colorSliderActive);
		plotterQueueDump.setColorForeground(colorSliderFore);
		plotterQueueDump.setColorBackground(colorSliderBack);
		
		chOnline = controlP5.addCheckBox("checkBox",(int)(dimension.x - 130 - xOrigin), yOrigin + 50+280);
		chOnline.setColorForeground(colorSliderBack);
		chOnline.setColorBackground(colorSliderBack);
		chOnline.setColorActive(colorSliderFore);
		chOnline.setColorLabel(color(0));
		chOnline.setItemsPerRow(3);
		chOnline.setSpacingColumn(30);
		chOnline.setSpacingRow(10);
		Toggle opt = chOnline.addItem("Online",0);
		opt.setState(true);
		
		
	}
	
	RadioButton getModeSelector()
	{
		return modeSelector;
	}

	void addToRadioButton(RadioButton theRadioButton, String theName, int theValue ) 
	{
		Toggle t = theRadioButton.addItem(theName,theValue);
		t.captionLabel().setColorBackground(color(80));
		t.captionLabel().style().movePadding(2,0,-1,2);
		t.captionLabel().style().moveMargin(-2,0,0,-3);
		t.captionLabel().style().backgroundWidth = 46;
	}

	public void draw()
	{

		fill(250);
		rect(0,0, width, height);

		arm.update();
				
		grid.draw();
		arm.draw();
		
		serial.receiveSerial();
	}
	
	public void cleanPaperSheet(int theValue) {

			grid.cleanPaperSheet();

		}
	
	Textarea getPlotterQueueDump()
	{
		return plotterQueueDump;
	}

}

class SerialCommunication
{
	public enum Mode 
	{
	    SETUP, TRACKING, AUTOMATIC 
	}
	
	Serial myPort;
	boolean armOnline;
	
	static final int BAUD_RATE     =  19200; 
    static final int SERIAL_DELAY  =  5; 
        
	int message;
	final static int JOINT_ANGLE_LENGTH = 10;
	final static int JOINT_CONTROL_LENGTH = 2;
	final static int TIP_LENGTH = 1;
	final static int MODE_SHIFT = 25;
	final static int MODE_LENGTH = 3;
	
	MainApplication main;
	
	Textarea serialInputMessage;
	
	SerialCommunication(MainApplication aApp)
	{
		main = aApp;
		
		myPort = new Serial(main, Serial.list()[1], BAUD_RATE);
		//System.out.println(Serial.list()[0]);
		System.out.println("Connection Established");
	}
	
	void sendStatus(int aAngle0, int aControl0, int aAngle1, int aControl1, boolean aTip, int aMode)
	{
		if (armIsOnline() == false)
			return;
		
		int tipStatus = 0;
		int rotSense0 = 0;
		int rotSense1 = 0;
		
		if (aTip == true)
			tipStatus = 1;
		
		if (aControl0 == 1)
			rotSense0 = 2;
		if (aControl0 == -1)
			rotSense0 = 1;
		
		if (aControl1 == 1)
			rotSense1 = 2;
		if (aControl1 == -1)
			rotSense1 = 1;
		
		/*System.out.println("ANGLE0: " + aAngle0);
		System.out.println("CONTROL0: " + aControl0);
		System.out.println("ANGLE1: " + aAngle1);
		System.out.println("CONTROL1: " + aControl1);
		System.out.println("TIP: " + aTip);
		System.out.println("MODE: " + aMode);
		*/
		int j0a;
		int j1a;
		int j0c;
		int j1c;
		int tip;
		int m;

		int shift = 0;
		j0a = (aAngle0 << shift) & 0x000003FF;
		//System.out.println("j0a: " + main.binary(j0a,32));
		
		shift += JOINT_ANGLE_LENGTH;
		j0c = (rotSense0 << shift) &  0x00000C00;
		//System.out.println("j0c: " + main.binary(j0c,32));
		
		shift += JOINT_CONTROL_LENGTH;
		j1a = (aAngle1 << shift) & 0x003FF000;
		//System.out.println("j1a: " + main.binary(j1a,32));
		
		shift += JOINT_ANGLE_LENGTH;
		j1c = (rotSense1 << shift) &  0x00C00000;
		//System.out.println("j1c: " + main.binary(j1c,32));
		
		shift += JOINT_CONTROL_LENGTH;
		tip = (tipStatus << shift) & 0x01000000;
		//System.out.println("tip: " + main.binary(tip,32));
		
		shift += TIP_LENGTH;
		m = (aMode << shift) & 0x0E000000;
		//System.out.println("mod: " + main.binary(m,32));
		
		message = j0a | j0c | j1a | j1c | tip | m;
		System.out.println("Sent: " + main.binary(message,32));
		
		for (shift = 24; shift >= 0; shift -= 8)
		{
			byte sms = (byte)((message >> shift) & 0x00FF);
		    myPort.write(sms);
		    //System.out.println("SMS "+shift+": m->"+main.binary(sms));
		    main.delay(SERIAL_DELAY);	
		}
		
		//System.out.println("Message Sent");

	}
	
	void receiveSerial()
	{
		//System.out.println("Preparing receiving");
		while (myPort.available() > 0) 
		{
			//System.out.println("Receiving...");
			String inBuffer = myPort.readString();   
			if (inBuffer != null) 
			{
				System.out.print(inBuffer);
			}
			//System.out.println("End of reception");
			//main.delay(SERIAL_DELAY);
		}
	}
	

	public boolean armIsOnline()
	{
		return armOnline;
	}
	
	public void setArmIsOnline(boolean aValue)
	{
		armOnline = aValue;
	}

}




