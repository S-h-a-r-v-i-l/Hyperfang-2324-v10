//package org.firstinspires.ftc.teamcode.OpenCV;
//
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvPipeline;
//import org.openftc.easyopencv.OpenCvWebcam;
//
//public class OpenCVsetup(boolean isRed, LinearOpMode linearopMode) {
//
//    public static void EOCVSetup(boolean isRed){
//        public WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "webcam1");
//        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
//        OpenCvWebcam webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
//
//        if(isRed){
//            webcam1.setPipeline(new PipelineRed());
//        }
//        else {
//            webcam1.setPipeline(new PipelineBlue());
//        }
//
//        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//            public void onOpened() {
//                webcam1.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
//            }
//
//        }
//    }
//
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//    });
//}