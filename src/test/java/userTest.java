import com.github.pagehelper.Page;
import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mapper.xml", "classpath:spring-redis.xml"})
public class userTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void userTest(){
        Page<User> cesh = userMapper.selectByCondition("175");
        for (User user : cesh) {
            System.out.println(user.toString());
        }
    }
}
