package org.usfirst.frc.team649.robot;
//import statements
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
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;
	Victor roller1, roller2, roller3, roller4;//rollers for intake
	DoubleSolenoid solenoidIntake1, solenoidIntake2;//solenoids for intake
	Talon[] motors;//motors for driving(talons)
	Victor[] shooterPivotMotors;//motors for shooting(victors)
	Joystick leftJoy;//left joystick
	Joystick rightJoy;//right joystick
	Joystick manualJoy;//manual logitech f310 joystick
	public static final double INTAKE_SPEED = 1;//max intake speed
	public static final double PURGE_SPEED = -1;//max purge speed
	boolean SolenoidState1;//current state of solenoid1
	boolean SolenoidState2;//current state of solenoid2
	public static final int[] SHOOTER_MOTOR_PORTS = { 4, 5 };//ports that the two motors of the shooter are plugged into
	public static final int[] ENCODER1 = { 0, 1 };//first encoder
	public static final int[] ENCODER2 = { 2, 3 };//second encoder
	public static final double ENCODER_DISTANCE_PER_PULSE = 0;//change this, but its the distance passed in the intervals of pulses
	public Counter counter;//counter for hall effect of shooter pivot
	public Victor motor1;//first motor for shooter pivot
	public Victor motor2;//second motor for shooter pivotr
	public Encoder encoder1;//encoder for shooterpiv
	public Encoder encoder2;//encoder for shooterpiv

	// button declaration(names should be changed according to function)
	// declaration of left joystick buttons
	int intakeButton = 0;
	int purgeButton = 1;
	int encoderResetButton = 2;
	int counterResetButton = 3;
	int leftRunShooter = 4;
	int leftReverseShooter = 5;
	int forwardSolenoids = 6;
	int reverseSolenoids = 7;
	int leftJoyButton8 = 8;
	int leftJoyButton9 = 9;
	int leftJoyButton10 = 10;
	int leftJoyButton11 = 11;

	// declaration of right joystick buttons
	int rightJoyButton0 = 0;
	int rightJoyButton1 = 1;
	int rightJoyButton2 = 2;
	int rightJoyButton3 = 3;
	int rightRunShooter = 4;
	int rightReverseShooter = 5;
	int rightJoyButton6 = 6;
	int rightJoyButton7 = 7;
	int rightJoyButton8 = 8;
	int rightJoyButton9 = 9;
	int rightJoyButton10 = 10;
	int rightJoyButton11 = 11;

	// add more joysticks, motors, etc here if necessary(probably necessary)

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

		solenoidIntake1 = new DoubleSolenoid(0, 0);
		solenoidIntake2 = new DoubleSolenoid(0, 0);

		motors = new Talon[4];
		motors[0] = new Talon(0);
		motors[1] = new Talon(0);
		motors[2] = new Talon(0);
		motors[3] = new Talon(0);

		// true is open false is closed
		SolenoidState1 = true;
		SolenoidState2 = true;

		motor1 = new Victor(SHOOTER_MOTOR_PORTS[0]);
		motor2 = new Victor(SHOOTER_MOTOR_PORTS[1]);
		encoder1 = new Encoder(ENCODER1[0], ENCODER1[1], false,
				EncodingType.k2X);
		encoder2 = new Encoder(ENCODER2[0], ENCODER2[1], false,
				EncodingType.k2X);
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
		// driving
		driveFwdRot(getDriveForward(), getDriveRotate());
		// intake/purge
		intakePurgeSubsystem();
		setSolenoids();
		// shooter pivot
		shooterPivot();
		// implementation code for SmartDashboard information
	}

	private void setRollerSpeed(double intakeSpeed) {
		// implementation code for the intake Rollers(Victor)
		roller1.set(intakeSpeed);
		roller2.set(intakeSpeed);
		roller3.set(-intakeSpeed);
		roller4.set(-intakeSpeed);

	}

	public void runShooter(Double power) {
		motor1.set(power);
		motor2.set(power);
	}

	public void shooterPivot() {
		if (leftJoy.getRawButton(encoderResetButton)) {
			encoder1.reset();
			encoder2.reset();
		}
		if (leftJoy.getRawButton(counterResetButton)) {
			counter.reset();
		}
		if (leftJoy.getRawButton(leftRunShooter)
				&& rightJoy.getRawButton(rightRunShooter)) {
			runShooter(1.0);
		}
		if (leftJoy.getRawButton(leftReverseShooter)
				&& rightJoy.getRawButton(rightReverseShooter)) {
			runShooter(-1.0);
		}
		if (!leftJoy.getRawButton(leftReverseShooter)
				&& !rightJoy.getRawButton(rightReverseShooter)
				|| !leftJoy.getRawButton(leftRunShooter)
				&& !rightJoy.getRawButton(rightRunShooter)) {
			runShooter(0.0);
		}
	}

	public void intakePurgeSubsystem() {
		if (leftJoy.getRawButton(intakeButton)) {
			setRollerSpeed(INTAKE_SPEED);
		} else if (leftJoy.getRawButton(purgeButton)) {
			setRollerSpeed(PURGE_SPEED);
		} else {
			setRollerSpeed(0);
		}
	}

	public double getDriveForward() {
		return -leftJoy.getY();
	}

	public double getDriveRotate() {
		final double turningValue = rightJoy.getX();
		final double sign = turningValue < 0 ? -1 : 1;
		return Math.pow(Math.abs(turningValue), 1.4) * sign;
	}

	public void driveFwdRot(double fwd, double rot) {
		double left = fwd + rot, right = fwd - rot;
		double max = Math.max(1, Math.max(Math.abs(left), Math.abs(right)));
		left /= max;
		right /= max;
		rawDrive(left, right);
	}

	public void rawDrive(double left, double right) {
		// right
		motors[0].set(-right);
		motors[1].set(-right);
		// left
		motors[2].set(left);
		motors[3].set(left);
	}
	public void setSolenoids()
	{
		if(leftJoy.getRawButton(forwardSolenoids))
		{
			solenoidIntake1.set(Value.kForward);
			solenoidIntake2.set(Value.kForward);	
		}
		else if(leftJoy.getRawButton(reverseSolenoids))
		{
			solenoidIntake1.set(Value.kReverse);
			solenoidIntake2.set(Value.kReverse);
		}
		else
		{
			solenoidIntake1.set(Value.kOff);
			solenoidIntake2.set(Value.kOff);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}