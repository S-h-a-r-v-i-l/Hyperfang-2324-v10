package org.firstinspires.ftc.teamcode.DriverOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class HyperfangTeleOp extends LinearOpMode{
    public DcMotorEx fl = null;
    public DcMotorEx bl = null;
    public DcMotorEx fr = null;
    public DcMotorEx br = null;
    public DcMotorEx sl = null;
    public DcMotorEx sr = null;
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

        fl = hardwareMap.get(DcMotorEx.class, "leftFront");
        bl = hardwareMap.get(DcMotorEx.class, "leftBack");
        fr = hardwareMap.get(DcMotorEx.class, "rightFront");
        br = hardwareMap.get(DcMotorEx.class, "rightBack");
        sl = hardwareMap.get(DcMotorEx.class, "liftLeft");
        sr = hardwareMap.get(DcMotorEx.class, "liftRight");
        psl = hardwareMap.get(CRServo.class, "percyServoLeft");
        psr = hardwareMap.get(CRServo.class, "percyServoRight");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        fl.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        sr.setDirection(DcMotorEx.Direction.REVERSE);
        psr.setDirection(CRServo.Direction.REVERSE);

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

            sl.setPower(gamepad2.right_stick_y);
            sr.setPower(gamepad2.right_stick_y);
            intake.setPower(gamepad2.right_trigger);

            psl.setPower(gamepad2.left_stick_y);
            psr.setPower(gamepad2.left_stick_y);

            sleep(50);

        }

    }
}
