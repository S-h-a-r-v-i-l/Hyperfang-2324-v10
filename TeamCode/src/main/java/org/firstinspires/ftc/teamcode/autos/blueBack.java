package org.firstinspires.ftc.teamcode.autos;

import androidx.appcompat.widget.MenuPopupWindow;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class blueBack extends LinearOpMode {

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

    @Override
    public void runOpMode() {
        double left;
        double right;
        double drive;
        double turn;
        double max;

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

        double circumference = 1.4763 * 2 * 3.141;
        int encoderDrivingTarget = (int) ((5/circumference)*(537.6/2));

        waitForStart();

        fl.setTargetPosition(encoderDrivingTarget);
        fr.setTargetPosition(encoderDrivingTarget);
        bl.setTargetPosition(encoderDrivingTarget);
        br.setTargetPosition(encoderDrivingTarget);

        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);

        fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        encoderDrivingTarget = (int) ((8/circumference)*(537.6/2));

        fl.setTargetPosition(encoderDrivingTarget);
        fr.setTargetPosition(encoderDrivingTarget);
        bl.setTargetPosition(encoderDrivingTarget);
        br.setTargetPosition(encoderDrivingTarget);

        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);

        fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        encoderDrivingTarget = (int) ((6.5/circumference)*(537.6/2));

        fl.setTargetPosition(encoderDrivingTarget);
        fr.setTargetPosition(encoderDrivingTarget);
        bl.setTargetPosition(encoderDrivingTarget);
        br.setTargetPosition(encoderDrivingTarget);

        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);

        fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        encoderDrivingTarget = (int) ((5/circumference)*(537.6/2));

        fl.setTargetPosition(encoderDrivingTarget);
        fr.setTargetPosition(encoderDrivingTarget);
        bl.setTargetPosition(encoderDrivingTarget);
        br.setTargetPosition(encoderDrivingTarget);

        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);
        fl.setPower(0.8);

        fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);


//        ElapsedTime timer = new ElapsedTime();
//        timer.startTime();
//        while (timer.seconds() < 1) {
//            fl.setPower(0.75);
//            fr.setPower(0.75);
//            bl.setPower(0.75);
//            br.setPower(0.75);
//        }
//        timer.reset();
//        timer.startTime();
//        while (timer.seconds() < 3) {
//            fl.setPower(-0.75);
//            fr.setPower(0.75);
//            bl.setPower(-0.75);
//            br.setPower(0.75);
//        }
//        timer.reset();
//        timer.startTime();
//        while (timer.seconds() < 2) {
//            fl.setPower(0.75);
//            fr.setPower(0.75);
//            bl.setPower(0.75);
//            br.setPower(0.75);
//        }
//        timer.reset();
//        timer.startTime();
//        while (timer.seconds() < 1){
//            fl.setPower(-0.75);
//            bl.setPower(0.75);
//            fr.setPower(0.75);
//            br.setPower(-0.75);
//        }
    }
}
