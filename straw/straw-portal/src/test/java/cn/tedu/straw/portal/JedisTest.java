package cn.tedu.straw.portal;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
/**
 * @author Wu
 * @date 2022-02-15 06:51
 */
public class JedisTest {
    @Test
    public void test(){
        Jedis jedis = new Jedis("192.168.80.129",6379);
        System.out.println(jedis.ping());
        jedis.close();
    }
}
