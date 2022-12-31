import com.dream.crm.settings.mapper.UserMapper;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class test {
    @Test
    public void myTest(){
        UserServiceImpl userService = new UserServiceImpl();
        HashMap<String, Object> map = new HashMap<>();

        map.put("loginAct","ls");
        map.put("loginPwd","yf123");
        User user = userService.selectUserByLoginActAndPwd(map);
        System.out.println(user.toString());
    }
}
