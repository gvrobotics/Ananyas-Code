package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class EncoderTest extends LinearOpMode {
    public DcMotor BR, BL, FR, FL, linear;
    public Servo claw;

    //clicks per degree
    double cpd = 21.94;

    //clicks per inch
    double cpi = 87.5;

    public enum robotMotion {
        forward, backward, right, left, openClaw, closeClaw, linearup, lineardown;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        linear = hardwareMap.get(DcMotor.class, "L");
        claw = hardwareMap.get(Servo.class, "c");

        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setDirection(DcMotorSimple.Direction.REVERSE);
        linear.setDirection(DcMotorSimple.Direction.REVERSE);

        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        BR.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        FL.setPower(0);
        linear.setPower(0);
        claw.setPosition(0);

        waitForStart();



        BR.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        FL.setPower(0);
        linear.setPower(0);

    }


    private void movementFB(robotMotion action, double inch, double power) {
        if (action == robotMotion.forward) {
            //Sets new position for motors
            FL.setTargetPosition((int) (FL.getCurrentPosition() + (inch * cpi)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() + (inch * cpi)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() + (inch * cpi)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() + (inch * cpi)));

            //Sets desired power for motors
            FL.setPower(power);
            FR.setPower(power);
            BL.setPower(power);
            FL.setPower(power);

            //Makes the motors to run to the position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {
//                telemetry.addLine("Move Forward");
//                telemetry.addData("Target", "%7d :%7d : %7d : %7d", FL, FR, BL, BR);
//                telemetry.addData("Actual", "%7d :%7d : %7d : %7d", FL.getCurrentPosition(),
//                        FR.getCurrentPosition(), BL.getCurrentPosition(),
//                        BR.getCurrentPosition());
//                telemetry.update();
            }

            //Stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);

            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        } else if (action == robotMotion.backward) {
            //Sets new position for motors
            FL.setTargetPosition((int) (FL.getCurrentPosition() - (inch * cpi)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() - (inch * cpi)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() - (inch * cpi)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() - (inch * cpi)));

            //Sets desired power for motors
            FL.setPower(power);
            FR.setPower(power);
            BL.setPower(power);
            FL.setPower(power);

            //Makes the motors to run to the position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {

            }

            //Stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);

            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    private void movementRL(robotMotion action, double degree, double power) {
        if (action == robotMotion.left) {
            //Sets new position for motors
            FL.setTargetPosition((int) (FL.getCurrentPosition() - (degree * cpd)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() + (degree * cpd)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() - (degree * cpd)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() + (degree * cpd)));

            //Sets desired power for motors
            FL.setPower(-power);
            FR.setPower(power);
            BL.setPower(-power);
            FL.setPower(power);

            //Makes the motors to run to the position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {

            }

            //Stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);

            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } else if (action == robotMotion.right) {
            //Sets new position for motors
            FL.setTargetPosition((int) (FL.getCurrentPosition() + (degree * cpd)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() - (degree * cpd)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() + (degree * cpd)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() - (degree * cpd)));

            //Sets desired power for motors
            FL.setPower(power);
            FR.setPower(-power);
            BL.setPower(power);
            FL.setPower(-power);

            //Makes the motors to run to the position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {

            }

            //Stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);

            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }


    private void linearup(double inch, double power) {
        linear.setTargetPosition((int) (linear.getCurrentPosition() + (inch * cpi)));
        linear.setPower(power);
        linear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (linear.isBusy()) {

        }
        linear.setPower(0.1);
        linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void lineardown(double inch, double power) {
        linear.setTargetPosition((int) (linear.getCurrentPosition() - (inch * cpi)));
        linear.setPower(power);
        linear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (linear.isBusy()) {

        }
        linear.setPower(0);

        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void Clawmovement(int Sleep, robotMotion action, double position) {
        if (action == robotMotion.openClaw) {
            claw.setPosition(position);
            sleep(Sleep);
        } else if (action == robotMotion.closeClaw) {
            claw.setPosition(position);
            sleep(Sleep);
        }
    }

    private void robotsleep(int sleep, double power) {
        BR.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        FL.setPower(0);
        linear.setPower(0);
    }
}
