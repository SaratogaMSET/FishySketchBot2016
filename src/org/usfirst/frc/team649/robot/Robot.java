package org.usfirst.frc.team649.robot;

import edu.wpi.first.wpilibj.Counter;
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
	Victor[] shooterPivotMotors;
	Joystick leftJoy;
	Joystick rightJoy;
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
	public static final int[] SHOOTER_MOTOR_PORTS = { 4, 5 };
	public static final int[] ENCODER1 = { 0, 1 };
	public static final int[] ENCODER2 = { 2, 3 };
	public static final double ENCODER_DISTANCE_PER_PULSE = 0; 
	public Counter counter;
	public Victor motor1;
	public Victor motor2;
	public Encoder encoder1;
	public Encoder encoder2;

	
	
	//button declaration(names should be changed according to function)
		//declaration of left joystick buttons
	int leftJoyButton0 = 0;
	int leftJoyButton1 = 1;
	int leftJoyButton2 = 2;
	int leftJoyButton3 = 3;
	int leftJoyButton4 = 4;
	int leftJoyButton5 = 5;
	int leftJoyButton6 = 6;
	int leftJoyButton7 = 7;
	int leftJoyButton8 = 8;
	int leftJoyButton9 = 9;
	int leftJoyButton10 = 10;
	int leftJoyButton11 = 11;
	
		//declaration of right joystick buttons
	int rightJoyButton0 = 0;
	int rightJoyButton1 = 1;
	int rightJoyButton2 = 2;
	int rightJoyButton3 = 3;
	int rightJoyButton4 = 4;
	int rightJoyButton5 = 5;
	int rightJoyButton6 = 6;
	int rightJoyButton7 = 7;
	int rightJoyButton8 = 8;
	int rightJoyButton9 = 9;
	int rightJoyButton10 = 10;
	int rightJoyButton11 = 11;
	
	//add more joysticks, motors, etc here if necessary(probably necessary)
	
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		leftJoy = new Joystick(0);
		rightJoy = new Joystick(1);
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
		
		motor1 = new Victor(SHOOTER_MOTOR_PORTS[0]);
		motor2 = new Victor(SHOOTER_MOTOR_PORTS[1]);
		encoder1 = new Encoder(ENCODER1[0],ENCODER1[1], false, EncodingType.k2X);
		encoder2 = new Encoder(ENCODER2[0],ENCODER2[1], false, EncodingType.k2X);
		encoder1.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE);
		encoder2.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE);
		counter.setReverseDirection(false);
		counter.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE);    
		
		
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
		drivetrain.tankDrive(leftJoy, rightJoy);;
		
		//intake/purge
		if(leftJoy.getRawButton(leftJoyButton0)){
			setRollerSpeed(INTAKE_SPEED);
		}
		else if(leftJoy.getRawButton(leftJoyButton1)){
			setRollerSpeed(PURGE_SPEED);
		}
		else{
			setRollerSpeed(0);
		}
		
		
		//shooter pivot
		if(leftJoy.getRawButton(leftJoyButton3) && rightJoy.getRawButton(rightJoyButton3))
		{
			
		}
		if(leftJoy.getRawButton(leftJoyButton4))
		{
			encoder1.reset();
			encoder2.reset();
		}
		if(leftJoy.getRawButton(leftJoyButton5))
		{
			counter.reset();
		}
		if(leftJoy.getRawButton(leftJoyButton6) && rightJoy.getRawButton(rightJoyButton6))
		{
			runShooter(1.0);
		}
		if(leftJoy.getRawButton(leftJoyButton7)&& rightJoy.getRawButton(rightJoyButton7))
		{
			runShooter(-1.0);
		}
		if(!leftJoy.getRawButton(leftJoyButton7) && !rightJoy.getRawButton(rightJoyButton7) 
				|| !leftJoy.getRawButton(leftJoyButton6) && !rightJoy.getRawButton(rightJoyButton6))
		{
			runShooter(0.0);
		}
		
		//implementation code for SmartDashboard information
	}

	private void setRollerSpeed(double intakeSpeed) {
		//implementation code for the intake Rollers(Victor)
		
	}
	public void runShooter(Double power)
	{
		motor1.set(power);
		motor2.set(power);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
