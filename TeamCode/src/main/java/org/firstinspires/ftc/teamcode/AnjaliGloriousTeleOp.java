package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class AnjaliGloriousTeleOp extends OpMode {

    public DcMotorEx special = null;

    @Override
    public void init() {
        special = hardwareMap.get(DcMotorEx.class, "leftLift");
    }

    @Override
    public void loop() {
        special.setPower(gamepad1.left_stick_y/3);
    }
}
