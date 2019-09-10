package org.jeff.common.test.serialize;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author admin
 * <p>Date: 2019-09-10 10:05:00</p>
 */
@ToString
@Data
public class Persion implements Serializable {

//    private static final long serialVersionUID = -5556486826832297220L;

    private String name;
    private int age;
}
