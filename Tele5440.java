package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class Tele5440 extends OpMode {
    private double powerLX, powerLY, powerRX, powerRY, robotAngle, PowerMultiplier, lf, rb, rf, lb;
    private DcMotor FL, BL, FR, BR, Arm, Slides;
    private CRServo Roller;
    private final double cpd = 6; //new arm motor
    double armPosition, armPositionFudgeFactor, targetPosition, currentPosition;

    /*
     28 // number of encoder ticks per rotation of the bare motor
   * 19.2 // This is the exact gear ratio
   * 90.0 / 30.0 // This is the external gear reduction
   * 1 / 360.0; // we want ticks per degree, not per rotation
    */

    @Override
    public void init() {
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        Slides = hardwareMap.get(DcMotor.class, "Slides");
        Arm = hardwareMap.get(DcMotor.class, "Arm");
        Roller = hardwareMap.get(CRServo.class, "Roller");

        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        Slides.setDirection(DcMotorSimple.Direction.REVERSE);
        Arm.setDirection(DcMotorSimple.Direction.FORWARD);

        //Arm.setTargetPosition(0);
        //Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        Slides.setPower(0);
        Arm.setPower(0);
        Roller.setPower(0);
        //Roller.setPosition(0);
    }

    @Override
    public void loop() {
        powerLX = gamepad1.left_stick_x;
        powerLY = gamepad1.left_stick_y;
        powerRX = gamepad1.right_stick_x;
        powerRY = gamepad1.right_stick_y;

        //HOLONOMIC
        robotAngle = Math.atan2(powerLX, powerLY);
        PowerMultiplier = Math.sqrt((Math.pow(powerLX, 2) + Math.pow(powerLY, 2)));

        lf = (PowerMultiplier * -1 * (Math.sin(robotAngle - (Math.PI / 4)))) - powerRX;
        rb = (PowerMultiplier * -1 * (Math.sin(robotAngle - (Math.PI / 4)))) + powerRX;
        lb = (PowerMultiplier * Math.sin(robotAngle + (Math.PI / 4))) - powerRX;
        rf = (PowerMultiplier * Math.sin(robotAngle + (Math.PI / 4))) + powerRX;

        FR.setPower(rf);
        FL.setPower(lf);
        BR.setPower(rb);
        BL.setPower(lb);

        //INTAKE/OUTAKE
        //SLIDES - dpad
        //up
        if (gamepad2.dpad_up) {
            Slides.setPower(0.8);
        } else {
            Slides.setPower(0);
        }
        //down
        if (gamepad2.dpad_down) {
            Slides.setPower(-0.3);
        } else {
            Slides.setPower(0);
        }

        //ARM - bumpers
        //Up
        if (gamepad1.right_bumper) {
            armMove(160, 0.07);
            Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else if (!gamepad1.right_bumper) {
            armMove(0, 0);
            Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (gamepad1.left_bumper) {
            armMove(0, -0.05);
            Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (!Arm.isBusy()) {
            Arm.setPower(0); // Stop the motor
        }

        armPositionFudgeFactor = (15 * cpd) * (gamepad2.right_trigger + (-gamepad2.left_trigger));

        //Spin
        //Collect
        if (gamepad2.x) {
            Roller.setPower(-0.5);
        }
        //Deposit
        if (gamepad2.b) {
            Roller.setPower(0.5);
        }
        //Off
        if (gamepad2.y) {
            Roller.setPower(0);
        }

        // telemetry
        telemetry.addData("powerRX: ", gamepad1.right_stick_x);
        telemetry.addData("powerRY: ", gamepad1.right_stick_y);
        telemetry.addData("powerLX: ", gamepad1.left_stick_x);
        telemetry.addData("powerLY: ", gamepad1.left_stick_y);

        telemetry.addData("FR: ", FR.getPower());
        telemetry.addData("FL: ", FL.getPower());
        telemetry.addData("BR: ", BR.getPower());
        telemetry.addData("BL: ", BL.getPower());

        telemetry.addData("Slides: ", Slides.getPower());
        telemetry.addData("Arm: ", Arm.getPower());
        telemetry.addData("Roller: ", Roller.getPower());

        telemetry.update();
    }

    public void armMove(int degree, double power){
        targetPosition = cpd*degree;
        Arm.setTargetPosition((int)targetPosition);
        Arm.setPower(power);
        Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
/*
    private void armUp(int degree, double power) {
            int a = (int) (Arm.getCurrentPosition() + (degree*cpd));
            Arm.setTargetPosition(a);
            Arm.setPower(power);
            Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (Arm.isBusy())
            {
                telemetry.addLine("Arm Up");
                telemetry.addData("Target", "%7d :%7d", a);
                telemetry.addData("Actual", "%7d: %7d", Arm.getCurrentPosition());
                telemetry.update();
            }
            /*
            if(degree < 8)
            {
                Slides.setPower(0.3);
            } else if (degree >= 8 && degree < 18)
            {
                Slides.setPower(0.35);
            } else if(degree >= 18)
            {
                Slides.setPower(0.35);
            }

        }
    private void armDown(double degree, double power)
        {
            int a = (int) (Arm.getCurrentPosition() - (degree*cpd));
            Arm.setTargetPosition(a);
            Arm.setPower(power);
            Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (Arm.isBusy())
            {
                telemetry.addLine("Arm Down");
                telemetry.addData("Target", "%7d :%7d", a);
                telemetry.addData("Actual", "%7d: %7d", Arm.getCurrentPosition());
                telemetry.update();
            }
            Arm.setPower(0);
        }
    }
    */
//SLIDES - right stick
//up
//       if (gamepad2.right_stick_y > 0.05) {
//       Slide1.setPower(right_stick_y);
//       Slide2.setPower(right_stick_y);
//       } else {
//       Slide1.setPower(0);
//       Slide2.setPower(0);
//       }
//       //down
//       if (gamepad2.right_stick_y < -0.05)    {
//       Slide1.setPower(-right_stick_y);
//       Slide2.setPower(-right_stick_y);
//       } else {
//       Slide1.setPower(0);
//       Slide2.setPower(0);
//       }

//Arm - stick
//up
//        if (gamepad2.left_stick_y > 0.05) {
//            Arm.setPower(left_stick_y);
//       } else {
//            Arm.setPower(0);
//       }
//       //down
//       if (gamepad2.left_stick_y < -0.05) {
//           Arm.setPower(-left_stick_y);
//       } else {
//           Arm.setPower(0);
//       }