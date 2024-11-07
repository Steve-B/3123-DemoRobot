// Program created by: Danny and William
// Purpose: FTC Robot Software

package org.firstinspires.ftc.teamcode.Robotics_10650_2024_2025_Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "TeleOp_RobotCentric")
public class TeleOpCode_RobotCentric extends LinearOpMode {

    // Run the initialize function
    RobotInitialize robot;

    int liftPitchPosition = 0;
    int liftExtenderPosition = 0;
    double maxLifEtxtension =0;

    final double liftDist = 8.25;



    @Override
    public void runOpMode() throws InterruptedException {
// create and define the initialization variable
        robot = new RobotInitialize(this);

        // initialization of the control of the robot when start is pressed
        waitForStart();

        robot.clawRoll.setPosition(0.0206);

        //robot.intake.setPower(0);


        // loop while the program is running
        // waits for controller input then runs the associated code
        while(opModeIsActive()) {
            // controller inputs that is inputted by the drive team
            controllerInput();
        }
    }

    public void controllerInput() {
        //robot.intake.setPower(0);

        // Gamepad usages (two gamepads in use, one for driving and one for mechanisms):

        // Gamepad1 is used for driving (motor controls)
        // Gamepad2 is used for mechanism manipulation (moving servos and the lift motors)


        // Variables that store the different game pad movements for ease of reference later
        // Gamepad1 configuration
        {
            int speed = 2700;

            if (gamepad1.circle){
                speed = 270;
            }
            if (gamepad1.cross){
                speed = 6969;
            }

            double strafeVelocity; // (left stick x-axis movement)
            strafeVelocity = Math.pow(gamepad1.right_stick_x, 3) * speed; // Min: -10000, Max: 10000
            //telemetry.addData("gamepad1.left_stick_x (strafing)", strafePower);
            double turnVelocity; // (right stick x-axis movement)
            turnVelocity = Math.pow(gamepad1.left_stick_x, 3) * speed; // Min: -10000, Max: 10000
            //telemetry.addData("gamepad1.right_stick_x (turning)", turnPower);
            double straightMovementVelocity = 0;
            if (gamepad1.left_trigger>0) {
                //slow
                straightMovementVelocity = Math.pow(gamepad1.left_trigger, 3) * speed;
                //strafeVelocity = 0*(gamepad1.left_trigger);
                //turnVelocity =0 * (gamepad1.left_trigger);

            } else if (gamepad1.right_trigger>0) {
                //slow
                straightMovementVelocity = -Math.pow(gamepad1.right_trigger, 3) * speed;
                //strafeVelocity = 0*(gamepad1.right_trigger);
                //turnVelocity = 0 *(gamepad1.right_trigger);

            } else{
                straightMovementVelocity = 0;
            }; // (left stick y-axis movement)
//      straightMovementPower = 10000*(gamepad1.left_stick_y*gamepad1.left_stick_y*gamepad1.left_stick_y);
// Min: -10000, Max: 10000
           //original: straightMovementVelocity = Math.pow(gamepad1.right_stick_y, 3) * 10000;
            //telemetry.addData("gamepad1.left_stick_y (straight movement)", strafePower);
            //Gamepad1 controls the drivetrain

            //not sure what this does
//            if (gamepad1.circle) {
//                straightMovementVelocity = Math.pow(gamepad1.right_stick_y, 3) * 1000;
//                turnVelocity = Math.pow(gamepad1.left_stick_x, 3) * 1000;
//                strafeVelocity = Math.pow(gamepad1.right_stick_x, 3) * 1000;
//                telemetry.addData("L2 pos", gamepad1.left_trigger);
//                telemetry.update();
//            }
        /*if(gamepad1.y){
            //testing upper bound of lift
            robot.liftExtender.setPower(.25);
            telemetry.addData("position", robot.liftExtender.getCurrentPosition());
            telemetry.update();
        }*/
//            if (gamepad1.right_trigger != 0) {
//                //normal speed
//                //after testing: it went backward sby accident
//                straightMovementVelocity = 400 * Math.signum(gamepad1.right_trigger);
//                strafeVelocity = 400 * Math.signum(gamepad1.right_stick_x);
//                turnVelocity = 400 * Math.signum(gamepad1.right_stick_x);
//            }
            int pitchSpeed = 25;

            if (gamepad1.left_trigger>0) {
                //slow

                straightMovementVelocity = 2700*(gamepad1.left_trigger);
                //strafeVelocity = 0*(gamepad1.left_trigger);
                //turnVelocity =0 * (gamepad1.left_trigger);

            }
            if (gamepad1.right_trigger>0) {
                //slow
                straightMovementVelocity = -2700*(gamepad1.right_trigger);
                //strafeVelocity = 0*(gamepad1.right_trigger);
                //turnVelocity = 0 *(gamepad1.right_trigger);

            }
            if (gamepad1.circle) {
                //slow
                if (gamepad1.left_trigger>0) {
                    straightMovementVelocity = 270;
                } else if (gamepad1.right_trigger>0) {
                    straightMovementVelocity = -270;

                }
                strafeVelocity = 270*Math.signum(gamepad1.right_stick_x);
                //turnVelocity = 0 * Math.signum(gamepad1.right_stick_x);
                if (gamepad1.left_stick_x>0){
                    turnVelocity = 270*Math.signum(gamepad1.left_stick_x);
                }else if (gamepad1.left_stick_x>0) {
                    turnVelocity = -270 * Math.signum(gamepad1.left_stick_x);
                }
            }
            if (gamepad1.cross) {
                //boost
                if (gamepad1.left_trigger>0) {
                    straightMovementVelocity = 6969;
                } else if (gamepad1.right_trigger>0) {
                    straightMovementVelocity = -6969;

                }
                strafeVelocity = 6969*Math.signum(gamepad1.right_stick_x);

                if (gamepad1.left_stick_x>0){
                    turnVelocity = 6969*Math.signum(gamepad1.left_stick_x);
                }else if (gamepad1.left_stick_x>0) {
                    turnVelocity = -6969 * Math.signum(gamepad1.left_stick_x);
                }
            }



            // Set velocity of the motors (drivetrain)
            // Forward and backward movement (left stick y-axis movement)
            // Left and right turning (right stick x-axis movement)
            // Strafing left and right (left stick x-axis movement)
            {
                robot.fLeft.setVelocity(strafeVelocity - straightMovementVelocity + turnVelocity); // Overall
                // negative value
                robot.fRight.setVelocity(-strafeVelocity - straightMovementVelocity - turnVelocity); // Overall
                // positive value
                robot.bLeft.setVelocity(strafeVelocity + straightMovementVelocity - turnVelocity); // Overall
                // positive value
                robot.bRight.setVelocity(-strafeVelocity + straightMovementVelocity + turnVelocity); // Overall
                // negative value
            }
        }

        // Gamepad2 configuration
        {
            {
//            double liftPower = (gamepad2.right_stick_y);// Extends and retracts the lift
//            if (Math.abs(liftPower)>.2){
//                robot.liftExtender.setPower(liftPower);
//            }
//            double pitchPower = (gamepad2.left_stick_y);// Extends and retracts the lift
//            if (Math.abs(pitchPower)>.2){
//                robot.liftPitch.setPower(pitchPower);
//            }
//            int pitchPower;
//            pitchPower = Math.round(gamepad2.left_stick_y);
//            //telemetry.addData("pitchPower", pitchPower);
        }





            if (Math.abs(robot.liftPitch.getCurrentPosition()-liftPitchPosition)>50){
                if (robot.liftPitch.getCurrentPosition()<liftPitchPosition){
                    robot.liftPitch.setVelocity(2250);
                } else if (robot.liftPitch.getCurrentPosition()>= liftPitchPosition) {
                    robot.liftPitch.setVelocity(-2250);
                    if (liftPitchPosition>1500){
                        robot.liftPitch.setVelocity(-2450);
                    }
                }
            } else {
                robot.liftPitch.setVelocity(0);
            }
            telemetry.addData(" extender curent pos", robot.liftExtender.getCurrentPosition());
            telemetry.addData("extender target pos", liftExtenderPosition);

            telemetry.addData("Pitch CurrentPos",robot.liftPitch.getCurrentPosition());
            telemetry.addData("Pitch TargetPos",liftPitchPosition);

//            if (gamepad2.triangle) {
//                robot.liftPitch.setPosition(2700, 0.3);
//            }
//
//            if (gamepad2.cross) {
//                robot.liftExtender(0, 0.3);
//            }
            //telemetry.addData("roll", robot.clawRoll.getPosition());
//            int pitchSpeed  = 25;
//            //telemetry.addData("joystick pos", gamepad2.left_stick_y);
//            if (gamepad2.right_bumper){
//                pitchSpeed = 10;
//            } else if (!gamepad2.right_bumper){
//                pitchSpeed = 25;
//            }
            if (liftPitchPosition<=2900&&liftPitchPosition>=0||
                    (liftPitchPosition>=2900&&gamepad2.left_stick_y > 0)|| // 3200 goes to the
                    // maximum horizontal position and further (try something less than this)
                    (liftPitchPosition<=0&&gamepad2.left_stick_y < 0)) {

                //determines where the lift pitch goes
                if (gamepad2.left_stick_y > 0.2) {//going down

                    liftPitchPosition = liftPitchPosition - 25;
                    if (liftPitchPosition>1500){
                        liftPitchPosition = liftPitchPosition - 15;
                    }


                } else if (gamepad2.left_stick_y< -0.2) {//going up
                    liftPitchPosition = liftPitchPosition + 25;


                    //if it is a t a really low point


//                    if (robot.liftPitch.getCurrentPosition()<400) {
//                        robot.liftPitch.setVelocity(470 * gamepad2.left_stick_y);
//                    }else if(robot.liftPitch.getCurrentPosition()>600){
//                        robot.liftPitch.setVelocity(1550 * gamepad2.left_stick_y);
//
//
//                    } else{
//                        robot.liftPitch.setVelocity(800 * gamepad2.left_stick_y);
//                        telemetry.addData("left pitch pos", robot.liftPitch.getCurrentPosition());
//
//                    }

                }

                if(liftPitchPosition < 0) {
                    liftPitchPosition = 0;
                } else if(liftPitchPosition > 2300) {
                    liftPitchPosition = 2300;  //change to max lift xtension
                }



            //1300
            }//2700




            //lift pitch horizontal bounds

            //if pitch degree is less than 31.25

            //find positon for extension
            telemetry.addData("lift extender pos", robot.liftExtender.getCurrentPosition());
            telemetry.addData("lift pitch pos", robot.liftPitch.getCurrentPosition());



            //Bounds on the liftExtender motor
            //FixMe: Correct the liftExtender motor so that the lift stays in the same place when
            // there is no controller input
            double pitchAngle = robot.liftPitch.getCurrentPosition()*(90)/2595;
            if (pitchAngle>=31.25){
                maxLifEtxtension = 1300/(Math.sin(Math.toRadians(pitchAngle)));
            } else{
                maxLifEtxtension = 2780;

            }
            telemetry.addData("max lift etension", 1567/(Math.sin(Math.toRadians(pitchAngle))));
            telemetry.addData("cos", (Math.sin(Math.toRadians(pitchAngle))));
            telemetry.addData("pitch angle", (robot.liftPitch.getCurrentPosition()*90)/2595);
            telemetry.addData("pitch angle", pitchAngle);




            //if in bounnds, set new target pos
            if ((Math.abs(gamepad2.right_stick_y)>0.2)&&(liftExtenderPosition<=maxLifEtxtension)
                    &&(liftExtenderPosition>=0)||(robot.liftExtender.getCurrentPosition()<0&&
                    gamepad2.right_stick_y<0)||(robot.liftExtender.getCurrentPosition()>
                    maxLifEtxtension&&gamepad2.right_stick_y>0)) {

                liftExtenderPosition = liftExtenderPosition - (int)(30*gamepad2.right_stick_y);


                if(liftExtenderPosition < 0) {
                    liftExtenderPosition = 0;
                } else if(liftExtenderPosition > maxLifEtxtension) {
                    liftExtenderPosition = (int) maxLifEtxtension;  //change to max lift xtension
                }
            }


                //Determines if the liftExtender should go up or down based on the controller inputs
            if (liftExtenderPosition<=5&&robot.liftExtender.getCurrentPosition()<=5) {
                //when down, save power
                robot.liftExtender.setVelocity(0);
            }else if(Math.abs(robot.liftExtender.getCurrentPosition()-liftExtenderPosition)>25) {
                //if far from target position

                //next if own or up
                if (robot.liftExtender.getCurrentPosition() < liftExtenderPosition) {
                    robot.liftExtender.setVelocity(1500);
                } else if (robot.liftExtender.getCurrentPosition() >= liftExtenderPosition) {
                    robot.liftExtender.setVelocity(-1500);
                }
                //If no input, make sure the liftExtender motor does not move
            }else {
                robot.liftExtender.setVelocity(1);
            }
//

            if (gamepad2.left_trigger != 0) {
                robot.intake.setPower(-1.0);
                telemetry.addData("intake power", robot.intake.getPower());
            }
            else if (gamepad2.right_trigger != 0) {
                robot.intake.setPower(1.0);
                telemetry.addData("intake power", robot.intake.getPower());

            } else {
                robot.intake.setPower(0.0);
                telemetry.addData("intake power", robot.intake.getPower());
            }

            if (gamepad2.dpad_left) {
                //roll turns down
                robot.pitch.setPosition(0);
            }

            if (gamepad2.dpad_right) {
                //roll turns up
                robot.pitch.setPosition(0.0461);
            }
            if(gamepad2.dpad_down){
               //pitch moves up
                //original value:0.1867
                robot.clawRoll.setPosition(0);

            }
            if(gamepad2.dpad_up){
                //pitch moves down
                //original value 0.125
                robot.clawRoll.setPosition(0.0206);
            }







            if (gamepad1.dpad_up) {
                robot.hangL.setPosition(robot.hangL.getPosition()+0.002);

//                robot.hangR.setPosition(0.9611);
//                robot.hangL.setPosition(0.0439);
            }
            if (gamepad1.dpad_down) {
                robot.hangL.setPosition(robot.hangL.getPosition()-0.002);
//                robot.hangR.setPosition(1.0);
//                robot.hangL.setPosition(0.0);
            }
            if (gamepad1.dpad_left) {
                robot.hangR.setPosition(robot.hangR.getPosition()+0.002);

//                robot.hangR.setPosition(0.9611);
//                robot.hangL.setPosition(0.0439);
            }
            if (gamepad1.dpad_right) {
                robot.hangR.setPosition(robot.hangR.getPosition()-0.002);

//                robot.hangR.setPosition(0.9611);
//                robot.hangL.setPosition(0.0439);
            }
            telemetry.addData("hang r pos", robot.hangR.getPosition());
            telemetry.addData("hang l pos", robot.hangL.getPosition());

            if (gamepad2.circle) {
//                robot.liftPitch(726, 0.2);
//                telemetry.addData("Pitchpos", robot.liftPitch.getCurrentPosition());
                liftPitchPosition = 2000;
                robot.clawRoll.setPosition(0.0206);
            }

            if (gamepad2.square) {//slaps it in
//
                liftExtenderPosition = 0;
                liftPitchPosition = 272;

            }

            if (gamepad2.triangle) {//to score hifh basket
//                robot.liftPitch(0, 0.2);
//                telemetry.addData("Pitchpos", robot.liftPitch.getCurrentPosition());
                liftPitchPosition = 272;
                liftExtenderPosition = 2502;
            }

            if (gamepad2.left_bumper){
                robot.pitch.setPosition(robot.pitch.getPosition()+ (0.0002));
//            } if (gamepad1.square){
//                robot.hangL.setPosition(robot.hangL.getPosition()+ (0.002));
            }
            if (gamepad2.right_bumper){
                robot.pitch.setPosition(robot.pitch.getPosition()- (0.0002));
            } //if (gamepad1.triangle)
//                robot.hangL.setPosition(robot.hangL.getPosition()- (0.002));
//            }

            //i=i+Math.round(gamepad2.right_stick_y);

            telemetry.addData("aactual pitchpos", robot.pitch.getPosition());
            //telemetry.addData("hang l pos", robot.hangL.getPosition());


        }

        // accelerationAdditive is 1428.57
        // The intended result is that when the control sticks are not stationary the speed slowly
        // increases until it gets to the max value of 10000 or -10000

        /*
        if encoder value too diff from initial, then increase speed
        as change in position increases, speed increases

        diff int = currentpos-initpos
        speed

        */

//        ElapsedTime accelerationTime = new ElapsedTime();
//
//        if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0|| gamepad1.right_stick_x != 0) {
//            for (int i = 0; i < 7; i++) {
//                strafePower = gamepad1.left_stick_x * 1428.57;
//                turnPower = gamepad1.right_stick_x * 1428.57;
//                straightMovementPower = gamepad1.left_stick_y * 1428.57;
//            }
//        }

        // Prints to the robot driver station screen
        telemetry.update();
    }
}