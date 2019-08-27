package org.jeff.security.core.properties;

import lombok.Data;
import org.jeff.security.core.constants.LoginType;

/**
 * @author admin
 * <p>Date: 2019-08-26 10:36:00</p>
 */
@Data
public class BrowserProperties {

    private String loginPage = "/login.html";

    private LoginType loginType = LoginType.JSON;

}
