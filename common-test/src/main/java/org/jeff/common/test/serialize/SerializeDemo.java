package org.jeff.common.test.serialize;

import java.io.*;

/**
 * @author admin
 * <p>Date: 2019-09-10 10:07:00</p>
 */
public class SerializeDemo {

    public static void main(String[] args) {
        //对象序列化
//        serializeObject();

        //反序列化
        deserializeObject();
    }

    private static void serializeObject() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File("persion")));
            Persion persion = new Persion();
            persion.setName("jeff");
            persion.setAge(20);
            outputStream.writeObject(persion);
            outputStream.flush();

            System.out.println("序列化完成");
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void deserializeObject() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("persion")));
            Persion persion = (Persion) inputStream.readObject();
            System.out.println("反序列化：" + persion);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
