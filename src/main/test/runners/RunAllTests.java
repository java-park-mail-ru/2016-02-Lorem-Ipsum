package runners;

import main.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountServiceAddTest.class,
        AccountServiceDelTest.class,
        AccountServiceGettersTest.class,
        UserProfileTest.class
})

/**
 * Created by Installed on 18.03.2016.
 */
public class RunAllTests {
}
