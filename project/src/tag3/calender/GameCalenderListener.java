package tag3.calender;

/**
 * Created with IntelliJ IDEA.
 * User: Jonathon
 * Date: 10/12/13
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GameCalenderListener {
    public void minutePassed();
    public void hourPassed();
    public void dayPassed();
    public void weekPassed();
}
