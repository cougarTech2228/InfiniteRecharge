// package frc.robot.commands;

// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.Constants;
// import frc.robot.subsystems.AcquisitionSubsystem;

// /**
//  * RunAcquireMotorCommand
//  * 
//  * 
//  */
// public class StartAcquirerCommand extends CommandBase {
//     @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

//     private AcquisitionSubsystem m_acquisitionSubsystem;
//     private Timer m_timer;

//     public StartAcquirerCommand(AcquisitionSubsystem acquisitionSubsystem) {

//         m_acquisitionSubsystem = acquisitionSubsystem;
//         m_timer = new Timer();
//         // Use addRequirements() here to declare subsystem dependencies.
//         //addRequirements();
//     }

//     // Called when the command is initially scheduled.
//     @Override
//     public void initialize() {
//         System.out.println("Start acquirer");
//         m_acquisitionSubsystem.deployAcquirer();
//         m_acquisitionSubsystem.setAcquirerSpeed(0.54);
//         m_acquisitionSubsystem.createStartAcquireCommandInstance(this);
//     }

//      // Called every time the scheduler runs while the command is scheduled.
//     @Override
//     public void execute() {
//         if(m_acquisitionSubsystem.getAcquisitionMotor().getSupplyCurrent() > Constants.ACQUIRER_CURRENT_THRESHOLD && m_timer.get() == 0) {
//             m_timer.start();
//             m_acquisitionSubsystem.setAcquirerSpeed(0.6); 
//         }
//         else if(m_timer.get() > 0.2) {
//             m_timer.stop();
//             m_timer.reset();
//             m_acquisitionSubsystem.setAcquirerSpeed(0.54); 
//         }
//     }

//     // Returns true when the command should end.
//     @Override
//     public boolean isFinished() {
//         return false;
//     }

//     // Called once the command ends or is interrupted.
//     @Override
//     public void end(boolean interrupted) {
//         System.out.println("stop acquirer");
//         m_acquisitionSubsystem.setAcquirerSpeed(0);
//         m_acquisitionSubsystem.retractAcquirer();
//     }
// }
