package org.jeff.common.test.serialize;

import cn.hutool.core.util.RandomUtil;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.codehaus.jackson.map.ObjectMapper;
import org.jeff.common.test.bean.User;

import java.io.IOException;

/**
 * @author admin
 * <p>Date: 2019-09-10 11:33:00</p>
 */
public class SerializeMainDemo {

    public static void main(String[] args) throws IOException {
        User user = initUser();

        //json
        executeWithJackson(user);

        //protobuf
        executeWithProtobuf(user);
    }

    private static User initUser() {
        User user = new User();
        user.setId(RandomUtil.randomString(11));
        user.setUsername("陈飞");
        user.setAccount("chenfei");
        user.setAge(20);
        user.setPassword("qwq1#ewer$%sasd");
        user.setMobile("18621209491");
        user.setEmail("1209491@wer.com");

        return user;
    }

    private static void executeWithJackson(User user) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++){
            bytes = objectMapper.writeValueAsBytes(user);
        }

        System.out.println("Jackson 序列化：" + (System.currentTimeMillis() - start) + "ms, 总大小" + bytes.length);

        User user1 = objectMapper.readValue(bytes, User.class);
        System.out.println(user1);
    }

    /**
     * 序列化后的字节数较小
     * @param user
     * @throws IOException
     */
    private static void executeWithProtobuf(User user) throws IOException {

        byte[] bytes = null;
        Codec<User> userCodec = ProtobufProxy.create(User.class);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++){
            bytes = userCodec.encode(user);
        }

        System.out.println("Protobuf 序列化：" + (System.currentTimeMillis() - start) + "ms, 总大小" + bytes.length);

        User user1 = userCodec.decode(bytes);
        System.out.println(user1);
    }
}
