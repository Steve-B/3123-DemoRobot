package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanism.Carousel;
import org.firstinspires.ftc.teamcode.mechanism.Color;
import org.firstinspires.ftc.teamcode.mechanism.Hopper;
import org.firstinspires.ftc.teamcode.mechanism.Lift;
import org.firstinspires.ftc.teamcode.mechanism.chassis.MecanumChassis;

@Autonomous(name = "Lift Box (Red)", group = "Sensor")
public class LiftBoxRed extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumChassis chassis = new MecanumChassis();
    private Carousel carousel = new Carousel(Color.RED);
    private Lift lift = new Lift();
    private Hopper hopper = new Hopper();

    public void runOpMode() {
        chassis.init(hardwareMap);
        carousel.init(hardwareMap);
        lift.init(hardwareMap);
        hopper.init(hardwareMap);

        waitForStart();
        // Start button is pressed

        // Drive to the the shipping hub
        chassis.moveBackwardWithEncoders(0.6,300);
        chassis.turnRightWithEncoders(0.6,225);
        chassis.moveBackwardWithEncoders(0.6,550);

        // Deposit the box on the top level (change once camera is on)
        lift.goTo(1400,0.8);
        delay(500);
        hopper.hopper.setPosition(0.33);
        delay(700);
        hopper.hopper.setPosition(0);
        lift.goTo(0,0.8);

        // Move to the carousel and spin it
        chassis.moveForwardWithEncoders(0.6,550);
        chassis.turnRightWithEncoders(0.6,1350);
        chassis.moveBackwardWithEncoders(0.6,400);
        chassis.strafeLeftWithEncoders(0.6,1100);
        carousel.turnCarousel();
        delay(2500);
        carousel.carouselMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        carousel.carouselMotor.setPower(0);

        // Drive into the warehouse
        chassis.strafeRightWithEncoders(0.6,50);
        chassis.moveForwardWithEncoders(1, 350);
        chassis.turnRightWithEncoders(0.6,650);
        chassis.moveForwardWithEncoders(1, 5000);
    }

    public void delay(int time) {
        double startTime = runtime.milliseconds();
        while (runtime.milliseconds() - startTime < time) {
        }
    }
}
