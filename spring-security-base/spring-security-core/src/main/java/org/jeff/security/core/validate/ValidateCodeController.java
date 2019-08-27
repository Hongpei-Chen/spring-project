package org.jeff.security.core.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author admin
 * <p>Date: 2019-08-27 11:28:00</p>
 */
@RestController
public class ValidateCodeController {

    private static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    //spring 提供操作session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 根据随机数生成图片
        ImageCode image = imageCodeGenerator.generate(new ServletWebRequest(request));
        // 将随机数缓存
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY , image);
        // 将图片写到响应中
        ImageIO.write(image.getImage(), "JPEG", response.getOutputStream());
    }

}
