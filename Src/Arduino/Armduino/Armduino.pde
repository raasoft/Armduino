//#define DEBUG
#include <QueueList.h>

#define BAUD_RATE 19200
#define SERIAL_DELAY 5
int serialListened;
long serialMessage = 0;   
//Joint0A Joint0C  Joint1A  Joint1C   Tip   Mode    free bits
//[0 - 9][10 - 11][12 - 21][22 - 23][ 24 ][25 - 28][29 - 31]

typedef enum { 
  SETUP, TRACKING, AUTOMATIC } 
mode;
mode armMode;

#define JOINT_ANGLE_LENGTH 10
#define JOINT_CONTROL_LENGTH 2
#define TIP_LENGTH 1
#define MODE_LENGTH 3


int tipPin = 3; // Set the pin to digital I/O 4
typedef enum {
  DOWN, UP} 
tipStatus;
tipStatus tip;

#include <AccelStepper.h>

// Define some steppers and the pins the will use
//STEPS PER MOTOR : 48 * 65 / 21 * 60 / 14 = 636.734 = 637
AccelStepper stepper1(4, 8, 9, 10, 11); // Defaults to 4 pins on 2, 3, 4, 5
AccelStepper stepper2(4, 4, 5, 6, 7);
AccelStepper joints[] = {
  stepper1, stepper2};

typedef enum {
  CW, CCW, STOP} 
rotationSense;

int jointStepAim[2];
rotationSense jointRotationSense[2];

// create a stack of numbers.
QueueList <int> anglesQueue[2];
QueueList <rotationSense> rotationQueue[2];



unsigned long time;
unsigned canLoop = 0;


void setup() {

  pinMode(tipPin, OUTPUT); // Set pin as OUTPUT

    for (int i = 0; i < 2; i++)
  {
    joints[i].setMaxSpeed(90.0);
    joints[i].setAcceleration(100.0);
    joints[i].disableOutputs();
    //joints[i].setSpeed(130.0);
    //joints[i].move(1);
  }

  Serial.begin(BAUD_RATE); // Start serial communication at 9600 bps

  time = millis();

  resetAll();

}

void loop() {

  if ((millis() - time) > SERIAL_DELAY)
  {
    canLoop = 1;
    time = millis();
  }
  else
  {
    canLoop = 0;
  }

  if (canLoop)
  {
    serialListen();
  }

  updateJoints();
  updateTip();
  runJoints();
}


void serialListen()
{
  int noAvailable = Serial.available();

  if (noAvailable) 
  { 
#ifdef DEBUG
    Serial.print("Available byte: ");
    Serial.print(noAvailable);
    Serial.println("");
#endif
    if (serialListened == 0)
      serialMessage = 0;
    serialListened++;

    byte val = Serial.read();
    //    delay(SERIAL_DELAY);
#ifdef DEBUG
    Serial.print("message no.: ");
    Serial.println(serialListened, DEC);

    Serial.print("valb: ");
    Serial.println(val, BIN);
    Serial.print("vald: ");
    Serial.println(val, DEC);

    Serial.print("serial: ");
    Serial.println(serialMessage, BIN);
#endif

    serialMessage = serialMessage << 8;

#ifdef DEBUG
    Serial.print("serial shifted: ");
    Serial.println(serialMessage, BIN);
#endif

    serialMessage |= val;

#ifdef DEBUG
    Serial.print("serialORRED: ");
    Serial.println(serialMessage, BIN);
    Serial.println("");
#endif
  }   
  //digitalWrite(tipPin, LOW); // turn the LED on
  if (serialListened == 4)
  {

#ifdef DEBUG
    Serial.write("END OF TRASMISSION: ");
    Serial.println(serialMessage, BIN);
    Serial.println("");
#endif 

    serialListened = 0;
    //setupTip(serialMessage);  
    setupJoints(serialMessage);
    setupMode(serialMessage);
  }
}


void resetAll()
{
  jointRotationSense[0] = STOP;
  jointRotationSense[1] = STOP;
  tip = UP;

  serialListened = 0;
  serialMessage = 0;
  
  armMode = SETUP;
}

void setupJoints(long aValue)
{
  int temp1, temp2;

  long shift = 0;
  for (int i = 0; i < 2; i++)
  {

    temp1 = (aValue  >> shift) & 0x03FF;
    shift += JOINT_ANGLE_LENGTH;

#ifdef DEBUG
    Serial.print("motor step ");
    Serial.print(i);
    Serial.print(":d ");
    Serial.print(temp1);
    Serial.print(" / b ");
    Serial.println(temp1, BIN);
    Serial.println("");
#endif
    temp2 = (aValue  >> shift) & 0x0003;
    shift += JOINT_CONTROL_LENGTH;

#ifdef DEBUG
    Serial.print("motor sense ");
    Serial.print(i);
    Serial.print(":d ");
    Serial.print(temp2);
    Serial.print(" / b ");
    Serial.println(temp2,BIN);
    Serial.println("");
#endif

    if (temp2 == 0)
    {
      rotationQueue[i].push(STOP);
      //jointRotationSense[i] = STOP;
    }
    else if (temp2 == 1)
    {
      rotationQueue[i].push(CW);
      //jointRotationSense[i] = CW;
    }
    else if (temp2 == 2)    
    {
      rotationQueue[i].push(CCW);
      //jointRotationSense[i] = CCW;
    }
      
      if (temp2 < 3)
      {
          //jointStepAim[i] = temp1;
          anglesQueue[i].push(temp1);
      }   
  }
}

void setupMode(long aValue)
{
  int temp;
  int shift = 0;
  shift += JOINT_ANGLE_LENGTH;
  shift += JOINT_CONTROL_LENGTH;
  shift += JOINT_ANGLE_LENGTH;
  shift += JOINT_CONTROL_LENGTH;
  shift += TIP_LENGTH;
  temp = (aValue  >> shift) & 0x0003;

#ifdef DEBUG
  Serial.print("mode: d ");
  Serial.print(temp);
  Serial.print(" / b ");
  Serial.println(temp,BIN);
  Serial.println("");
#endif

  switch (temp)
  {
  case 0:
    if (armMode != SETUP)
    { 
      for (int i=0; i < 2; i++)
        joints[i].disableOutputs();
    }
    armMode = SETUP;

    break;
  case 1:
    if (armMode == SETUP)
    { 
      for (int i=0; i < 2; i++)
      {
        joints[i].enableOutputs();
        joints[i].move(1);
      }
    }
    if (armMode != TRACKING)
    for (int i=0; i < 2; i++)
      {
        joints[i].setMaxSpeed(100.0);
        joints[i].setAcceleration(100.0);
      }
    armMode = TRACKING;
    break;
  case 2:
    if (armMode == SETUP)
    { 
      for (int i=0; i < 2; i++)
      {
        joints[i].enableOutputs();
        joints[i].move(1);
      }
    }
    if (armMode != AUTOMATIC)
      for (int i=0; i < 2; i++)
      {
        joints[i].setMaxSpeed(50.0);
        joints[i].setAcceleration(30.0);
      }
    
    armMode = AUTOMATIC;
    
    
    break;
  }
}

void setupTip(long aValue)
{
  int temp;
  int shift = 0;
  shift += JOINT_ANGLE_LENGTH;
  shift += JOINT_CONTROL_LENGTH;
  shift += JOINT_ANGLE_LENGTH;
  shift += JOINT_CONTROL_LENGTH;
  temp = (aValue  >> shift) & 0x0001;

#ifdef DEBUG
  Serial.print("tip status: d ");
  Serial.print(temp);
  Serial.print(" / b ");
  Serial.println(temp,BIN);
  Serial.println("");
#endif

  if (temp == 1)
    tip = DOWN;
  else
    tip = UP;
}

void updateJoints()
{
#ifdef DEBUG
  Serial.println("");
#endif

if (joints[0].distanceToGo() == 0 && joints[1].distanceToGo() == 0)
{
 
 if (!rotationQueue[0].isEmpty()) 
 {

  int rotSense[2];
  int temp;
  
  for (int i = 0; i < 2; i++)
  {
       joints[i].enableOutputs();
    
    jointRotationSense[i] = rotationQueue[i].pop();
    temp = jointRotationSense[i];

#ifdef DEBUG
    Serial.print("motor rotational sense no. ");
    Serial.print(i);
    Serial.print(":d ");
    Serial.print(temp);
    Serial.print(" / b ");
    Serial.print(temp,BIN);
    Serial.println("");
#endif  

    if (temp == STOP)
    {
      rotSense[i] = 0;
    }
    if (temp == CW)
    {
      rotSense[i] = 1;
    }
    if (temp == CCW)    
    {
      rotSense[i] = -1;
    }

  jointStepAim[i] = anglesQueue[i].pop();

#ifdef DEBUG
    Serial.print("motor rotational aim: d ");
    Serial.print(jointStepAim[i]);
    Serial.print(" / b ");
    Serial.print(jointStepAim[i],BIN);
    Serial.println("");
#endif  

    if (temp < 3)
      joints[i].move(rotSense[i] * jointStepAim[i]);
  }
 }
 else
 {
   joints[0].disableOutputs();
   joints[1].disableOutputs();
 }
}



}

void updateTip()
{
  if (tip == DOWN)
    digitalWrite(tipPin, HIGH); // turn the LED on
  else
    digitalWrite(tipPin, LOW); // turn the LED on
}


void runJoints()
{
  for (int i = 0; i < 2; i++)
  {
    joints[i].run();
  } 
}

