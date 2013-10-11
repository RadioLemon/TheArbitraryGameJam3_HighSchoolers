package tag3.test;

import tag3.party.Party;
import tag3.states.MainMenuState;

/**
 * Created with IntelliJ IDEA.
 * User: Starbuck
 * Date: 10/11/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleMenuState extends MainMenuState {
    Party party = null;

    @Override
    public void initState() {
        party = new Party(23,1,33);
        super.initState();
    }

    @Override
    public void updateLogic() {
        if (party == null) {
            initState();
            return;
        }
        System.out.println("Days since slept: " + party.getDaysSinceSlept());
        System.out.println("Food remaining: " + party.getFoodAmount());
        System.out.println("Water remaining: " + party.getWaterAmount());
        System.out.println("Walking pace: " + party.getWalkingPace());
        System.out.println("Moral: " + party.getMorale());
        System.out.println("Distance traveled: " + party.getDistanceTraveled());
        System.out.println("Num of diseased animals: " + party.getNumberOfDiseased());
        party.moveForward();
        super.updateLogic();
    }
}