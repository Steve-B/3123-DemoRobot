package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drivetrains.Mecanum;
import org.firstinspires.ftc.teamcode.subsystems.ActiveIntake;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Wrist;

public class FourEyesRobot extends Mecanum {
    //---------------------------------------------------------------------------------------------
    //----------------------------------Subsystem Objects------------------------------------------
    //---------------------------------------------------------------------------------------------
    HardwareMap hardwareMap;
    Lift lift;
    Arm arm;
    Wrist wrist;
    Claw claw;
    ActiveIntake activeIntake;
    //---------------------------------------------------------------------------------------------
    //----------------------------------Internal States--------------------------------------------
    //---------------------------------------------------------------------------------------------
    enum ScoringType{
        SAMPLE,
        SPECIMEN
    }

    ScoringType currentState;
    //---------------------------------------------------------------------------------------------
    //----------------------------------Initialization---------------------------------------------
    //---------------------------------------------------------------------------------------------
    public FourEyesRobot(HardwareMap hw) {
        super(hw);
        //Reassigned here to ensure that they are properly initialized
        hardwareMap = hw;
        lift = new Lift(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);
        claw = new Claw(hardwareMap);
        activeIntake = new ActiveIntake(hardwareMap);
        currentState = ScoringType.SAMPLE;
    }

    /**
     * This is to provide power to servos DURING
     * the begining of START PHASE
     */
    public void initializePowerStates(){
        lift.goToZero();
        wrist.setParallelMode();
        activeIntake.deactivateIntake();
        claw.closeClaw();
    }

    //---------------------------------------------------------------------------------------------
    //----------------------------------Automated Controls-----------------------------------------
    //---------------------------------------------------------------------------------------------

    /**
     * Function to set up subsystems to:
     * Preparation to intake SAMPLE from the Submersible
     */
    public void intakeSamplePos() {
        lift.goToSubHover(); //Set the lift just high enough to be above submersible
        arm.goToBase();//
        wrist.setParallelMode();
        activeIntake.deactivateIntake();
        currentState = ScoringType.SAMPLE;
    }

    /**
     * Function to set up subsystems to:
     * Preparation to intake SPECIMEN from the Human Player Wall
     */
    public void intakeSpecimenPos(){
        lift.goToZero(); //Lower lift as low as possible
        arm.goToSpecimen(); //Use arm to go to an angle to decrease extention length from center of rotation
        wrist.setSampleIntakeMode(); //Use wrist to counter act arm's rotation
        claw.closeClaw(); //Close the claw before hand so if the arm is behind, the claw won't hit the lift
        currentState = ScoringType.SPECIMEN;
    }

    /**
     * Function to set up subsystems to:
     * Deposit SAMPLE into High Basket
     */
    public void depositSamplePos(){
        lift.goToTopBucket(); //Raises lift to maximum height
        arm.goToDeposit(); //Flips arm to go backwards
        wrist.setParallelMode(); //Flips wrist to angle
        claw.closeClaw(); //Closes claw if it was open from before
        currentState = ScoringType.SAMPLE;
    }

    /**
     * Function to set up subsystems to:
     * Deposit SPECIMEN into High Bar
     */
    public void depositSpecimenPos(){
        lift.goToHighBar();
        arm.goToBase();
        wrist.setSampleDepositMode();
        claw.closeClaw();
        currentState = ScoringType.SPECIMEN;
    }

    public void intakeBackward() {
        activeIntake.reverseIntake();
    }
    public void intakeStop() {
        wrist.setParallelMode();
        activeIntake.deactivateIntake();
    }


    //Right bumper
    public void toggleIntake(){
        switch(lift.getState()){
            //Sample Modes
            //Currently hovering above sub
            case HOVER:
                if (wrist.getState() == Wrist.WristStates.ParallelMode) {
                    //Switch to intake mode
                    wrist.setSampleDepositMode(); //Change later to SampleDeposit
                    //Activate intake
                    activeIntake.activateIntake();
                }
                else{
                    wrist.setParallelMode();
                }
                break;
            case HIGH_BAR:
                lift.goToSpecimenScore();
                break;
            case SPECIMEN_SCORE:
                lift.goToHighBar();
                break;
            default:
                break;
        }
    }

    //Left bumper
    public void toggleDeposit(){
        switch (currentState){
            case SAMPLE:
                if (activeIntake.isRunning()) {
                    activeIntake.deactivateIntake();
                }
                else{
                    activeIntake.reverseIntake();
                }
                break;
            case SPECIMEN:
                claw.toggleClaw();

                break;
            default:
                break;
        }
    }



    public void raiseClimb(){
        arm.goToRest();
        lift.goToClimb();
    }
    public void lowerClimb(){
        arm.goToRest();
        lift.goToZero();
    }



    public void updatePID(){
        lift.update();
        arm.update();
        wrist.wristParallelToGround(arm.getRotation());
    }


    public void depositBasket(){
        currentState = ScoringType.SAMPLE;
        wrist.setSampleDepositMode();
    }

    //---------------------------------------------------------------------------------------------
    //----------------------------------Manual Controls--------------------------------------------
    //---------------------------------------------------------------------------------------------
    public boolean isIntaking() {
        return activeIntake.isRunning();
    }
    public void deactivateIntake(){
        activeIntake.deactivateIntake();
    }
    public void activateIntake(){
        activeIntake.activateIntake();
    }

    public void openClaw() {
        claw.openClaw();
    }

    public void closeClaw(){
        claw.closeClaw();
    }

    public void moveLift(double power) {
        lift.setPosition(power);
    }
    public void changeHeightArm(double power) {
        arm.setPosition(power);
    }
    public void setWristPosition(double power){
        wrist.setPosition(power);
    }
    //---------------------------------------------------------------------------------------------
    //----------------------------------Auto Actions Controls--------------------------------------
    //---------------------------------------------------------------------------------------------
    public Action autoPID(){
        return new ParallelAction(
                lift.liftPID(),
                arm.armPID(),
                new wristParallel()
        );
    }

    public Action waitForLiftArmPID(double seconds){
        return new WaitForLiftArmPID((long) seconds);
    }

    //This needed to be here since it saves the issue of transferring arm rotation to the wrist
    //class and then calling wrist to transfer a new wrist action
    public class wristParallel implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.wristParallelToGround(arm.getRotation());
            return true;
        }
    }

    public class WaitForLiftArmPID implements Action{

        private long maxWaitSeconds;
        public WaitForLiftArmPID(long maxWaitSeconds){
            this.maxWaitSeconds = System.currentTimeMillis() + maxWaitSeconds * 1000;
        }

        /**
         * Returns true if this is is supposed to loop again, returns false to stop
         * @param telemetryPacket
         * @return
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            /* This run block ends once the following is true:
            This was running for more longer than the allocated maxWaitForSeconds
            OR
            Lift Position Error within 50 ticks, Lift Velocity within 20 ticks
            AND
            Arm Position Error within 50 ticks, Arm Velocity within 20 ticks
             */

            return System.currentTimeMillis() < this.maxWaitSeconds &&
                    (Math.abs(lift.getTargetPosition() - lift.getPosition()) > 50
                            || Math.abs(lift.getVelocity()) > 20
                            || Math.abs(arm.getTargetPosition() - arm.getPosition()) > 50
                            || Math.abs(arm.getVelocity()) > 20
                            );
        }
    }


    //---------------------------------------------------------------------------------------------
    //----------------------------------Helper Functions-------------------------------------------
    //---------------------------------------------------------------------------------------------
    @SuppressLint("DefaultLocale")
    public String toString(){
        return String.format(
                "Lift Current Position: %d\n" +
                        "Lift Target Position: %d\n" +
                        "Arm Current Position: %d\n" +
                        "Arm Target Position: %d\n" +
                        "Arm Rotation %f\n" +
                        "Wrist position: %f\n" +
                        "Active intake Powered: %b\n" +
                        "Claw Open: %b\n" +
                        "Lift State: %s\n" +
                        "Arm State: %s\n" +
                        "Wrist State: %s\n" +
                        "Current Scoring Type: %s\n"
                ,
                lift.getPosition(),
                lift.getTargetPosition(),
                arm.getPosition(),
                arm.getTargetPosition(),
                arm.getRotation(),
                wrist.getWristPosition(),
                activeIntake.isRunning(),
                claw.getIsOpen(),
                lift.getState(),
                arm.getState(),
                wrist.getState(),
                currentState
        );
    }
}