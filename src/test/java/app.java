import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laofei.MySpringBootApplication;
import com.laofei.dao.UserDao;
import com.laofei.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class app {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate ;
    @Test
    public void findAll(){
        Optional<User> byId = userDao.findById(1);
        User user = byId.get();
        System.out.println(user);
    }

    @Test
    public void findAllByRedis() throws JsonProcessingException {

        // 首先从redis中获取用户信息
     String  userList =(String)   redisTemplate.boundValueOps("userList").get();

        // 如果获取到,直接返回
        if(userList!=null && userList!=""){
            System.out.println("=====================从redis中获取数据===========================");
        }
        // 如果获取不到,从数据库获取,并存入redis中
        else{

            System.out.println("=====================从mysql中获取数据===========================");
            List<User> all = userDao.findAll();
            System.out.println("=====================将数据存入redis中===========================");
           // 将List集合转化为Json格式
            ObjectMapper om = new ObjectMapper();
               userList = om.writeValueAsString(all);
            redisTemplate.boundValueOps("userList").set(userList);
        }
    }

}
