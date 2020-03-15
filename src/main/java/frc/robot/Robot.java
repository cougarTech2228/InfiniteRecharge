package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.Scribe;

public class Robot extends TimedRobot {
	private RobotContainer m_robotContainer;

	@Override
	public void robotInit() {
		Scribe.initialize();
		m_robotContainer = new RobotContainer();
	}
	@Override
	public void teleopInit() {

	}
	@Override
	public void teleopPeriodic() {

	}
	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}
	@Override
	public void testPeriodic() {

	}
	@Override
	public void testInit(){
	
	}
}