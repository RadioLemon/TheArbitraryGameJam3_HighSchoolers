package tag3.gamelogic.encounters;

import horsentp.input.KeyDownListener;
import horsentp.input.KeyUpListener;
import tag3.gamelogic.ConfirmCommand;
import tag3.gamelogic.PartyWrapper;
import tag3.media.MediaLoader;
import tag3.states.GameOverState;
import tag3.states.PlayState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Starbuck Johnson on 10/13/13.
 */
public class HunterEncounter implements RandomEncounter, KeyDownListener{
    @Override
    public double getChancePerHour() {
        return 8;
    }

    @Override
    public void handleEncounter(PartyWrapper partyWrapper, final PlayState gameState) {
        gameState.askForConfirmation(new HunterDialog(gameState, partyWrapper));
    }

    @Override
    public void reactToKeyDown(KeyEvent keyEvent) {

    }

    private class HunterDialog implements ConfirmCommand, KeyUpListener, KeyDownListener {
        private PlayState gameState;
        private PartyWrapper partyWrapper;

        private boolean spaceLastUp = true;
        private int remainingToggles = 4;
        private long lastDownMillis = System.currentTimeMillis();
        private boolean complete = false;

        public HunterDialog(PlayState state, PartyWrapper wrapper) {
            this.gameState = state;
            this.partyWrapper = wrapper;
        }

        public void preCommandAction() {
            gameState.setResourceDialogText("Hunters have appeared!");
            gameState.setOtherResourceDialogText("TAP SPACE TO RUN AWAY");

            gameState.getInput().addKeyDownListener(this);
            gameState.getInput().addKeyUpListener(this);

            Timer timer = new Timer();
            timer.schedule(new CheckForCompletion(this), 2000);

            MediaLoader.getLoadedSound("hunters").play();
        }

        @Override
        public void onYes() {
            MediaLoader.getLoadedSound("hunters").stop();
        }

        @Override
        public void onNo() {
            MediaLoader.getLoadedSound("hunters").stop();
        }

        public synchronized void setComplete() {
            complete = true;
            partyWrapper.setMoving(false);
        }

        @Override
        public void reactToKeyDown(KeyEvent keyEvent) {
            if (complete) {
                return;
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                if (spaceLastUp) {
                    spaceLastUp = false;
                    lastDownMillis = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - lastDownMillis >= 500) {
                    System.out.println("You lose.");
                    gameState.getRunner().changeState(new GameOverState("Lost to hunters."));
                }
            }
        }

        @Override
        public void reactToKeyUp(KeyEvent keyEvent) {
            if (complete) {
                return;
            }
            if (System.currentTimeMillis() - lastDownMillis >= 500) {
                System.out.println("You lose.");
                gameState.getRunner().changeState(new GameOverState("Lost to hunters."));
            } else {
                spaceLastUp = true;
                remainingToggles--;
            }
        }

        private class CheckForCompletion extends TimerTask {
            private HunterDialog dialog;

            public CheckForCompletion(HunterDialog dialog) {
                this.dialog = dialog;

            }

            public void run() {
                if (this.dialog.remainingToggles <= 0) {
                    System.out.println("You made it");
                    MediaLoader.getLoadedSound("hunters").stop();
                    gameState.setOtherResourceDialogText("");
                    gameState.askForConfirmation(new ConfirmCommand() {
                        @Override
                        public void preCommandAction() {
                            gameState.setOtherResourceDialogText("Success");
                            gameState.setResourceDialogText("You escaped from the hunters!");
                            MediaLoader.getLoadedSound("hunters").stop();
                        }

                        @Override
                        public void onYes() {

                        }

                        @Override
                        public void onNo() {

                        }

                        @Override
                        public boolean isAChoice() {
                            return false;
                        }

                        @Override
                        public String afterChoiceText() {
                            return "";
                        }
                    });
                    partyWrapper.setMoving(false);
                    gameState.setAsking(false);
                    gameState.setPressed(true);
                    dialog.setComplete();
                } else {
                    System.out.println("You didn't make it.");
                    MediaLoader.getLoadedSound("hunters").stop();
                    gameState.setOtherResourceDialogText("");
                    gameState.getRunner().changeState(new GameOverState("Lost to hunters"));
                }
            }
        }

        public boolean isAChoice() { return false; }

        @Override
        public String afterChoiceText() {
            return "";
        }
    }
}
