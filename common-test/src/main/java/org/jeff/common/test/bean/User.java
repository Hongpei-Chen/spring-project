package org.jeff.common.test.bean;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author admin
 * <p>Date: 2019-09-10 11:32:00</p>
 */

@Data
@ToString
public class User implements Serializable {


    private static final long serialVersionUID = -7824652274928836544L;

    @Protobuf(fieldType = FieldType.STRING)
    private String id;

    @Protobuf(fieldType = FieldType.STRING)
    private String username;

    @Protobuf(fieldType = FieldType.STRING)
    private String account;

    @Protobuf(fieldType = FieldType.STRING)
    private String password;

    @Protobuf(fieldType = FieldType.INT32)
    private int age;

    @Protobuf(fieldType = FieldType.STRING)
    private String mobile;

    @Protobuf(fieldType = FieldType.STRING)
    private String email;
}
