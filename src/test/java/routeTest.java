import com.github.pagehelper.Page;
import com.tarnett.mapper.RouteMapper;
import com.tarnett.pojo.Route;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mapper.xml", "classpath:spring-redis.xml"})
public class routeTest {

    @Resource
    private RouteMapper routeMapper;

    @Test
    public void routeTest()
    {
        Page<Route> page = routeMapper.selectByCondition("越南");
        for (Route route : page) {
            System.out.println(route.toString());
        }
    }
}
