package org.usfirst.frc.team649.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot{
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;
	Victor roller1, roller2, roller3, roller4;
	DoubleSolenoid solenoidIntake1, solenoidIntake2;
	Talon[] motors;
	Joystick joy1;
	Joystick joy2;
	Joystick manualJoy;
	public static final double INTAKE_SPEED = 1;
	public static final double PURGE_SPEED = -1;
	boolean SolenoidState1;
	boolean SolenoidState2;
	public static final int FL_MOTOR = 0;
	public static final int FR_MOTOR = 0;
	public static final int BR_MOTOR = 0;
	public static final int BL_MOTOR = 0;
	public RobotDrive drivetrain = new RobotDrive(FL_MOTOR,FR_MOTOR,BR_MOTOR,BL_MOTOR);
	
	//add more joysticks, motors, etc here if necessary(probably necessary)
	
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		joy1 = new Joystick(0);
		joy2 = new Joystick(1);
		manualJoy = new Joystick(2);
		
		roller1 = new Victor(0);
		roller2 = new Victor(0);
		roller3 = new Victor(0);
		roller4 = new Victor(0);
		
		solenoidIntake1 = new DoubleSolenoid(0,0);
		solenoidIntake2 = new DoubleSolenoid(0,0);
		
		motors = new Talon[4];
		motors[0] = new Talon(0);
		motors[1] = new Talon(0);
		motors[2] = new Talon(0);
		motors[3] = new Talon(0);
		
		//true is open false is closed
		SolenoidState1 = true;
		SolenoidState2 = true;	
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		autoSelected = (String) chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		//driving
		drivetrain.tankDrive(joy1, joy2);
		
		//intake/purge
		if(joy1.getRawButton(0)){
			setRollerSpeed(INTAKE_SPEED);
		}
		else if(joy1.getRawButton(0)){
			setRollerSpeed(PURGE_SPEED);
		}
		else{
			setRollerSpeed(0);
		}
		
		
		//shooter pivot
		if(joy1.getRawButton(0) && joy1.getRawButton(0))
		{
			//implementation code for shooterPivot
		}
		//implementation code for SmartDashboard information
	}
	

	private PIDController getPIDController() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setRollerSpeed(double intakeSpeed) {
		//implementation code for the intake Rollers(Victors)
		
	}

	public double getDriveForward() {
		// TODO Auto-generated method stub
		return -joy1.getY();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
