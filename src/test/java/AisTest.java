import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import org.junit.Test;

/**
 * @author dajin kim
 */
public class AisTest {

    @Test
    public void test() {
        CallManager callManager = CallManager.getInstance();
        CallInfo callInfo = callManager.createCallInfo("test");


        try {
            callInfo.lock();
            if (callInfo.getCallId().equals("test2")) {
                System.out.println("Try-Finally Before Return");
                return;
            }
        } finally {
            callInfo.unlock();
        }

        System.out.println("Out of Try-Finally Section");


    }
}
