package org.jeff.spring.security.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.jeff.spring.security.demo.validator.IPValid;

import javax.validation.constraints.Past;
import java.util.Date;

@Data
public class User {

    public interface UserSimpleView {}
    public interface UserDetailView extends UserSimpleView{}

    private String id;

    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Past(message = "生日必须是过去时间")
    private Date birthday;

    @IPValid(message = "请输入正确的IP地址")
    private String ip;

    @JsonView(UserSimpleView.class)
    public String getId() {
        return id;
    }

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }
}
