package DefaultPackage;

import processing.core.*;
import Controllers.*;
import controlP5.*;
import java.util.*;

public class Arm {
	
	PVector armOrigin;
	PVector armTarget;
	PVector armOldTarget;

	Joint[] joint;
	
	ArrayList<PVector> pointsToPlot; 
	
	boolean armTipDown;
	boolean armTargetReachable;
	
	public enum Mode 
	{
	    SETUP, TRACKING, AUTOMATIC 
	}
	Mode armMode;
	
	MainApplication parent; // The parent PApplet that we will render ourselves onto
	
	int armColor;
	int targetColor;
	int armColorNodeArea;
	int armColorNodeStroke;
	int armColorAnglesStroke;
	int armColorAnglesArea;
	int armJointNo;
	int armColorTip1;
	int armColorTip2;
	
	static final int maxPointsToPlot = 100;
	
	Arm(MainApplication aParent) {
		
		setParent(aParent);
		
		armMode = Mode.TRACKING;
		
		setArmOrigin(Grid.getArmOrigin());
		armColor = getParent().color(10,10,10);
		targetColor = getParent().color(200,0,0);
		armColorNodeArea = getParent().color(200,0,0);
		armColorNodeStroke = getParent().color(150,0,0);
		armColorAnglesStroke = getParent().color(255,150,0);
		armColorAnglesArea = getParent().color(255,150,0,100);
		armColorTip1 = getParent().color(0,0,255);
		armColorTip2 = getParent().color(255,255,0);
		
		pointsToPlot = new ArrayList();
		
		float jointDefaultLength = 100;
		int jointDefaultSteps = 638;
		armJointNo = 2;
						
		joint = new Joint[armJointNo];
		Joint p = null;
		int i = 0;
		
		joint[0] = new Joint(p, 155	, jointDefaultSteps, 1);
		p = joint[i];
		joint[1] = new Joint(p, 125, jointDefaultSteps, 1);
		p = joint[i];
		
		joint[0].setPosition(new PVector(0,0));
		
		setOldTarget(new PVector(0,0));
		setTarget(new PVector(0,0));

	}
	
	ArrayList<PVector> getPointsToPlot()
	{
		return pointsToPlot;
	}
	
	void addPointToPlot(PVector aVector)
	{
		getPointsToPlot().add(aVector);
		//((MainApplication)getParent()).addPointToQueue(aVector);
		
	}
	
	PVector getFirstPointToPlot()
	{
		
		if (getPointsToPlot().size() > 0)
			return getPointsToPlot().get(0);
		else
			return null;
		
	}
	
	int getPointToPlotNumber()
	{
		return getPointsToPlot().size();		
	}
	
	PVector getLastPointToPlot()
	{
		
		PVector vect;
		if (getPointsToPlot().size() > 0)
			vect = getPointsToPlot().get(getPointsToPlot().size()-1);
		else
			vect = null;
		
		return vect;
		
	}
	
	void removeFirstPointToPlot()
	{
		if (getPointsToPlot().size() > 0)
		{
			getPointsToPlot().remove(0);
		}
		
	}
	
	void cleanPointsToPlot()
	{
		while (getPointsToPlot().size() > 0)
		{
			removeFirstPointToPlot();
		}
		
	}
	
	PVector getTarget()
	{
		if (armTarget == null)
			return null;
		
		return new PVector(armTarget.x, armTarget.y);
	}
	
	void setTarget(PVector aVector)
	{
		if (aVector == null)
		{
			armTarget = null;
			return;
		}
		
		if (armTarget == null)
		{
			armTarget = new PVector(aVector.x, aVector.y);
		}
		else
		{
			armTarget.x = aVector.x;
			armTarget.y = aVector.y;
		}
	}
	
	
	PVector getOldTarget()
	{
		if (armOldTarget == null)
			return null;
		
		return new PVector(armOldTarget.x, armOldTarget.y);
	}
	
	void setOldTarget(PVector aVector)
	{
		if (aVector == null)
		{
			armOldTarget = null;
			return;
		}
		
		if (armOldTarget == null)
		{
			armOldTarget = new PVector(aVector.x, aVector.y);
		}
		else
		{
			armOldTarget.x = aVector.x;
			armOldTarget.y = aVector.y;
		}
	}
	
	void setArmOrigin(PVector aVector)
	{
		if (armOrigin == null)
		{
			armOrigin = new PVector(aVector.x, aVector.y);
		}
		else
		{
			armOrigin.x = aVector.x;
			armOrigin.y = aVector.y;
		}
	}
	
	PVector getArmOrigin()
	{
		return new PVector(armOrigin.x, armOrigin.y);
	}
	
	MainApplication getParent()
	{
		return parent;
	}
	
	void setParent(MainApplication aParent)
	{
		parent = aParent;
	}
	
	boolean getTipStatus() //1 Down 0 Up
	{
		return armTipDown;
	}
	
	void setTargetReachable(boolean aValue)
	{
		armTargetReachable = aValue;
	}
	
	boolean getTargetReachable() //1 Yes 0 No
	{
		return armTargetReachable;
	}
	
	void setArmMode(Mode aMode)
	{
		armMode = aMode;
		int mode = 0;
		if (armMode == Mode.TRACKING)
		mode = 1;
		if (armMode == Mode.AUTOMATIC)
		mode = 2;
		
		getParent().serial.sendStatus(0, 3, 0, 3, getTipStatus(), mode);
	}
	
	Mode getArmMode()
	{
		return armMode;
	}
	
	void setTipStatus(boolean aIsDown)
	{
		armTipDown = aIsDown;
		if (aIsDown == true)
		{
			//getParent().writeSerial('H');
		}
	}
	
	float getLength()
	{
		float armLength = 0;
		for (Joint j : joint)
			armLength += j.getLength();

		return armLength;
	}
	
	PVector getEndEffectorPosition()
	{
		Joint endEffector = joint[armJointNo-1];
		return (endEffector.getTipPosition());
	}
	
	float getEndEffectorTargetDistance(PVector aTarget)
	{
		Joint endEffector = joint[armJointNo-1];
		return (endEffector.getTipTargetDistance(aTarget));
	}
	
	void update()
	{
		float arrayOptions[] = ((MainApplication)(getParent())).getModeSelector().arrayValue();
		
		
		getParent().serial.setArmIsOnline(getParent().chOnline.getState(0));
		
		if (arrayOptions[0] == 1 && getArmMode() != Mode.SETUP)
		{
			setArmMode(Mode.SETUP);
			cleanPointsToPlot();
		}
		else if (arrayOptions[1] == 1 && getArmMode() != Mode.TRACKING)
		{
			setArmMode(Mode.TRACKING);
			cleanPointsToPlot();
		}
		else if (arrayOptions[2] == 1 && getArmMode() != Mode.AUTOMATIC)
		{
			cleanPointsToPlot();
			setArmMode(Mode.AUTOMATIC);
			
			
			float radius = 100;
			float theta = 0;
			int x = 150;
			int y = 210;
			float tt = PApplet.abs(PApplet.tan(theta));
			radius = 150 * PApplet.pow(PApplet.abs(tt),1/tt);
			float xx = radius * PApplet.cos(theta);
			float yy = radius * PApplet.sin(theta);
			
			addPointToPlot(new PVector(x + xx, y - yy));
			
			float acc = 0.1f;
			for (float i = acc; i < 180; i+=acc)
			{
				
				/*
				if (i < 55)
				{
					acc = 1;
				}
				if ( i > 55 && i < 175)
				{
					acc = 1;
				}
				if ( i > 175)
				{
					acc = 10;
				}
				
				
				*/
				theta = i/180*PApplet.PI;
				
				float dist = 6;
				
				if (i > 65 && i < 85)
				{
					dist = 1.5f;
				}
				if (i > 85 && i < 120)
				{
					dist = 3;
				}
				
				if (i > 120 && i < 160)
				{
					dist = 1.5f;
				}
				
				if (i > 160)
				{
					dist = 0.25f;
				}
			
				//CUORE
				tt = PApplet.abs(PApplet.tan(theta));
				radius = 150 * PApplet.pow(PApplet.abs(tt),1/tt);
				xx = radius * PApplet.cos(theta);
				yy = radius * PApplet.sin(theta);
				
				PVector target = getLastPointToPlot();	
				if (getParent().dist(target.x, target.y, x + xx, y - yy) > dist)
					addPointToPlot(new PVector(x + xx, y - yy));
				
			}
			
			theta = 0;
			tt = PApplet.abs(PApplet.tan(theta));
			radius = 150 * PApplet.pow(PApplet.abs(tt),1/tt);
			xx = radius * PApplet.cos(theta);
			yy = radius * PApplet.sin(theta);
			
			addPointToPlot(new PVector(x + xx, y - yy));
			addPointToPlot(new PVector(-150, 150));
	
		}
		
		float[] theta = new float[2];
		theta[0] = joint[0].getDirectionToAim();
		theta[1] = joint[1].getDirectionToAim();
		
		int rotationSense = 0;
		int sign = 1;
		
		setTargetReachable(false);
		setTipStatus(false);
		
		setOldTarget(getTarget());
		
		if (getArmMode() == Mode.SETUP)
		{
			theta[0] = 0;
			theta[1] = 0;
			
			for (int i = 0; i < armJointNo; i++)
			{
				if (i != 0)
					joint[i].setAimDirection( theta[i], rotationSense );
				else
					joint[i].setAimDirection( theta[i], 0);
			}
			
		}
		else
		{
			if (getArmMode() == Mode.TRACKING) 
			{
				//Set up the target vector
				if (getParent().keyPressed && (getParent().key  == 'w'))
				{
					getParent().keyPressed = false;
					float x = getParent().mouseX - Grid.origin.x;
					float y = -getParent().mouseY + Grid.origin.y;
					
					if (x > Grid.dimension.x/2)
						x = Grid.dimension.x/2;
					if (x < -Grid.dimension.x/2)
						x = -Grid.dimension.x/2;
					
					if (y > Grid.dimension.y)
						y = Grid.dimension.y;
					if (y < 0)
						y = 0;
					
					PVector target = new PVector(x,y);
					
					float maxRadius = getLength() * 0.999f;
					float minRadius = PApplet.abs(joint[1].getLength() - joint[0].getLength());
					
					if (target.y >= 0 && (target.mag() <=  maxRadius) && (target.mag() > minRadius )) //If the target is in the reachable area then try to reach it
					{
						setTargetReachable(true);
					}
					
					if (getTargetReachable() == true)
					{
						if (getPointsToPlot().size() == 0)
						{
							addPointToPlot(target);
						}
						else
						{
							if (getLastPointToPlot().dist(target) > 5 && getPointToPlotNumber() < maxPointsToPlot)
							{
								addPointToPlot(target);
							}
						}
					}
					
				}
			}
			
			step();
		}
	
		for (int i = 0; i < armJointNo; i++)
		{
			joint[i].updatePosition();
			joint[i].updateRotation();
		}
		
		setTipStatus(false);
		
		//Set up the tip status: 1 down, 0 up
		if ((getArmMode() == Mode.AUTOMATIC) || 
		(getParent().mousePressed && (getParent().mouseButton == PApplet.RIGHT)))
		{
			float rotationalDistance = PApplet.abs(joint[0].getDirectionOffset()) + PApplet.abs(joint[1].getDirectionOffset());
			
			if ((getTarget() != null) && (rotationalDistance < 3 * PApplet.PI / 180))
				setTipStatus(true);
		}
	}

	
	void step()
	{
		float[] theta = new float[2];
		theta[0] = joint[0].getDirectionToAim();
		theta[1] = joint[1].getDirectionToAim();
		
		int rotationSense = 0;
		int sign = 1;
		
		int mode = 1;
		if (getArmMode() == Mode.AUTOMATIC)
			mode =2;
		
		if (getPointsToPlot().size() > 0)
		{
			
			if (joint[0].getIsMoving() == false && joint[1].getIsMoving() == false) //If the target is in the reachable area then try to reach it
			{
				//System.out.print("****************************UNPUNTO!*******************************");
				PVector target = getFirstPointToPlot();	
				setTarget(target);
				removeFirstPointToPlot();
				if (getArmMode() == Mode.AUTOMATIC)
				{
					getParent().delay(200);
				}
			
				float l1 = joint[0].getLength();
				float l2 = joint[1].getLength();
				
				double a = 2 * l1 * target.x + PApplet.sq(target.x) + 
				PApplet.sq(target.y) + PApplet.sq(l1) - PApplet.sq(l2); 
				double b = -4 * l1 * target.y; 
				double c = PApplet.sq(target.x) + PApplet.sq(target.y) + 
				PApplet.sq(l1) - PApplet.sq(l2) - 2 * l1 * target.x; 
				
				
				if (target.x <= 0) 		sign = -1;
				else if (target.x > 0)	sign = 1;
				
				/*float jointsOffset = joint[1].getTipPosition().x - joint[0].getPosition().x;
				
				if ( ( jointsOffset / PApplet.abs(jointsOffset) * sign) == -1)
				{
					rotationSense = -sign;
					System.out.println("Forcing rotation sense to " + rotationSense);
				}*/ //FORCING NON ATTIVO
				
				theta[0] = 2*PApplet.atan((float) ((-b + sign *PApplet.sqrt((float)(b*b-4*a*c)))/(2*a)));
				theta[1] = -sign * PApplet.acos( (PApplet.sq(target.x) + 
							PApplet.sq(target.y) - 
							PApplet.sq(l1) - 
							PApplet.sq(l2)) 
							/(2*l1*l2)); 
				
				for (int i = 0; i < armJointNo; i++)
				{
					if (i != 0)
						joint[i].setAimDirection( theta[i], rotationSense );
					else
						joint[i].setAimDirection( theta[i], 0);
				}	
				
				
				int deltaSteps1 = -joint[1].getDirectionStepToAim() + joint[1].getDirectionStep();
				
				if (deltaSteps1 > joint[1].getStepNumber()/2) deltaSteps1 -= joint[1].getStepNumber();
				if (deltaSteps1 < -joint[1].getStepNumber()/2) deltaSteps1 += joint[1].getStepNumber();
				
				deltaSteps1 = getParent().abs(deltaSteps1);
				
				getParent().serial.sendStatus(
						getParent().abs(joint[0].getDirectionStepToAim() - joint[0].getDirectionStep()), 
						joint[0].getRotationSense(), 
						deltaSteps1, 
						joint[1].getRotationSense(), 
						getTipStatus(), mode);
			}
		}
		else
		{
			setTarget(null);
			if (getOldTarget() != getTarget())
			{
				getParent().serial.sendStatus(
						0,3,0,3, 
						getTipStatus(), mode);
			}
		}	
	}
	
	void draw()
	{
		PApplet drawer = getParent();
		
		float addTheta1 = 0;
		float addTheta2 = 0;
		
		float arcLength = 17;
		
		drawer.strokeWeight(8);
		drawer.stroke(targetColor);
		
		if (getTarget() != null)
		{
			drawer.line(getTarget().x - 10 + Grid.origin.x, -getTarget().y - 10 + Grid.origin.y,
					getTarget().x + 10 + Grid.origin.x, -getTarget().y + 10 + Grid.origin.y);
			
			drawer.line(getTarget().x + 10 + Grid.origin.x, -getTarget().y - 10 + Grid.origin.y,
					getTarget().x - 10 + Grid.origin.x, -getTarget().y + 10 + Grid.origin.y);
		}
		for (int i = 0; i < armJointNo; i++)
		{
					
			drawer.noFill();
			drawer.strokeWeight(20);
			drawer.stroke(armColor);
			
			addTheta1 = 0;
			addTheta2 = joint[i].getDirection();
			
			drawer.line(joint[i].getPosition().x + Grid.origin.x, -joint[i].getPosition().y + Grid.origin.y, 
					joint[i].getTipPosition().x + Grid.origin.x, -joint[i].getTipPosition().y + Grid.origin.y );
			
			drawer.stroke(armColorAnglesStroke);
			drawer.strokeWeight(1);
			drawer.fill(armColorAnglesArea);
			
			if ( i != 0 )
				addTheta1 = joint[i-1].getDirection();

			drawer.line(joint[i].getPosition().x + Grid.origin.x, 
						-joint[i].getPosition().y + Grid.origin.y, 
						joint[i].getPosition().x + PApplet.cos(addTheta1) * 50 + Grid.origin.x, 
						-joint[i].getPosition().y - PApplet.sin(addTheta1) * 50 + Grid.origin.y);
			
			if ( i != 0 )
				addTheta2 += joint[i-1].getDirection();
			
			drawer.line(joint[i].getPosition().x + Grid.origin.x, 
						-joint[i].getPosition().y + Grid.origin.y, 
						joint[i].getPosition().x + PApplet.cos(addTheta2) * 50 + Grid.origin.x, 
						-joint[i].getPosition().y - PApplet.sin(addTheta2)* 50 + Grid.origin.y);
			
			drawer.arc(joint[i].getPosition().x + Grid.origin.x, 
						-joint[i].getPosition().y + Grid.origin.y, arcLength, arcLength,
						PApplet.TWO_PI- addTheta2, PApplet.TWO_PI - addTheta1);
				
			drawer.stroke(armColorNodeStroke);
			drawer.strokeWeight(1);
			drawer.fill(armColorNodeArea);
			drawer.ellipse(joint[i].getPosition().x + Grid.origin.x, 
						-joint[i].getPosition().y + Grid.origin.y, 8, 8);
		}
		
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1BaseX").setValue(
				joint[0].getPosition().x);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1BaseY").setValue(
				joint[0].getPosition().y);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1TipX").setValue(
				joint[0].getTipPosition().x);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1TipY").setValue(
				joint[0].getTipPosition().y);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1Theta").setValue(
				joint[0].getDirection() * 360 / PApplet.TWO_PI);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1ThetaAim").setValue(
				joint[0].getDirectionToAim() * 360 / PApplet.TWO_PI);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint1ThetaStepAim").setValue(
				joint[0].getDirectionStepToAim());
		
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2BaseX").setValue(
				joint[1].getPosition().x );
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2BaseY").setValue(
				joint[1].getPosition().y );
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2TipX").setValue(
				joint[1].getTipPosition().x);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2TipY").setValue(
				joint[1].getTipPosition().y);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2Theta").setValue(
				joint[1].getDirection() * 360 / PApplet.TWO_PI);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2ThetaAim").setValue(
				joint[1].getDirectionToAim() * 360 / PApplet.TWO_PI);
		((MainApplication)getParent()).getP5Controller().controller("sliderJoint2ThetaStepAim").setValue(
				joint[1].getDirectionStepToAim());
		
		((MainApplication)getParent()).getP5Controller().controller("sliderPointsQueue").setValue(
				getPointToPlotNumber());
		
		if (getFirstPointToPlot()!= null )
		{
		((MainApplication)getParent()).getP5Controller().controller("sliderMouseX").setValue(
				getFirstPointToPlot().x );
		
		((MainApplication)getParent()).getP5Controller().controller("sliderMouseY").setValue(
				getFirstPointToPlot().y  );
		}
		
		if (getTargetReachable() == true)
		{
			((MainApplication)getParent()).getP5Controller().controller("sliderMouseY").setColorForeground(
					MainApplication.colorSliderFore);
			((MainApplication)getParent()).getP5Controller().controller("sliderMouseX").setColorForeground(
					MainApplication.colorSliderFore);
			
			((MainApplication)getParent()).getP5Controller().controller("sliderMouseY").setColorBackground(
					MainApplication.colorSliderBack);
			((MainApplication)getParent()).getP5Controller().controller("sliderMouseX").setColorBackground(
					MainApplication.colorSliderBack);
		}
		else
		{
			((MainApplication)getParent()).getP5Controller().controller("sliderMouseY").setColorForeground(
					getParent().color(255,0,0));
				((MainApplication)getParent()).getP5Controller().controller("sliderMouseX").setColorForeground(
						getParent().color(255,0,0));
				
				((MainApplication)getParent()).getP5Controller().controller("sliderMouseY").setColorBackground(
						getParent().color(200,0,0));
					((MainApplication)getParent()).getP5Controller().controller("sliderMouseX").setColorBackground(
							getParent().color(200,0,0));
		}
		
		drawer.strokeWeight(1);
		
		if (getTipStatus() == false)
		{
			drawer.stroke(armColorTip1);
			drawer.fill(armColorTip1);
		}
		else
		{
			drawer.stroke(armColorTip2);
			drawer.fill(armColorTip2);
			
			/*Grid.drawPoint(getEndEffectorPosition().x + Grid.dimension.x/2, 
					-getEndEffectorPosition().y + Grid.dimension.y);*/
					
			
			Grid.drawLine(getEndEffectorPosition().x + Grid.dimension.x/2, 
					-getEndEffectorPosition().y + Grid.dimension.y, 
					getTarget().x + Grid.dimension.x/2,
					-getTarget().y + Grid.dimension.y);

		}
		
		drawer.ellipse(getEndEffectorPosition().x + Grid.origin.x, 
				-getEndEffectorPosition().y + Grid.origin.y, 8, 8);
		
		StringBuffer dest = new StringBuffer(0);

		for (int i = 0; i < pointsToPlot.size(); i++)
		{
			dest.append((i+1)+": (" + pointsToPlot.get(i).x + ", "+ pointsToPlot.get(i).y+")\n");
		}
		
		((MainApplication)(getParent())).getPlotterQueueDump().setText(dest.toString());
	}
	
	

}

class Joint
{
	Arm arm;
	Joint parent;
	PVector position;
	float length;
	int directionStep;
	int directionStepToAim;
	int stepNumber;
	int rotationSense;
	float speed;
	float maxSpeed;
	PID pid;
	boolean isMoving;	
	
	Joint(Joint aParent, float aLength, int aSteps, float aMaxSpeed)
	{
		setParent(aParent);
		
		if (aParent != null)
			setPosition(aParent.getPosition());
		else
			setPosition(new PVector(0,0));
		
		setDirectionStep(0);
		setDirectionStepToAim(0);
		setLength(aLength);
		setStepNumber(aSteps);
		setSpeed(aMaxSpeed);
		setIsMoving(false);
		
		pid = new PID(1,0,0);
	}
	
	PID getPid()
	{
		return pid;
	}
	
	boolean getIsMoving()
	{
		return isMoving;
	}
	
	void setIsMoving(boolean aValue)
	{
		isMoving = aValue;
	}
	
	PVector getPosition()
	{
		return new PVector(position.x, position.y);
	}
	
	void setPosition(PVector aPosition)
	{
		if (position == null)
		{
			position = new PVector(aPosition.x, aPosition.y);
		}
		else
		{
			position.x = aPosition.x;
			position.y = aPosition.y;
		}
	}
	
	float getLength()
	{
		return length;
	}
	
	void setLength(float aLength)
	{
		length = aLength;
	}
	
	float getDirection()
	{
		return ((float)(getDirectionStep()) / getStepNumber() * PApplet.TWO_PI);
	}
	
	float getDirectionOffset()
	{
		return getDirectionToAim() - getDirection();
	}
	
	void setDirection(float aDirection)
	{
		setDirectionStep( PApplet.round(aDirection / PApplet.TWO_PI * getStepNumber()));
	}

	void setDirectionStep(int aDirectionStep)
	{
		directionStep = aDirectionStep;
	}
	
	int getDirectionStep()
	{
		return directionStep;
	}
	
	void setDirectionStepToAim(int aDirectionStepToAim)
	{
		directionStepToAim = aDirectionStepToAim;
	}
	
	int getDirectionStepToAim()
	{
		return directionStepToAim;
	}
	
	int getDirectionStepOffset()
	{
		return directionStepToAim - directionStep;
	}
	
	float getDirectionToAim()
	{
		return ((float)(getDirectionStepToAim()) / getStepNumber() * PApplet.TWO_PI);
	}
	
	void setDirectionToAim(float aDirectionToAim)
	{
		setDirectionStepToAim(PApplet.round(aDirectionToAim / PApplet.TWO_PI * getStepNumber()));
	}
	
	float getSpeed()
	{
		return speed;
	}
	
	void setSpeed(float aSpeed)
	{
		speed = aSpeed;
	}
	
	float getMaxSpeed()
	{
		return maxSpeed;
	}
	
	void setMaxSpeed(float aMaxSpeed)
	{
		maxSpeed = aMaxSpeed;
	}
	
	float getStepNumber()
	{
		return stepNumber;
	}
	
	void setStepNumber(int aStepNumber)
	{
		stepNumber = aStepNumber;
	}
	
	int getRotationSense()
	{
		return rotationSense;
	}
	
	void setRotationSense(int aRotationSense)
	{
		rotationSense = aRotationSense;
	}
	
	Joint getParent()
	{
		return parent;
	}
	
	void setParent(Joint aParent)
	{
		parent = aParent;
	}
	
	PVector getTipPosition()
	{
		float addTheta = 0;
		
		if (getParent() != null)
		{
			addTheta = getParent().getDirection();
		}
		
		return new PVector(getPosition().x + getLength() * PApplet.cos(getDirection() + addTheta),
				getPosition().y + getLength() * PApplet.sin(getDirection() + addTheta));
	}
	
	void updatePosition()
	{
		
		Joint p = getParent();
		
		if (p != null)
		{
			setPosition(p.getTipPosition());
		}
		
	}
	
	void updateRotation()
	{
		
		//Calculating the correct value to insert in the PID Position Controller
		float a = 0, b = 0;

		if (getRotationSense() == 1)
		{
			if (getDirection() >= getDirectionToAim())
			{
				a = getDirection();
				b = getDirectionToAim() + PApplet.TWO_PI;
			}
			else
			{
				a = getDirection();
				b = getDirectionToAim();
			}
		}
		else
		{
			if (getDirection() <= getDirectionToAim())
			{
				a = getDirection() + PApplet.TWO_PI;
				b = getDirectionToAim();
			}
			else
			{
				a = getDirection();
				b = getDirectionToAim();
			}
		}
		
		
		float increment = PApplet.abs(getPid().update(a, b));
		int step = (int) ((getSpeed() * getRotationSense() * increment));
		
		if ((PApplet.abs(getDirectionStep() - getDirectionStepToAim()) >= 1))
		{
			setDirectionStep(getDirectionStep() + getRotationSense());
			setIsMoving(true);
		}    
		else
			setIsMoving(false);
	
		if (getDirectionStep() > getStepNumber())
			setDirectionStep((int)(getDirectionStep() - getStepNumber()));
		if (getDirectionStep() < 0)
			setDirectionStep((int)(getDirectionStep() + getStepNumber()));
	}
	
	void setAimDirection(float aDirection, int aRotationSense)
	{		
		float thetaAdd = 0;
		
		if ( aDirection > PApplet.TWO_PI )
			thetaAdd = - PApplet.TWO_PI;
		
		if ( aDirection < 0 )
			thetaAdd = + PApplet.TWO_PI;
	
		setDirectionToAim(aDirection + thetaAdd);
		
		float A = getDirection() - getDirectionToAim();
		
		if (A > PApplet.PI) A -= PApplet.TWO_PI;
		if (A < -PApplet.PI) A += PApplet.TWO_PI;
		
		if (aRotationSense != 0)
		{
			setRotationSense(aRotationSense);
		}
		else
		{
			if (A > 0)
			{
				setRotationSense(-1);
			}
			else
			{
				setRotationSense(1);
			}
		}
	}
	
	float getTipTargetDistance(PVector aTarget)
	{
		PVector offset = new PVector(aTarget.x - getTipPosition().x, 
				aTarget.y - getTipPosition().y);
		return (offset.mag());
	}
}

