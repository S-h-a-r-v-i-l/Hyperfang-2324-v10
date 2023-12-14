package org.firstinspires.ftc.teamcode.OpenCV;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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
public class OpenCVprotoRedREAL extends OpMode {
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

        webcam1.setPipeline(new PipelineRed());

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

    class PipelineRed extends OpenCvPipeline{

        Mat YCbCr = new Mat();
        Mat midCrop;
        Mat leftCrop;
        Mat rightCrop;
        double leftavgfin;
        double rightavgfin;
        double midavgfin;
        Mat outPut = new Mat();
        Scalar rectColor = new Scalar(0.0, 255.0, 0.0);

        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("Pipeline running");

            Rect leftRect = new Rect(40, 300, 290, 350);
            Rect midRect = new Rect(585, 250, 290, 300);
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

            leftavgfin = leftavg.val[1];
            rightavgfin = rightavg.val[1];
            midavgfin = midavg.val[1];

            if (leftavgfin > midavgfin && leftavgfin > 136.25) {
                telemetry.addLine("Left");
                zone = 1;
            } else if (midavgfin > leftavgfin && midavgfin > 136.25) {
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


        if (zone == 1) { //left
            fl.setPower(0.2);
            bl.setPower(0.2);
            br.setPower(0.2);
            fr.setPower(0.2);
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
            fr.setPower(0);


        } else {
            if (zone == 2) { //middle
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
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
                sleep(250);


            } else { //right
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
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
                sleep(250);
            }
        }
    }
}