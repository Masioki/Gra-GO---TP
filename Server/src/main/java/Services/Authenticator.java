package Services;

import DTO.LoginData;

import java.util.ArrayList;
import java.util.List;

public class Authenticator {

    private static List<LoginData> dataList = new ArrayList<>();

    public static synchronized boolean authenticate(LoginData loginData) {
        for (LoginData data : dataList) {
            if (data.getUsername().equals(loginData.getUsername()))
                return false;
        }
        dataList.add(loginData);
        return true;
    }
}
