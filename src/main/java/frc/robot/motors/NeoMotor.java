package frc.robot.motors;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Neo Spark Max motor.
 * 
 * @see https://github.com/REVrobotics/SPARK-MAX-Examples/blob/master/Java
 * @see https://github.com/REVrobotics/SPARK-MAX-Examples/blob/master/Java/Tank%20Drive%20With%20CAN/src/main/java/frc/robot/Robot.java
 */

public class NeoMotor extends MotorBase {

	private CANSparkMax m_sparkMax;
	private int m_canId;

	public NeoMotor(int canId, Boolean isBrushless) {
		m_canId = canId;

		if (isBrushless) {
			m_sparkMax = new CANSparkMax(m_canId, MotorType.kBrushless);
		} else {
			m_sparkMax = new CANSparkMax(m_canId, MotorType.kBrushed);
		}

		m_sparkMax.restoreFactoryDefaults(); // Do we want to do this?
	}

	public CANSparkMax getSparkMaxController() {
		return m_sparkMax;
	}

	public Boolean setSpeed(double speedValue) {
		if ((speedValue <= 1.0) && (speedValue >= -1.0)) {
			m_sparkMax.set(speedValue);
			return true;
		} else {
			return false;
		}
	}

	// In CAN mode, one SPARK MAX can be configured to follow another. This is done
	// by calling the follow() method on the SPARK MAX you want to configure as a
	// follower, and by passing as a parameter the SPARK MAX you want to configure
	// as a leader.
	public Boolean follow(CANSparkMax leadMotorController) {
		if (m_sparkMax.follow(leadMotorController) == CANError.kOk) {
			return true;
		} else {
			return false;
		}
	}
}
