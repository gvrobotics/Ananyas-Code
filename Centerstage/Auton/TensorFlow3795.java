package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;


@Autonomous

public class TensorFlow3795 extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    //model_20240201_082106.tflite
    private static final String TFOD_MODEL_ASSET = "BothTeamProp.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/BothTeamProp.tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
            "TP-RED", "TP-BLUE"
    };

    // TEST THESE VALUES!!
    double cpi = 7; // cycles per inch
    double cpd = 3.7; // clicks per degree

    public DcMotor BR, BL, FR, FL, Arm0, Arm1;
    //public DcMotor Rigging1, Rigging2;
    public Servo Wrist, Claw;

    double x, y;

    boolean zone1, zone2, zone3 = false;
    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    public enum robotMotion
    {
        right, left, armUp, armDown;
    }

    @Override
    public void runOpMode() throws InterruptedException{

        // hardware map
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        Arm0 = hardwareMap.get(DcMotor.class, "Arm0");
        Arm1 = hardwareMap.get(DcMotor.class, "Arm1");
        //Rigging1 = hardwareMap.get(DcMotor.class, "Rigging1");
        // Rigging2 = hardwareMap.get(DcMotor.class, "Rigging2");
        Wrist = hardwareMap.get(Servo.class, "Wrist");

        //Launcher = hardwareMap.get(Servo.class, "Launcher");
        Claw = hardwareMap.get(Servo.class, "Claw");

        //Arm = hardwareMap.get(DcMotor.class, "Arm");

        //Arm = hardwareMap.get(DcMotor.class, "Arm");


        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);


        // set mode
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // set initial power/position
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        Arm0.setPower(0);
        Arm1.setPower(0);
        //Rigging1.setPower(0);
        //Rigging2.setPower(0);
        Wrist.setPosition(0.85);
        //Launcher.setPosition(0);
        Claw.setPosition(0);
        // Arm0.setPower(0);
        // Arm1.setPower(0);


        initTfod();

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();

        waitForStart();

        //code for zone 1

        forward(2, 0.2);
        sleep(1000);
        Wrist.setPosition(0.85);
        //movementRL(robotMotion.left, 40, 0.1);


        //distance needed to go for zone 2 to place pixel
        //forward(35, 0.2);

        //code for zone 3
        /*
        forward(18, 0.2);
        sleep(1000);
        movementRL(robotMotion.right, 40, 0.1);
*/

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                telemetryTfod();

                // Push telemetry to the Driver Station.
                telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                // Share the CPU.
                sleep(20);
            }
        }


        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

    }   // end runOpMode()

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        tfod.setZoom(1.4);

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

        if(x>= 160 && x<=250 && y>=170 && y<=190){
            telemetry.addLine("Middle SI");
            zone1 = true;
        }
        else if(x>=290 && x<=330 && y>=190 && y<=250) {
            telemetry.addLine("Right SI");
            zone2 = true;
        } else{
            zone3 = true;
        }

        if(zone1==true){
            Wrist.setPosition(0.25);
            sleep(1000);
            movementRL(robotMotion.left,45, 0.1);
            sleep(1000);
            forward(15,0.1);
            sleep(1000);
            Wrist.setPosition(0.85);
            sleep(1000);
            Claw.setPosition(0.8);
            tfod.setZoom(1.85);
        }
        if(zone2==true){
            Wrist.setPosition(0.25);
            sleep(1000);
            forward(15,0.1);
            sleep(1000);
            Wrist.setPosition(0.85);
            sleep(1000);
            Claw.setPosition(0.8);
        }
/*
        if(currentRecognitions.size() >= 1) {
            double diff1 = y - FL.getCurrentPosition();
            double diff2 = y - FR.getCurrentPosition();
            double diff3 = y - BL.getCurrentPosition();
            double diff4 = y - BR.getCurrentPosition();

            telemetry.addData("Diff1: ", diff1);
            telemetry.addData("Diff2: ", diff2);
            telemetry.addData("Diff3: ", diff3);
            telemetry.addData("Diff4: ", diff4);
            telemetry.addData("FL: ", FL.getCurrentPosition());
            telemetry.addData("FR: ", FR.getCurrentPosition());
            telemetry.addData("BL: ", BL.getCurrentPosition());
            telemetry.addData("BR: ", BR.getCurrentPosition());

            if(x>= 350 && x<=420){
                telemetry.addLine("Zone 1");
                zone1 = true;
            }
            else if(y>=290 && y<=360){
                telemetry.addLine("Zone 2");
                zone2 = true;
            }


            /*

             int a = (int) ((FL.getCurrentPosition() + 12));
            int b = (int) ((FR.getCurrentPosition() + 12));
            int c = (int) ((BL.getCurrentPosition() + 12));
            int d = (int) ((BR.getCurrentPosition() + 12));

            int a = (int) ((FL.getCurrentPosition() + Math.abs(diff1) + 7));
            int b = (int) ((FR.getCurrentPosition() + Math.abs(diff2) + 7));
            int c = (int) ((BL.getCurrentPosition() + Math.abs(diff3) + 7));
            int d = (int) ((BR.getCurrentPosition() + Math.abs(diff4) + 7));
            // sets new position for motors
            FL.setTargetPosition(a);
            FR.setTargetPosition(b);
            BL.setTargetPosition(c);
            BR.setTargetPosition(d);


            // sets desired power for motors
            FL.setPower(0.3);
            FR.setPower(0.3);
            BL.setPower(0.3);
            BR.setPower(0.3);

            // sets motors to RUN_TO_POSITION
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            if(FL.getCurrentPosition() == y-7){
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
            }



            //loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {
                // TODO: Add telemetry data here
                telemetry.addData("FL Target", a);
                telemetry.addData("FL Actual", FL.getCurrentPosition());

                telemetry.addData("FR Target", b);
                telemetry.addData("FR Actual", FR.getCurrentPosition());

                telemetry.addData("BL Target", c);
                telemetry.addData("BL Actual", BL.getCurrentPosition());

                telemetry.addData("BR Target", d);
                telemetry.addData("BR Actual", BR.getCurrentPosition());

                telemetry.update();
            }


            // stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);
 */
        /*

            int a = (int) ((FL.getCurrentPosition() + Math.abs(diff1)));
            int b = (int) ((FR.getCurrentPosition() + Math.abs(diff2)));
            int c = (int) ((BL.getCurrentPosition() + Math.abs(diff3)));
            int d = (int) ((BR.getCurrentPosition() + Math.abs(diff4)));

            // sets new position for motors
            FL.setTargetPosition(a);
            FR.setTargetPosition(b);
            BL.setTargetPosition(c);
            BR.setTargetPosition(d);


            // sets desired power for motors
            FL.setPower(0.3);
            FR.setPower(0.3);
            BL.setPower(0.3);
            BR.setPower(0.3);

            // sets motors to RUN_TO_POSITION
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            Claw.setPosition(0.8);


            if(FL.getCurrentPosition() == y-7){
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
            }



            //loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {
                // TODO: Add telemetry data here
                telemetry.addData("FL Target", a);
                telemetry.addData("FL Actual", FL.getCurrentPosition());

                telemetry.addData("FR Target", b);
                telemetry.addData("FR Actual", FR.getCurrentPosition());

                telemetry.addData("BL Target", c);
                telemetry.addData("BL Actual", BL.getCurrentPosition());

                telemetry.addData("BR Target", d);
                telemetry.addData("BR Actual", BR.getCurrentPosition());

                telemetry.update();
            }


            // stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);
        }
        else if(currentRecognitions.size()==0){
            sleep(1500);
            movementRL(robotMotion.right, 40, 0.1);
            double diff1 = y - FL.getCurrentPosition();
            double diff2 = y - FR.getCurrentPosition();
            double diff3 = y - BL.getCurrentPosition();
            double diff4 = y - BR.getCurrentPosition();

            if(x>= 350 && x<=420){
                telemetry.addLine("Zone 1");
                zone1 = true;
            }
            else if(y>=290 && y<=360){
                telemetry.addLine("Zone 2");
                zone2 = true;
            }



            int a = (int) ((FL.getCurrentPosition() + Math.abs(diff1)));
            int b = (int) ((FR.getCurrentPosition() + Math.abs(diff2)));
            int c = (int) ((BL.getCurrentPosition() + Math.abs(diff3)));
            int d = (int) ((BR.getCurrentPosition() + Math.abs(diff4)));

            // sets new position for motors
            FL.setTargetPosition(a);
            FR.setTargetPosition(b);
            BL.setTargetPosition(c);
            BR.setTargetPosition(d);


            // sets desired power for motors
            FL.setPower(0.3);
            FR.setPower(0.3);
            BL.setPower(0.3);
            BR.setPower(0.3);

            // sets motors to RUN_TO_POSITION
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            if(FL.getCurrentPosition() == y-7){
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
            }



            //loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy()) {
                // TODO: Add telemetry data here
                telemetry.addData("FL Target", a);
                telemetry.addData("FL Actual", FL.getCurrentPosition());

                telemetry.addData("FR Target", b);
                telemetry.addData("FR Actual", FR.getCurrentPosition());

                telemetry.addData("BL Target", c);
                telemetry.addData("BL Actual", BL.getCurrentPosition());

                telemetry.addData("BR Target", d);
                telemetry.addData("BR Actual", BR.getCurrentPosition());

                telemetry.update();
            }


            // stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);


        }
        else{
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);
        }
        */

    }   // end method telemetryTfod()
    public void forward(double inch,  double power)
    {
        // calculate new position for (wheel) motors
        // make sure you cast to int!
        int a = (int)(FL.getCurrentPosition() + (inch * cpi));
        int b = (int)(FR.getCurrentPosition() + (inch * cpi));
        int c = (int)(BL.getCurrentPosition() + (inch * cpi));
        int d = (int)(BR.getCurrentPosition() + (inch * cpi));


        // sets new position for motors
        FL.setTargetPosition(a);
        FR.setTargetPosition(b);
        BL.setTargetPosition(c);
        BR.setTargetPosition(d);


        // sets desired power for motors
        // TODO: Do for each wheel motor
        FL.setPower(power);
        FR.setPower(power);
        BL.setPower(power);
        BR.setPower(power);


        // sets motors to RUN_TO_POSITION
        // TODO: Do for each wheel motor
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        //loop to run encoders method
        while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy())
        {
            // TODO: Add telemetry data here
            telemetry.addData("FL Target", a);
            telemetry.addData("FL Actual", FL.getCurrentPosition());

            telemetry.addData("FR Target", b);
            telemetry.addData("FR Actual", FR.getCurrentPosition());

            telemetry.addData("BL Target", c);
            telemetry.addData("BL Actual", BL.getCurrentPosition());

            telemetry.addData("BR Target", d);
            telemetry.addData("BR Actual", BR.getCurrentPosition());

            telemetry.update();
        }


        // stop motors
        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);




    }


    public void backward(double inch,  double power)
    {
        // calculate new position for motors
        int a = (int)(FL.getCurrentPosition() - (inch * cpi));
        int b = (int)(FR.getCurrentPosition() - (inch * cpi));
        int c = (int)(BL.getCurrentPosition() - (inch * cpi));
        int d = (int)(BR.getCurrentPosition() - (inch * cpi));


        // sets new position for motors
        FL.setTargetPosition(-a);
        FR.setTargetPosition(-b);
        BL.setTargetPosition(-c);
        BR.setTargetPosition(-d);


        // sets desired power for motors
        FL.setPower(power);
        FR.setPower(power);
        BL.setPower(power);
        BR.setPower(power);


        // make motors run to position
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // loop to run encoders method
        while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy())
        {
            telemetry.addData("FL Target", a);
            telemetry.addData("FL Actual", FL.getCurrentPosition());

            telemetry.addData("FR Target", b);
            telemetry.addData("FR Actual", FR.getCurrentPosition());

            telemetry.addData("BL Target", c);
            telemetry.addData("BL Actual", BL.getCurrentPosition());

            telemetry.addData("BR Target", d);
            telemetry.addData("BR Actual", BR.getCurrentPosition());

            telemetry.update();

        }

        // stop motors
        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);


    }


    public void movementRL(robotMotion action, double degree,  double power)
    {


        if(action == robotMotion.left)
        {
            // left is moving backwards, right is moving forwards
            FL.setTargetPosition((int) (FL.getCurrentPosition() - (degree * cpd)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() + (degree * cpd)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() - (degree * cpd)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() + (degree * cpd)));


            // sets desired power for motors
            FL.setPower(-power);
            FR.setPower(power);
            BL.setPower(-power);
            BR.setPower(power);




            // make motors run to position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);




            // loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy())
            {
                telemetry.addLine("turn left");

                telemetry.update();
            }


            // stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);
        }



        if(action == robotMotion.right)
        {
            // calculates new position for motors
            FL.setTargetPosition((int) (FL.getCurrentPosition() + (degree * cpd)));
            FR.setTargetPosition((int) (FR.getCurrentPosition() - (degree * cpd)));
            BL.setTargetPosition((int) (BL.getCurrentPosition() + (degree * cpd)));
            BR.setTargetPosition((int) (BR.getCurrentPosition() - (degree * cpd)));


            // sets desired power for motors
            FL.setPower(power);
            FR.setPower(-power);
            BL.setPower(power);
            BR.setPower(-power);


            // make motors run to position
            FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // loop to run encoders method
            while (FL.isBusy() && FR.isBusy() && BL.isBusy() && BR.isBusy())
            {
                telemetry.addLine("turn right");


                telemetry.update();
            }


            // stop motors
            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);
        }


    }


    private void sleep(int sleep, double power)
    {
        BR.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        FL.setPower(0);
    }



}   // end class
