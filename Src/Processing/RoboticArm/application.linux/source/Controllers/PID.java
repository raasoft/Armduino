package Controllers;

public class PID
{	  
	  float oldError;
	  float error;
	  float integral;
	  float derivative;
	  float Kp;
	  float Ki;
	  float Kd;
	  
	  public PID(float aKp, float aKi, float aKd)
	  {
		  setKP(aKp);
		  setKD(aKd);
		  setKI(aKi);
		  setOldError(0);
		  setError(0);
		  setIntegral(0);
		  setDerivative(0);
	  }
	  
	  public void setKP(float aKP)
	  {
		  Kp = aKP;
	  }
	  
	  public float getKP()
	  {
		  return Kp;
	  }
	  
	  public void setKI(float aKI)
	  {
		  Ki = aKI;
	  }
	  
	  public float getKI()
	  {
		  return Ki;
	  }
	  
	  public void setKD(float aKD)
	  {
		  Kd = aKD;
	  }
	  
	  public float getKD()
	  {
		  return Kd;
	  }
	  
	  public void setIntegral(float aIntegral)
	  {
		  integral = aIntegral;
	  }
	  
	  public float getIntegral()
	  {
		  return integral;
	  }
	  
	  public void setDerivative(float aDerivative)
	  {
		  derivative = aDerivative;
	  }
	  
	  public float getDerivative()
	  {
		  return derivative;
	  }
	  
	  public void setError(float aError)
	  {
		  error = aError;
	  }
	  
	  public float getError()
	  {
		  return error;
	  }
	  
	  public void setOldError(float aOldError)
	  {
		  oldError = aOldError;
	  }
	  
	  public float getOldError()
	  {
		  return oldError;
	  }
	  
	  public float update(float aX, float aY)
	  {
	  
		  float increment = 0;
		  
		  setError(aY - aX);
		  setIntegral(getError());
		  setDerivative(getError() - getOldError());
		  
		  increment = getDerivative() * getKD() + getIntegral() * getKI() + getError() * getKP();
		  setOldError(getError());
		  
		  return increment;
	  }
	  
}