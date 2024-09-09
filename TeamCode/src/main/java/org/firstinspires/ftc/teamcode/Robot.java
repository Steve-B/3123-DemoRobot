package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {


    HardwareMap hardwareMap;
    MecanumDriveTrain driveTrain = new MecanumDriveTrain();
    LinearMotion linear = new LinearMotion();

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;
        driveTrain.init(hardwareMap);
        linear.init(hardwareMap);
    }

}
