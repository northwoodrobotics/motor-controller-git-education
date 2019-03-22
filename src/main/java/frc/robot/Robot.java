/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * This sample program shows how to control a motor using a joystick. In the
 * operator control part of the program, the joystick is read and the value is
 * written to the motor.
 *
 * <p>
 * Joystick analog values range from -1 to 1 and speed controller inputs also
 * range from -1 to 1 making it easy to work together.
 */
public class Robot extends TimedRobot {
  private static final int kMotorPort = 0;
  private static final int kMotor2Port = 1;
  private static final int xboxPort = 0;

  private SpeedController m_motor;
  private SpeedController m_motor2;
  private XboxController xbox;

  @Override
  public void robotInit() {
    m_motor = new VictorSP(kMotorPort);
    m_motor2 = new VictorSP(kMotor2Port);
    xbox = new XboxController(xboxPort);
    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);
  }

  public static double deadBandSlowLower(double input) {
    double output;
    double radius = 0.2;
    double maxOutput = 1;
    assert (-1 < input && input < 1) : "input is less than -1 or greater than 1";
    assert (radius < maxOutput) : "deadband radius is greater than or equal to the maximum output";

    if (input > radius) {
      output = ((maxOutput * (input - maxOutput)) / (maxOutput - radius)) + maxOutput;
    } else if (input < -radius) {
      output = (((maxOutput * (input + maxOutput)) / (maxOutput - radius)) - maxOutput) * 0.25;
    } else {
      output = 0;
    }

    assert (Math.abs(output) <= maxOutput) : "expected to output a smaller number than the maxOutput of " + maxOutput;
    return output;
  }

  @Override
  public void teleopPeriodic() {
    m_motor.set(deadBandSlowLower(xbox.getY(Hand.kRight)) + 0.25);
    m_motor2.set(deadBandSlowLower(xbox.getY(Hand.kRight)) + 0.25);
    //System.out.println(xbox.getY(Hand.kRight));
  }
}
