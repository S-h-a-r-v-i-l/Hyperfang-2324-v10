package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
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

import java.util.Base64;

@Autonomous
public class OpenCVprotoRed extends OpMode {

    OpenCvWebcam webcam1 = null;

    @Override
    public void init() {
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
        Scalar rectColor = new Scalar(255.0, 0.0, 0.0);

        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("Pipeline running");

            Rect leftRect = new Rect(1, 1, 213, 359);
            Rect rightRect = new Rect(213, 1, 213, 359);
            Rect midRect = new Rect(426, 1, 213, 359);


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

            leftavgfin = leftavg.val[0];
            rightavgfin = rightavg.val[0];
            midavgfin = midavg.val[0];

            if (leftavgfin > rightavgfin && leftavgfin > midavgfin) {
                telemetry.addLine("Left");
            } else if (midavgfin > leftavgfin && midavgfin > rightavgfin) {
                telemetry.addLine("middle");
            } else {
                telemetry.addLine("Right");
            }

            return outPut;

        }

        private DcMotor frontRight;
        private DcMotor backRight;
        private DcMotor frontLeft;
        private DcMotor backLeft;

        private ElapsedTime runtime = new ElapsedTime();

        /*
        private Base64.Decoder vuforia;

        OpticalDistanceSensor odsFL;

        BNO055IMU imu;

        Orientation angles;
        @Override
        public void runOpMode() throws InterruptedException {
            vuforia = new Base64.Decoder(hardwareMap, "AYx3Kw3/////AAAAGQreNEJhLkdWqUbBsQ06dnWIksoccLxh/R9WNkXB8hvuonWmFXUWJ2tYqM+8VqYCWXkHfanXzG/G1un7ZvwgGkkO6u0ktevZDb8AFWF2/Y4wVH1BWGQ2psV5QkHAKZ7Z6ThZI01HPZqixiQowyeUstv7W/QU8jJ48NrqGBLVYdE6eFfzNDzVY/1IvrBJaRwqKR8vo+3a2zmeFEnEhFTqMI7anU2WSPy8RP7tR61CdfidjL2biMe0RiSOBIbqOe4rs9NGaDvp1Crtz17uyY71GyMkp+Kmjbejyfj8LgZ/dZQoEsuVuQyo0dbd4KBxsEJlQj/uAEst22QoEwZe0Af4DnFtwn6/IEe02L3DT3/Np+ZX");

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;

            OpticalDistanceSensor odsFL = hardwareMap.get(OpticalDistanceSensor.class, "odsFL");

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);

            waitForStart();
            runtime.reset();
        }

        public void auto() throws InterruptedException{
            vuforia.start();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Status", "Target: " + vuforia.getDecodedColumn());
            telemetry.addData("Status", "Name: " + vuforia.getMark().name());
            telemetry.update();

            int column = vuforia.getDecodedColumn();

            if (leftavgfin > rightavgfin && leftavgfin > midavgfin) {
                switch (column) {
                    case 1:
                }
            } else if (midavgfin > leftavgfin && midavgfin > rightavgfin) {
                switch (column) {
                    case 2:
                }
            } else {
                switch (column) {
                    case 3:
                }
            }
        }
        public void modeResetEncoder() throws InterruptedException {
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        */
    }
}