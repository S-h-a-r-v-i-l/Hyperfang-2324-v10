package org.firstinspires.ftc.teamcode.DriverOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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
        ll = hardwareMap.get(DcMotorEx.class, "leftLift");
        lr = hardwareMap.get(DcMotorEx.class, "rightLift");
        psl = hardwareMap.get(CRServo.class, "percyL");
        psr = hardwareMap.get(CRServo.class, "percyR");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        fl.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        psl.setDirection(CRServo.Direction.REVERSE);

        fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            drive = -gamepad1.left_stick_y;
            turn  =  gamepad1.right_stick_x;

            left  = drive + turn;
            right = drive - turn;

            max = Math.max(Math.abs(left), Math.abs(right));

            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            fl.setPower(left);
            bl.setPower(left);
            fr.setPower(right);
            br.setPower(right);

            ll.setPower(gamepad2.right_stick_y);
            lr.setPower(gamepad2.right_stick_y);
            intake.setPower(gamepad2.right_trigger);
            telemetry.addLine("continuous");

            if (gamepad2.right_bumper) {
                psl.setPower(1);
                psr.setPower(1);
                telemetry.addLine("hi");
            } else if (gamepad2.left_bumper) {
                psl.setPower(-1);
                psr.setPower(-1);
                telemetry.addLine("hi2");
            } else {
                psl.setPower(0);
                psr.setPower(0);
                telemetry.addLine("hi3");
            }

            telemetry.update();
            sleep(50);

        }
    }
}