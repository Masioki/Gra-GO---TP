package Services;

import DTO.LoginData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    @Test
    public void properAuthentication(){
        LoginData b = new LoginData();
        LoginData fir = new LoginData();
        LoginData sec = new LoginData();
        b.setUsername("bot");
        b.setPassword("oss");
        fir.setUsername("first");
        fir.setPassword("osss");
        sec.setUsername("first");
        sec.setPassword("ssss");

        assertFalse(Authenticator.authenticate(b));
        assertTrue(Authenticator.authenticate(fir));
        assertFalse(Authenticator.authenticate(sec));
    }
}
