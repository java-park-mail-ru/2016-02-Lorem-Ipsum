package runners;


import frontend.*;
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
        SignUpServletTest.class
})



/**
 * Created by Installed on 18.03.2016.
 */
public class RunFrontendTests {
}
