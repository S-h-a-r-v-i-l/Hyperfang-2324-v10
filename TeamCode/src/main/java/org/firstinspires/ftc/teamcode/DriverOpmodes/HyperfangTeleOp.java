package org.firstinspires.ftc.teamcode.DriverOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class HyperfangTeleOp extends LinearOpMode{
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

    public CRServo drone = null;

    @Override
    public void runOpMode() {
        double speed; speed = 1;

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
        drone = hardwareMap.get(CRServo.class, "drone");

        fl.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        ll.setDirection(DcMotorEx.Direction.REVERSE);
        psl.setDirection(CRServo.Direction.REVERSE);

        fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        while (opModeIsActive()) {

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            double y = -currentGamepad1.left_stick_y * speed; // Remember, Y stick value is reversed
            double x = currentGamepad1.left_stick_x * speed; // Counteract imperfect strafing
            double rx = currentGamepad1.right_stick_x * speed;



            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            fl.setPower(frontLeftPower);
            bl.setPower(backLeftPower);
            fr.setPower(frontRightPower);
            br.setPower(backRightPower);

            ll.setPower(currentGamepad2.right_stick_y / 2);
            lr.setPower(currentGamepad2.right_stick_y / 2);
            if (gamepad2.left_stick_y > 0.1) {
                intake.setPower(1);
            } else if (gamepad2.left_stick_y < -0.1) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }

            if (gamepad2.right_bumper) {
                psl.setPower(1);
                psr.setPower(1);
            } else if (gamepad2.left_bumper) {
                psl.setPower(-1);
                psr.setPower(-1);
            } else {
                psl.setPower(0);
                psr.setPower(0);
            }

            if (gamepad2.right_trigger >= 0.1) {
                spider.setPower(1);
            } else if (gamepad2.left_trigger >= 0.1) {
                spider.setPower(-1);
            } else {
                spider.setPower(0);
            }

            if (gamepad1.right_trigger >= 0.1) {drone.setPower(-1);}
            else if (gamepad1.left_trigger >= 0.1) {drone.setPower(0.25);}
            else {drone.setPower(0);}

            if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {speed = Math.min(speed + 0.2, 1);}
            else if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {speed = Math.max(speed - 0.2, 0);}

            telemetry.addLine("" + speed);
            telemetry.update();
            sleep(50);
        }
    }
}