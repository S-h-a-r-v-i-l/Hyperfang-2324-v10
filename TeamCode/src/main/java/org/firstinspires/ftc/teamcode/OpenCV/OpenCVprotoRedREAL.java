package org.firstinspires.ftc.teamcode.OpenCV;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
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
            fl.setPower(0.15);
            bl.setPower(0.15);
            br.setPower(0.15);
            fr.setPower(0.15);
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
            fl.setPower(-0.15);
            bl.setPower(-0.15);
            br.setPower(0.15);
            fr.setPower(0.15);
            sleep(2000);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            sleep(1000);
            fl.setTargetPosition(450);
            bl.setTargetPosition(450);
            br.setTargetPosition(1650);
            fr.setTargetPosition(1650);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setPower(0.25);
            bl.setPower(0.25);
            br.setPower(0.25);
            fr.setPower(0.25);
            sleep(2000);
            fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
<<<<<<< Updated upstream
            intake.setPower(0.3);
            sleep(4000);
            intake.setPower(0.1);
=======
            intake.setPower(0.30);
            sleep(3000);
            intake.setPower(0);
>>>>>>> Stashed changes
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
            intake.setPower(0);
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
                fl.setTargetPosition(875);
                bl.setTargetPosition(875);
                br.setTargetPosition(875);
                fr.setTargetPosition(875);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
<<<<<<< Updated upstream
                fl.setPower(0.15);
                bl.setPower(0.15);
                br.setPower(0.15);
                fr.setPower(0.15);
                sleep(5000);
                intake.setPower(0.30);
                sleep(4000);
=======
                fl.setPower(0.2);
                bl.setPower(0.2);
                br.setPower(0.2);
                fr.setPower(0.2);
                sleep(3000);
                intake.setPower(0.3);
                sleep(1000);
>>>>>>> Stashed changes
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
                intake.setPower(0);
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
                fl.setTargetPosition(900);
                bl.setTargetPosition(900);
                br.setTargetPosition(900);
                fr.setTargetPosition(900);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(0.15);
                bl.setPower(0.15);
                br.setPower(0.15);
                fr.setPower(0.15);
            /*fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            fl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);*/
                sleep(3000);
                fr.setPower(0);
                br.setPower(0);
                bl.setPower(0);
                fl.setPower(0);
                sleep(1000);
                fr.setTargetPosition(300);
                br.setTargetPosition(300);
                bl.setTargetPosition(1500);
                fl.setTargetPosition(1500);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setPower(-0.2);
                br.setPower(-0.2);
                bl.setPower(0.2);
                fl.setPower(0.2);
                sleep(2000);
                fl.setPower(0);
                bl.setPower(0);
                br.setPower(0);
                fr.setPower(0);
                sleep(2000);
                fr.setTargetPosition(290);
                br.setTargetPosition(290);
                bl.setTargetPosition(1490);
                fl.setTargetPosition(1490);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setPower(0.25);
                bl.setPower(0.25);
                br.setPower(0.25);
                fr.setPower(0.25);
                sleep(2000);
                fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                intake.setPower(0.3);
<<<<<<< Updated upstream
                sleep(4000);
=======
                sleep(1000);
>>>>>>> Stashed changes
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