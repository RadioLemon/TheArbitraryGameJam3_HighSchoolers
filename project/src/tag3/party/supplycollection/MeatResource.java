package tag3.party.supplycollection;

import tag3.party.Party;
import tag3.party.food.Food;
import tag3.party.food.Quality;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 10/11/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeatResource extends SupplyCollectPoint {

    public MeatResource(int size, Quality meatQuality) {
        super(size, meatQuality);
    }

    @Override
    public void collectFrom(Party collectingParty) {
        if (getSize() > 0) {
            int yield = (int)(getSize()*(collectingParty.getHuntingSuccess()/100.0));
            for (int i=0; i<getSize(); i++) {
                collectingParty.addFood(new Food(getQuality()));
            }
        }
    }
}
