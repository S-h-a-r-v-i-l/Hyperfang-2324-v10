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

            Rect leftRect = new Rect(1, 1, 1, 1);
            Rect midRect = new Rect(430, 250, 280, 300);
            Rect rightRect = new Rect(1000, 300, 280, 350);


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

            if (rightavgfin > midavgfin && rightavgfin > 137.5) {
                telemetry.addLine("Right");
                zone = 3;
            } else if (midavgfin > rightavgfin && midavgfin > 137.5) {
                telemetry.addLine("Middle");
                zone = 2;
            } else {
                telemetry.addLine("Left");
                zone = 1;
            }
            telemetry.addLine("middle: " + midavgfin);
            telemetry.addLine("right: " + rightavgfin);

            return outPut;

        }
    }

    @Override
    public void start() {
        fl.setPower(1000);
        bl.setPower(1000);
        br.setPower(1000);
        fr.setPower(1000);
        sleep(200);

        if (zone == 1) { //left

        } else {
            if (zone == 2) { //middle
                ElapsedTime timer = new ElapsedTime();
                timer.startTime();
                while (timer.seconds() < 2) {
                    fl.setPower(0.75);
                    fr.setPower(0.75);
                    bl.setPower(0.75);
                    br.setPower(0.75);
                }
                timer.reset();
                timer.startTime();
                while (timer.seconds() < 1) {
                    psl.setPower(0.75);
                    psr.setPower(0.75);
                }
                timer.reset();
                timer.startTime();

                while (timer.seconds() < 1.75) {
                    fl.setPower(-0.75);
                    fr.setPower(-0.75);
                    bl.setPower(-0.75);
                    br.setPower(-0.75);
                }
                timer.reset();
                timer.startTime();
                while (timer.seconds() < 3) {
                    fl.setPower(-0.75);
                    fr.setPower(0.75);
                    bl.setPower(-0.75);
                    br.setPower(0.75);
                }
                timer.reset();
                timer.startTime();
                while (timer.seconds() < 6) {
                    fl.setPower(0.75);
                    fr.setPower(0.75);
                    bl.setPower(0.75);
                    br.setPower(0.75);
                }
            } else { //right
                ElapsedTime timer = new ElapsedTime();
                timer.startTime();
                while (timer.seconds() < 2) {
                    fl.setPower(0.75);
                    fr.setPower(0.75);
                    bl.setPower(0.75);
                    br.setPower(0.75);
//                Odom stuff below (I think??)
//                fl.setTargetPosition(1738);
//                bl.setTargetPosition(1738);
//                fr.setTargetPosition(1738);
//                br.setTargetPosition(1738);
//
//                sleep(1000);
//
//                psl.setPower(1);
//                psr.setPower(1);
//                sleep(500);
                }
            }
        }
    }
}































