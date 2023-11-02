package org.firstinspires.ftc.teamcode.OpenCV;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;


public class EasyOpenCv {
    OpenCvWebcam webcam1 = null;
    WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new pipelineBlue());

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
