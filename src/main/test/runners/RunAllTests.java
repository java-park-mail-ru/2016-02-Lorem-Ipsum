package runners;

import frontend.*;
import main.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ChangeUserServletTest.class,
        DeleteUserServletTest.class,
        GetUserProfileServletTest.class,
        IsAuthenticatedServletTest.class,
        LogOutServletTest.class,
        SignInServletTest.class,
        SignUpServletTest.class,
        AccountServiceAddTest.class,
        AccountServiceDelTest.class,
        AccountServiceGettersTest.class,
        UserProfileTest.class,
        MainTest.class
})

/**
 * Created by Installed on 18.03.2016.
 */
public class RunAllTests {
}
