package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class blueLeft extends LinearOpMode {
    public DcMotorEx fl = null;
    public DcMotorEx bl = null;
    public DcMotorEx fr = null;
    public DcMotorEx br = null;
    public DcMotorEx gl = null;
    public DcMotorEx gr = null;

    public CRServo psl = null;
    public CRServo psr = null;
    @Override
    public void runOpMode() throws InterruptedException {



        double left;
        double right;
        double drive;
        double turn;
        double max;

        fl = hardwareMap.get(DcMotorEx.class, "leftFront");
        bl = hardwareMap.get(DcMotorEx.class, "leftBack");
        fr = hardwareMap.get(DcMotorEx.class, "rightFront");
        br = hardwareMap.get(DcMotorEx.class, "rightBack");
        gl = hardwareMap.get(DcMotorEx.class, "liftLeft");
        gr = hardwareMap.get(DcMotorEx.class, "liftRight");
        psl = hardwareMap.get(CRServo.class, "percyServoLeft");
        psr = hardwareMap.get(CRServo.class, "percyServoRight");

        fl.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        gr.setDirection(DcMotorEx.Direction.REVERSE);

        fl.setPower(100);
        sleep(100);


    }
}
