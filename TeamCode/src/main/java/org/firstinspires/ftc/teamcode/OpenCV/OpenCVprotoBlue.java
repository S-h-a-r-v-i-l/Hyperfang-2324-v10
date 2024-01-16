package org.firstinspires.ftc.teamcode.OpenCV;

import static android.os.SystemClock.sleep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class OpenCVprotoBlue extends OpMode {
    public DcMotorEx fl = null;
    public DcMotorEx bl = null;
    public DcMotorEx fr = null;
    public DcMotorEx br = null;
    public DcMotorEx ll = null;
    public DcMotorEx lr = null;
    public DcMotorEx intake = null;

    public CRServo psl = null;
    public CRServo psr = null;

    public CRServo spider = null;
    OpenCvWebcam webcam1 = null;
    int zone;

    @Override
    public void init() {
        fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        fr = hardwareMap.get(DcMotorEx.class, "frontRight");
        br = hardwareMap.get(DcMotorEx.class, "backRight");
        ll = hardwareMap.get(DcMotorEx.class, "leftSlide");
        lr = hardwareMap.get(DcMotorEx.class, "rightSlide");
        psl = hardwareMap.get(CRServo.class, "percyL");
        psr = hardwareMap.get(CRServo.class, "percyR");
        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        spider = hardwareMap.get(CRServo.class, "spiderLegs");

        fl.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        ll.setDirection(DcMotorEx.Direction.REVERSE);
        psl.setDirection(CRServo.Direction.REVERSE);

        fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);



        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new PipelineBlue());

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam1.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    @Override
    public void loop() {

    }

    class PipelineBlue extends OpenCvPipeline{

        Mat YCbCr = new Mat();
        Mat midCrop;
        Mat leftCrop;
        Mat rightCrop;
        double leftavgfin;
        double rightavgfin;
        double midavgfin;
        Mat outPut = new Mat();
        Scalar rectColor = new Scalar(0.0, 0.0, 255.0);

        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("Pipeline running");

            Rect leftRect = new Rect(40, 300, 290, 350);
            Rect midRect = new Rect(585, 250, 320, 300);
            Rect rightRect = new Rect(1, 1, 1, 1);


            input.copyTo(outPut);
            Imgproc.rectangle(outPut, leftRect, rectColor, 2);
            Imgproc.rectangle(outPut, rightRect, rectColor, 2);
            Imgproc.rectangle(outPut, midRect, rectColor, 2);

            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);
            midCrop = YCbCr.submat(midRect);

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rightavg = Core.mean(rightCrop);
            Scalar midavg = Core.mean(midCrop);

            leftavgfin = leftavg.val[2];
            rightavgfin = rightavg.val[2];
            midavgfin = midavg.val[2];

            if (leftavgfin > midavgfin && leftavgfin > 138.5) {
                telemetry.addLine("Left");
                zone = 1;
            } else if (midavgfin > leftavgfin && midavgfin > 134) {
                telemetry.addLine("Middle");
                zone = 2;
            } else {
                telemetry.addLine("Right");
                zone = 3;
            }
            telemetry.addLine("middle: " + midavgfin);
            telemetry.addLine("left: " + leftavgfin);

            return outPut;

        }
    }

    @Override
    public void start() {

        //28.9768546737 ticks per inch???
        if (zone == 1) { //left
            /*fl.setPower(0.5);
            bl.setPower(0.5);
            br.setPower(0.5);
            fr.setPower(0.5);
            sleep(950);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            sleep(250);
            fl.setPower(-0.3);
            fr.setPower(0.3);
            bl.setPower(-0.3);
            fr.setPower(0.3);
            sleep(550);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            sleep(250);
            intake.setPower(0.3);
            sleep(500);
            intake.setPower(0);
            fl.setPower(-0.1);
            bl.setPower(-0.1);
            br.setPower(-0.1);
            fr.setPower(-0.1);
            sleep(700);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);
            fr.setPower(0);*/
            fl.setTargetPosition(800);
            bl.setTargetPosition(800);
            br.setTargetPosition(800);
            fr.setTargetPosition(800);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setPower(0.2);
            bl.setPower(0.2);
            br.setPower(0.2);
            fr.setPower(0.2);
            /*fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);*/
            sleep(3000);
            fl.setTargetPosition(200);
            bl.setTargetPosition(200);
            br.setTargetPosition(1400);
            fr.setTargetPosition(1400);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setPower(-0.3);
            bl.setPower(-0.3);
            br.setPower(0.3);
            fr.setPower(0.3);
            sleep(2000);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            sleep(500);
            fl.setTargetPosition(305);
            bl.setTargetPosition(305);
            br.setTargetPosition(1505);
            fr.setTargetPosition(1505);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setPower(0.2);
            bl.setPower(0.2);
            br.setPower(0.2);
            fr.setPower(0.2);
            sleep(1000);
            fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            intake.setPower(0.3);
            sleep(1000);
            intake.setPower(0);
            fl.setTargetPosition(-400);
            bl.setTargetPosition(-400);
            br.setTargetPosition(-400);
            fr.setTargetPosition(-400);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setPower(-0.5);
            bl.setPower(-0.5);
            br.setPower(-0.5);
            fr.setPower(-0.5);
            sleep(500);



        } else {
            if (zone == 2) { //middle
                /*fl.setPower(0.5);
                bl.setPower(0.5);
                br.setPower(0.5);
                fr.setPower(0.5);
                sleep(890);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(250);
                intake.setPower(0.25);
                sleep(500);
                intake.setPower(0);
                fl.setPower(-0.1);
                bl.setPower(-0.1);
                br.setPower(-0.1);
                fr.setPower(-0.1);
                sleep(700);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(250);*/
                fl.setTargetPosition(850);
                bl.setTargetPosition(850);
                br.setTargetPosition(850);
                fr.setTargetPosition(850);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
                sleep(3000);
                intake.setPower(0.20);
                sleep(500);
                intake.setPower(0);
                fl.setTargetPosition(650);
                bl.setTargetPosition(650);
                br.setTargetPosition(650);
                fr.setTargetPosition(650);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(-0.2);
                bl.setPower(-0.2);
                br.setPower(-0.2);
                fr.setPower(-0.2);
                /*fl.setTargetPosition(740);
                bl.setTargetPosition(740);
                br.setTargetPosition(740);
                fr.setTargetPosition(740);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);*/


            } else { //right
                /*fl.setPower(0.5);
                bl.setPower(0.5);
                br.setPower(0.5);
                fr.setPower(0.5);
                sleep(910);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(250);
                fl.setPower(0.3);
                fr.setPower(-0.3);
                bl.setPower(0.3);
                fr.setPower(-0.3);
                sleep(550);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(250);
                fl.setPower(-0.2);
                bl.setPower(-0.2);
                br.setPower(-0.2);
                fr.setPower(-0.2);
                sleep(300);
                intake.setPower(0.25);
                sleep(500);
                intake.setPower(0);
                fl.setPower(-0.1);
                bl.setPower(-0.1);
                br.setPower(-0.1);
                fr.setPower(-0.1);
                sleep(350);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(250);*/
                fl.setTargetPosition(800);
                bl.setTargetPosition(800);
                br.setTargetPosition(800);
                fr.setTargetPosition(800);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
            /*fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);*/
                sleep(3000);
                fr.setTargetPosition(200);
                br.setTargetPosition(200);
                bl.setTargetPosition(1400);
                fl.setTargetPosition(1400);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setPower(-0.3);
                br.setPower(-0.3);
                bl.setPower(0.3);
                fl.setPower(0.3);
                sleep(2000);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(500);
                fr.setTargetPosition(250);
                br.setTargetPosition(250);
                bl.setTargetPosition(1450);
                fl.setTargetPosition(1450);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
                sleep(1000);
                fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                intake.setPower(0.25);
                sleep(500);
                intake.setPower(0);
                fl.setTargetPosition(-400);
                bl.setTargetPosition(-400);
                br.setTargetPosition(-400);
                fr.setTargetPosition(-400);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(-0.5);
                bl.setPower(-0.5);
                br.setPower(-0.5);
                fr.setPower(-0.5);
                sleep(500);
            }
        }
    }
}































