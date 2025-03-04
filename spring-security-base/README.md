### spring security知识点
1. 企业级认证模块

    - 多种认证方式
    - 多种终端
    - 集群环境、跨应用、SESSION控制、控制用户权限、防护与身份认证相关的攻击

### spring social 第三方认证


### spring security oAuth 基于token

### 项目结构
- spring-security-base: 父项目管理版本
- spring-security-core: 核心业务逻辑
- spring-security-browser: 浏览器安全特定
- spring-security-app: app相关特定
- spring-security-demo: 样例

#### spring-security-base
- pom文件看源码

### RESTful API
 1. 用URL描述资源
 2. 使用HTTP方法描述行为，使用HTTP状态码来表示不同结果
 3. 使用json交互数据
 4. RESTful只是一种风格
 
 
### spring boot的功能点
1. RESTful请求

    - 请求方法
    
        - GetMapping  查询
        - PostMapping 添加
        - PutMapping  更新
        - DeleteMapping 删除
 
2. 测试用例
     ```java
     //查看测试用例
     ```
3. 常用注解使用

    - RestController
    
    - JsonView
        
        - 返回指定视图，与自定义视图的使用
        ```java
        @GetMapping("/{id:\\d+}")
        @JsonView(User.UserDetailView.class)
        public User getInfo() {
          return new User()
        }
        ```

4. 自定义视图(返回指定属性内容)
    ```java
    public class User {
        
        /**
        * 返回简单视图
        */
        public interface UserSimpleView {}
        
        /**
         * 返回详细视图
         */
        public interface UserDetailView extends UserSimpleView{}
        
        @JsonView(UserSimpleView.class)
        private String id;
        
        @JsonView(UserSimpleView.class)
        private String username;
        
        @JsonView(UserDetailView.class)
        private String password;
    }
    ```

5. 校验

    - 校验方式
    
        - url上的直接校验
        ```java
        @GetMapping("/{id:\\d+}")
        ```
        - 使用框架校验注解
        - 自定义校验
        ```java
            //1. 自定义注解
            @Documented
            @Target({ METHOD, FIELD})
            @Retention(RUNTIME)
            @Constraint(validatedBy = IPConstraint.class)
            public @interface IPValid {
            
                String message() default "{IPValid.message}";
            
                Class<?>[] groups() default { };
            
                Class<? extends Payload>[] payload() default { };
            
            }
      
            //2. 自定义注解处理类
              public class IPConstraint implements ConstraintValidator<IPValid, String> {
              
                  //可以使用自动注入
            
                  @Override
                  public void initialize(IPValid constraintAnnotation) {
              
                  }
              
                  @Override
                  public boolean isValid(String value, ConstraintValidatorContext context) {
                      if (Objects.nonNull(value)) {
                          //TODO 验证IP
                          return true;
                      }
                      return false;
                  }
              }
        ```

6. 自定义异常

    - 自定义异常类
    ```java
    public class UserNotExistException extends RuntimeException {
    
        private String id;
    
        public UserNotExistException(String id) {
            super("user not exist");
            this.id = id;
        }
    
        public String getId() {
            return id;
        }
    
        public void setId(String id) {
            this.id = id;
        }
    }
    ```
    - 处理自定义异常的Controller
    ```java
    @ControllerAdvice
    public class ControllerExceptionHandler {
          
        /**
         * 处理UserNotExistException异常
         */
        @ExceptionHandler(UserNotExistException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String, Object> handleUserNotExistException(UserNotExistException ex) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", ex.getId());
            result.put("message", ex.getMessage());
            return result;
        }
    }
    ```
    
7. 切片拦截服务
    - Filter 过滤器
        ```
        只能拦截Http的请求和响应，无法获知请求是由哪个控制器处理,因为Filter的规范是j2ee的规范
        ```
        
        - 自定义过滤器
        ```java
          //实现Filter接口，并添加@Component注解
          import org.springframework.stereotype.Component;
          
          import javax.servlet.*;
          import java.io.IOException;
          import java.util.Date;
          
          @Component
          public class TimeFilter implements Filter {
              @Override
              public void init(FilterConfig filterConfig) throws ServletException {
                  System.out.println("time filter destroy");
              }
          
              @Override
              public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
                      throws IOException, ServletException {
                  System.out.println("time filter start");
                  long start = new Date().getTime();
                  filterChain.doFilter(request, response);
                  System.out.println("time filter 耗时:"+ (new Date().getTime() - start));
                  System.out.println("time filter finish");
              }
          
              @Override
              public void destroy() {
                  System.out.println("time filter init");
              }
          }
        ```
    
        - 添加第三方拦截器
        ```java
        //通过FilterRegistrationBean注册,同上面添加@Component注解同样的效果
          @Configuration
          public class WebConfig {
          
              @Bean
              public FilterRegistrationBean filterRegistrationBean() {
                  FilterRegistrationBean registrationBean = new FilterRegistrationBean();
                  registrationBean.setFilter(new TimeFilter());
          
                  //添加拦截的url请求
                  List<String> urls = new ArrayList<>();
                  urls.add("/*");
                  registrationBean.setUrlPatterns(urls);
          
                  return registrationBean;
              }
          }
      
        ```
        
    - Interceptor 拦截器
        ```
        spring 自身提供的拦截机制(可拦截控制器的请求)，但无法获取执行方法的参数值
        ``` 
        - 拦截器实现
        ```java
        //1. 自定义拦截器类并实现HandlerInterceptor
        //2. 注册拦截器
          @Configuration
          public class WebConfig extends WebMvcConfigurerAdapter {
          
              @Autowired
              private TimeInterceptor timeInterceptor;
          
              @Override
              public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(timeInterceptor);
              }  
          }   
        ```
    
    - Aspect 切片
        ```
        能够获取请求方法的参数
        ```
        - 实现
        ```java
            @Component
            @Aspect
            public class TimeAspect {
            
                @Around("execution(* UserController.*(..))")
                public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
            
                    System.out.println("time aspect start");
            
                    Object[] args = pjp.getArgs();
                    for (Object arg : args) {
                        System.out.println("arg is "+arg);
                    }
            
                    long start = new Date().getTime();
                    Object object = pjp.proceed();
                    System.out.println("time aspect 耗时:"+ (new Date().getTime() - start));
                    System.out.println("time aspect end");
            
                    return object;
                }
            }
        ```

        - 处理机制连
         ```
            入：Filter --> Interceptor --> ControllerAdvice --> Aspect --> Controller
            出：Filter <-- Interceptor <-- ControllerAdvice <-- Aspect <-- Controller                                                                 
         ```
    
8. 文件上传下载 

    - 上传
    
    - 下载

9. 异步处理REST服务
    - Callable

        -使用Callable进行异步处理，副线程写在主线程里面的 ，符合企业级开发的一般应用场景
        ```java
            @RestController
            @RequestMapping("/order")
            public class AsyncController {
            
                private Logger logger = LoggerFactory.getLogger(getClass());
            
                @GetMapping
                public Callable<String> order() throws Exception {
                    logger.info("主线程开启");
                    Callable<String> result = new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            logger.info("副线程开启");
                            Thread.sleep(2000);
                            logger.info("副线程关闭");
                            return "success";
                        }
                    };
                    logger.info("主线程关闭");
                    return result;
                }
            
            }
        ```
    - DefferedResult

        - 使用DefferedResult异步处理Rest服务，符合企业级开发的复杂应用场景
        ```java
        //下订单场景
        
        ```

- 前端交互
    - Swagger自动生成html文档
    
    - WireMock 快速伪造RESTful服务
    
### spring security 示例

#### 基本原理

1. 核心(过滤器链) 

    - UsernamePasswordAuthenticationFilter
    
    - BasicAuthenticationFilter
 
    - 还有其它过滤器
    
    - ExceptionTranslationFilter 
        ```
        捕获最后过滤器的异常
        ```
    - FilterSecurityInterceptor (最后一个过滤器)
        ```
         判断前面的认证规则是否通过,若不通过则抛出相应异常
        ```  

#### 认证

- 自定义用户认证逻辑

    - 处理用户信息获取 UserDetailsService 
        ```java
        @Service
        public class UserDetailServiceImpl implements UserDetailsService {
        
            private Logger logger = LoggerFactory.getLogger(getClass());
        
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                logger.info("登陆用户名：" + username);
                //根据用户名查询用户信息
                return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));;
            }
        
        }
        ```
    
    - 处理用户校验 UserDetails
        ```
        //设置UserDetails相应的属性值
        isAccountNonExpired：        用户没有过期
        isAccountNonLocked：         用户没有被锁
        isCredentialsNonExpired：    凭证(密码)是否过期
        isEnabled：                  用户可用(删除状态)
        ```
    - 处理密码加密解密(PasswordEncoder)
        
        - PasswordEncoder
        ```java
          /**
           * 密码加密，用户自定义的加密
           **/
          String encode(CharSequence rawPassword);
  
          /**
           * 比较用户提交的密码是否和加密后的密码一致
           **/
          boolean matches(CharSequence rawPassword, String encodedPassword);
        ```
        
        - 配置
        ```java
            //BrowserSecurityConfig extends WebSecurityConfigurerAdapter 
            /**
             * 密码加密
             */
            @Bean
            public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }
      
            //UserDetailServiceImpl implements UserDetailsService
            @Autowired
            private PasswordEncoder passwordEncoder;
      
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                logger.info("登录用户名：" + username);
          
                //应该在用户注册时的加密操作，从数据库获取的密码应该是加密后的
                String password = passwordEncoder.encode("123456");
                logger.info("登录密码：" + password);
                User user = new User(username, password, true, true, true, true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        
                return user;
            }
        ```
- 个性化认证流程

    - 自定义登录认证逻辑
        ```java
        //用户未授权请求时，跳转到自定义的controller处理认证逻辑(主要判断是浏览器请求还是其它rest请求)
            //配置
            //securityProperties.getBrowser().getLoginPage() 为自定义的properties属性(自定义登录页面)
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                //表单验证
                http.formLogin()
                        //跳转到指定的controller处理认证逻辑
                        .loginPage("/authentication/require")
                        //通知UsernamePasswordAuthenticationFilter处理登录验证的路径
                        .loginProcessingUrl("/authentication/form")
                        .and()
                        .authorizeRequests()
                        //匹配可放行页面
                        .antMatchers("/authentication/require",
                                securityProperties.getBrowser().getLoginPage()).permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        //跨站伪造攻击配置
                        .csrf().disable();
            }
      
            //自定义controller
            @RequestMapping("/authentication/require")
            @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
            public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        
                SavedRequest savedRequest = requestCache.getRequest(request, response);
        
                if (savedRequest != null) {
                    String targetUrl = savedRequest.getRedirectUrl();
                    logger.info("引发跳转的请求是:" + targetUrl);
                    if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                        //如果是html请求，跳转到指定的的登录页
                        redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
                    }
                }
                //rest请求返回未授权信息
                return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
            }
        
        ```
    - 自定义登录成功处理
    
        - 实现AuthenticationSuccessHandler接口
        ```java
        /**
         * 登录成功后的处理
         * @author admin
         * <p>Date: 2019-08-26 11:36:00</p>
         */
        @Component("browserAuthenticationSuccess")
        public class BrowserAuthenticationSuccess implements AuthenticationSuccessHandler {
        
            private Logger logger = LoggerFactory.getLogger(getClass());
        
            @Autowired
            private ObjectMapper objectMapper;
        
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {
                logger.info("登录成功");
        
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(authentication));
            }
        }
        ```
        
        - 继承SavedRequestAwareAuthenticationSuccessHandler实现多种方式处理登录成功
        ```java
        @Component("browserAuthenticationSuccess")
        public class BrowserAuthenticationSuccess extends SavedRequestAwareAuthenticationSuccessHandler {
        
            private Logger logger = LoggerFactory.getLogger(getClass());
        
            @Autowired
            private ObjectMapper objectMapper;
        
            @Autowired
            private SecurityProperties securityProperties;
        
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {
                logger.info("登录成功");
        
                //判断登录成功后是否返回JSON数据还是跳转页面
                if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(authentication));
                }else {
                    //spring 默认的登录成功处理
                    super.onAuthenticationSuccess(request, response, authentication);
                }
        
            }
        }
        ```
        
    - 自定义登录失败处理
    ```java
    /**
     * 登录失败处理
     * @author admin
     * <p>Date: 2019-08-26 14:51:00</p>
     */
    @Component("browserAuthenticationFailure")
    public class BrowserAuthenticationFailure implements AuthenticationFailureHandler {
    
        private Logger logger = LoggerFactory.getLogger(getClass());
    
        @Autowired
        private ObjectMapper objectMapper;
    
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
    
            logger.info("登录失败");
    
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(exception));
        }
    }
    ```
    
    - 继承SimpleUrlAuthenticationFailureHandler实现多种方式处理登录失败
    ```java
    @Component("browserAuthenticationFailure")
    public class BrowserAuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {
    
        private Logger logger = LoggerFactory.getLogger(getClass());
    
        @Autowired
        private ObjectMapper objectMapper;
    
        @Autowired
        private SecurityProperties securityProperties;
    
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
    
            logger.info("登录失败");
            //判断登录失败是否返回JSON数据还是跳转页面
            if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(exception));
            }else {
                //spring 默认的登录失败处理
                super.onAuthenticationFailure(request, response, exception);
            }
    
    
        }
    }
    ```
    
- 认证源码解读
    - 流程图
    
        ![security-flow](image/security-flow.png)
        
    - 认证处理流程说明(账号密码认证)
    ```
    1. UsernamePasswordAuthenticationFilter 拦截登录请求，获取username和password信息
    2. username和password被获得后封装到一个UsernamePasswordAuthenticationToken（Authentication接口的实例）的实例中
    3. 将UsernamePasswordAuthenticationToken的实例转递到AuthenticationManager进行认证
       3.1  账号密码的AuthenticationManager实现类为ProviderManager
       3.2  实际的认证处理有AuthenticationProvider实现，而AuthenticationManager主要是管理AuthenticationProvider
    4. AuthenticationProvider实现认证逻辑，其中账号密码认证由DaoAuthenticationProvider实现
    5. DaoAuthenticationProvider继承抽象类AbstractUserDetailsAuthenticationProvider，而AbstractUserDetailsAuthenticationProvider实现认证
        5.1 AbstractUserDetailsAuthenticationProvider的authenticate调用子类实现的方法retrieveUser获取用户的实际信息
        5.2 retrieveUser通过调用UserDetailsService的实现类(用户实现)，获取用户信息
    6. authenticate认证用户后校验用户信息(是否禁用，密码是否过期等)
    7. 从新构建UsernamePasswordAuthenticationToken对象返回，该对象包含认证后的用户信息
    8. AbstractAuthenticationProcessingFilter获取到认证信息后，将认证信息保存到上下文中SecurityContext
    9. 调用认证成功的Handler AuthenticationSuccessHandler
        
    ``` 
    - 认证结果如何在多个请求之间共享
    ```
        //AbstractAuthenticationProcessingFilter获取到认证信息后，将认证信息保存到上下文中SecurityContext
    ```
      
    - 获取认证用户信息
    ```
        //controller可通过该方式获取认证信息
        @GetMapping("/me")
        public Object getCurrentUser(Authentication authentication) {
            return authentication;
        }
    ```
- 实现图形验证码

    - 根据随机数生成图片
    - 将随机数缓存
    - 将图片写到响应中
        ```java
        //验证码对象
        @Data
        public class ImageCode {
        
            /**
             * 图形验证码
             */
            private BufferedImage image;
            /**
             * 随机数
             */
            private String code;
            /**
             * 过期时间
             */
            private LocalDateTime expireTime;
        
            public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
                this.image = image;
                this.code = code;
                this.expireTime = expireTime;
            }
        
            /**
             *
             * @param image
             * @param code
             * @param expireIn 过期时间秒
             */
            public ImageCode(BufferedImage image, String code, int expireIn) {
                this.image = image;
                this.code = code;
                this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
            }
        
        }

        //验证码请求
        @RestController
        public class ValidateCodeController {
        
            private static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
        
            //spring 提供操作session的工具类
            private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        
            @GetMapping("/code/image")
            public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
                // 根据随机数生成图片
                ImageCode image = generate(new ServletWebRequest(request));
                // 将随机数缓存
                sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY , image);
                // 将图片写到响应中
                ImageIO.write(image.getImage(), "JPEG", response.getOutputStream());
            }
        
            private ImageCode generate(ServletWebRequest request)  {
                int width = 67;
                int height = 23;
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
                Graphics g = image.getGraphics();
        
                Random random = new Random();
        
                g.setColor(getRandColor(200, 250));
                g.fillRect(0, 0, width, height);
                g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
                g.setColor(getRandColor(160, 200));
                for (int i = 0; i < 155; i++) {
                    int x = random.nextInt(width);
                    int y = random.nextInt(height);
                    int xl = random.nextInt(12);
                    int yl = random.nextInt(12);
                    g.drawLine(x, y, x + xl, y + yl);
                }
        
                String sRand = "";
                for (int i = 0; i < 4; i++) {
                    String rand = String.valueOf(random.nextInt(10));
                    sRand += rand;
                    g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                    g.drawString(rand, 13 * i + 6, 16);
                }
        
                g.dispose();
        
                return new ImageCode(image, sRand, 60);
            }
        
            /**
             * 生成随机背景条纹
             *
             * @param fc
             * @param bc
             * @return
             */
            private Color getRandColor(int fc, int bc) {
                Random random = new Random();
                if (fc > 255) {
                    fc = 255;
                }
                if (bc > 255) {
                    bc = 255;
                }
                int r = fc + random.nextInt(bc - fc);
                int g = fc + random.nextInt(bc - fc);
                int b = fc + random.nextInt(bc - fc);
                return new Color(r, g, b);
            }
        
        }
        
         //需要放行请求验证码路径
         //验证验证码过滤器
        public class ValidateCodeFilter extends OncePerRequestFilter {
        
            private AuthenticationFailureHandler authenticationFailureHandler;
        
            private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                System.out.println("拦截地址： " + request.getRequestURI() + " " + request.getMethod());
                if (StringUtils.equals("/authentication/form", request.getRequestURI())
                        && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
                    try {
                        validate(new ServletWebRequest(request));
                    }catch (ValidateCodeException e){
                        authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                        return;
                    }
                }
        
                filterChain.doFilter(request, response);
            }
        
            private void validate(ServletWebRequest request) throws ServletRequestBindingException {
                ImageCode imageCode = (ImageCode) sessionStrategy.getAttribute(request, "SESSION_KEY_IMAGE_CODE");
        
                //获取提交的验证码
                String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
                if (StringUtils.isBlank(codeInRequest)){
                    throw new ValidateCodeException("验证码不能为空");
                }
        
                if (imageCode == null){
                    throw new ValidateCodeException("验证码不存在");
                }
        
                if (imageCode.isExpried()) {
                    sessionStrategy.removeAttribute(request, "SESSION_KEY_IMAGE_CODE");
                    throw new ValidateCodeException("验证码已过期");
                }
        
                if (!StringUtils.equalsIgnoreCase(imageCode.getCode(), codeInRequest)){
                    throw new ValidateCodeException("验证码不匹配");
                }
                sessionStrategy.removeAttribute(request, "SESSION_KEY_IMAGE_CODE");
            }
        
            public AuthenticationFailureHandler getAuthenticationFailureHandler() {
                return authenticationFailureHandler;
            }
        
            public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
                this.authenticationFailureHandler = authenticationFailureHandler;
            }
        }
  
        //配置验证码过滤器
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
                validateCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailure);
        
                // 验证码过滤器添加到账号密码验证过滤器之前
                http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                        //表单验证
                        .formLogin()
                        //...
            }
        ```
        
- 重构图形验证码接口
    
    - 基本参数可配置
        
        - 配置读取顺序
        ![image-code-config](image/image-code-config.png)
        
        - 默认配置
        ```java
           //多层级配置，可扩展
            @Data
            public class ImageCodeProperties {
            
                /**
                 * 宽度
                 */
                private int width = 67;
                /**
                 * 高度
                 */
                private int height = 23;
                /**
                 * 随机数个数
                 */
                private int length = 4;
                /**
                 * 过期时间，默认60秒
                 */
                private int expireIn = 60;
            }
  
             @Data
             public class ValidateCodeProperties {
             
                 private ImageCodeProperties image = new ImageCodeProperties();
             }
       
             @Data
             @ConfigurationProperties(prefix = "jeff.security")
             public class SecurityProperties {
             
                 private BrowserProperties browser = new BrowserProperties();
             
                 private ValidateCodeProperties code = new ValidateCodeProperties();
             
             }
        ```
        - 应用级配置
        ```yml
        #application.yml
        jeff:
          security:
            browser:
              #登录成功或失败后的处理
              login-type: JSON
            code:
              image:
                length: 6
                width: 100
        ```
        - 请求配置
        ```html
          <tr>
              <td>图形验证码:</td>
              <td>
                  <input type="text" name="imageCode">
                      <img src="/code/image?width=150">
              </td>
          </tr>
        ```
        - 重构接口
        ```java
            //可配置参数
            int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                            securityProperties.getCode().getImage().getWidth());
            int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                            securityProperties.getCode().getImage().getHeight());
            //验证码长度
            for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
             //...
            }
            //过期时间
            new ImageCode(image, sRand, securityProperties.getCode().getImage().getExpireIn());
        ```
        
    - 验证码拦截请求可配置(多个接口可重用)
        
        ````
           //添加可配置属性：ImageCodeProperties
          /**
           * 可配置接口(用‘,’间隔)
           */
          private String url;
          
          //application.yml
          jeff:
            security:
              code:
                image:
                  length: 6
                  width: 100
                  url: /user/*
                  
          //拦截器 ValidateCodeFilter
            @Override
             protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                     throws ServletException, IOException {
                 boolean action = false;
                 for (String url: urls) {
                     if (antPathMatcher.match(url, request.getRequestURI())){
                         action = true;
                     }
                 }
                 if (action) {
                     try {
                         validate(new ServletWebRequest(request));
                     }catch (ValidateCodeException e){
                         authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                         return;
                     }
                 }
         
                 filterChain.doFilter(request, response);
             }
          //
        ````
    - 验证码生成逻辑可配置
    
        - 添加接口
        ```java
            /**
             * 验证码需实现基本接口
             * @author admin
             * <p>Date: 2019-08-27 17:02:00</p>
             */
            public interface ValidateCodeGenerator {
            
                ImageCode generate(ServletWebRequest request);
            
            }
        ```
        - 实现接口
        ```java
            public class ImageCodeGenerator implements ValidateCodeGenerator {
            
                private SecurityProperties securityProperties;
            
                @Override
                public ImageCode generate(ServletWebRequest request) {
                    int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                            securityProperties.getCode().getImage().getWidth());
                    int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                            securityProperties.getCode().getImage().getHeight());
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
                    Graphics g = image.getGraphics();
            
                    Random random = new Random();
            
                    g.setColor(getRandColor(200, 250));
                    g.fillRect(0, 0, width, height);
                    g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
                    g.setColor(getRandColor(160, 200));
                    for (int i = 0; i < 155; i++) {
                        int x = random.nextInt(width);
                        int y = random.nextInt(height);
                        int xl = random.nextInt(12);
                        int yl = random.nextInt(12);
                        g.drawLine(x, y, x + xl, y + yl);
                    }
            
                    String sRand = "";
                    for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
                        String rand = String.valueOf(random.nextInt(10));
                        sRand += rand;
                        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                        g.drawString(rand, 13 * i + 6, 16);
                    }
            
                    g.dispose();
            
                    return new ImageCode(image, sRand, securityProperties.getCode().getImage().getExpireIn());
                }
            
                /**
                 * 生成随机背景条纹
                 */
                private Color getRandColor(int fc, int bc) {
                    Random random = new Random();
                    if (fc > 255) {
                        fc = 255;
                    }
                    if (bc > 255) {
                        bc = 255;
                    }
                    int r = fc + random.nextInt(bc - fc);
                    int g = fc + random.nextInt(bc - fc);
                    int b = fc + random.nextInt(bc - fc);
                    return new Color(r, g, b);
                }
            
                public void setSecurityProperties(SecurityProperties securityProperties) {
                    this.securityProperties = securityProperties;
                }
            }
        ```
        
        - 添加配置选项
        ```java
            @Configuration
            public class ValidateCodeBeanConfig {
            
                @Autowired
                private SecurityProperties securityProperties;
            
                /**
                 * 若提供了其他验证码接口，该验证码接不起作用
                 * 否则默认使用该验证码生成器
                 * @return
                 */
                @Bean
                @ConditionalOnMissingBean(name = "imageCodeGenerator")
                public ValidateCodeGenerator imageCodeGenerator() {
                    ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
                    codeGenerator.setSecurityProperties(securityProperties);
                    return codeGenerator;
                }
            }
        ```
        
        - Controller重构
        ```java
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
        ```
- "记住我"功能

    - 基本原理
    ![remember-me](image/remember-me.png)
        ```
            1. 浏览器发起认证请求
            2. 认证成功后，调用RememberMeService服务生成Token
            3. RememberMeService服务将Token写入浏览器的Cookie中，
              并调用TokenRepository将Token写入数据库
            4. 再次进行服务请求，经过RememberMeAuthenticationFilter过滤器，
                RememberMeAuthenticationFilter 读取Cookie并将Cookie转递给RememberMeService
            5. RememberMeService根据Token查询数据库获取用户名，
              若存在用户名再调用UserDetailsService获取用户信息，并将获取的用户信息存放在ServletContext
     
        ```
    - 具体实现
    
        - 创建TokenRepository配置
        ```
        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
            JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
            tokenRepository.setDataSource(dataSource);
            tokenRepository.setCreateTableOnStartup(true);
            return tokenRepository;
        }
        
         http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                        //表单验证
                        .formLogin()
                        //跳转到指定的controller处理认证逻辑
                        .loginPage("/authentication/require")
                        //通知UsernamePasswordAuthenticationFilter处理登录验证的路径
                        .loginProcessingUrl("/authentication/form")
                        .successHandler(browserAuthenticationSuccess)
                        .failureHandler(browserAuthenticationFailure)
                        .and()
                        //"记住我"的配置
                        .rememberMe()
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                        .userDetailsService(userDetailsService)
                        .and()
                        .authorizeRequests()
                        //匹配可放行页面
                        .antMatchers("/authentication/require",
                                securityProperties.getBrowser().getLoginPage(),
                                "/code/image").permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        //跨站伪造攻击配置
                        .csrf().disable();            
        ```
        
    - 源码分析

- 短信验证码登录

    - 开发短信验证码接口
        - 创建短信验证码Bean ValidateCode
        ```
        //由于ImageCode和ValidateCode只是多了一个属性，所以ImageCode可以继承ValidaeCode
        ```
        - 开发短信验证码生成器 SmsCodeGenerator
        ```java
            @Component
            public class SmsCodeGenerator implements ValidateCodeGenerator {
            
                @Autowired
                private SecurityProperties securityProperties;
            
                @Override
                public ValidateCode generate(ServletWebRequest request) {
                    String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
                    return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
                }
            }
        
            //短信可配置项
            @Data
            public class SmsCodeProperties {
            
                private int length = 6;
                /**
                 * 过期时间，默认60秒
                 */
                private int expireIn = 60;
            
                /**
                 * 可配置接口(用‘,’间隔)
                 */
                private String url;
            }
        ```
        - 短信发送器(可扩展接口)
        ```java
            public interface SmsCodeSender {
            
                void send(String mobile, String code);
            }
  
            public class DefaultSmsCodeSender implements SmsCodeSender {
            
                private Logger logger = LoggerFactory.getLogger(getClass());
            
                @Override
                public void send(String mobile, String code) {
                    logger.info("向手机【{}】发送验证码【{}】", mobile, code);
                }
            }
        ```
        - 配置
        ```java
            @Bean
            @ConditionalOnMissingBean(SmsCodeSender.class)
            public SmsCodeSender smsCodeSender() {
                return new DefaultSmsCodeSender();
            }
        ```
        - 接口调用
        ```java
            @GetMapping("/code/sms")
            public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
                // 随机数生成
                ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
                // 将随机数缓存
                sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY , smsCode);
                // 发送短信
                String mobile = ServletRequestUtils.getStringParameter(request, "mobile");
                smsCodeSender.send(mobile , smsCode.getCode());
            }
        ```
    - 校验短信验证码并登录
    
        - 校验流程: 短信校验应该在短信验证过滤器前校验，类似图形验证码
        ![login-flow](image/login-flow.png)
        
        - SmsCodeAuthenticationFilter
        ```java
        
        ```
        
        - SmsCodeAuthenticationToken
        ```java
        
        ```
        
        - SmsCodeAuthenticationProvider
        ```java
        
        ```
       - 配置SmsCodeAuthenticationConfig
       ```java
       /** 
        * 短信配置配置core模块中为了多个模块重用
        * @author jeff
        * <p>Date 2019/8/28</p>
        */
       @Configuration
       public class SmsCodeAuthenticationConfig extends
               SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
       
           @Autowired
           private AuthenticationSuccessHandler successHandler;
       
           @Autowired
           private AuthenticationFailureHandler failureHandler;
       
           @Autowired
           private UserDetailsService userDetailsService;
       
           @Override
           public void configure(HttpSecurity http) throws Exception {
               SmsCodeAuthenticationFilter smsFilter = new SmsCodeAuthenticationFilter();
               smsFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
               smsFilter.setAuthenticationSuccessHandler(successHandler);
               smsFilter.setAuthenticationFailureHandler(failureHandler);
       
               SmsCodeAuthenticationProvider smsProvider = new SmsCodeAuthenticationProvider();
               smsProvider.setUserDetailsService(userDetailsService);
       
               http.authenticationProvider(smsProvider)
                       .addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class);
           }
       }
       ```
       
       - 添加到spring的认证流程中
       ```java
          @Override
           protected void configure(HttpSecurity http) throws Exception {
               ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
               validateCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailure);
               validateCodeFilter.setSecurityProperties(securityProperties);
               validateCodeFilter.afterPropertiesSet();
       
               //短信验证
               SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
               smsCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailure);
               smsCodeFilter.setSecurityProperties(securityProperties);
               smsCodeFilter.afterPropertiesSet();
       
               http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                       .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                       //表单验证
                       .formLogin()
                       //跳转到指定的controller处理认证逻辑
                       .loginPage("/authentication/require")
                       //通知UsernamePasswordAuthenticationFilter处理登录验证的路径
                       .loginProcessingUrl("/authentication/form")
                       .successHandler(browserAuthenticationSuccess)
                       .failureHandler(browserAuthenticationFailure)
                       .and()
                       //"记住我"的配置
                       .rememberMe()
                       .tokenRepository(persistentTokenRepository())
                       .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                       .userDetailsService(userDetailsService)
                       .and()
                       .authorizeRequests()
                       //匹配可放行页面
                       .antMatchers("/authentication/require",
                               securityProperties.getBrowser().getLoginPage(),
                               "/code/*").permitAll()
                       .anyRequest()
                       .authenticated()
                       .and()
                       //跨站伪造攻击配置
                       .csrf().disable()
                       // 添加短信认证流程到认证流程中
                       .apply(smsCodeAuthenticationConfig);
           }
       ```
        
    - 重构
    
        - 重构后的结构
        ![validate-code-image](image/validate-code-template.png)
        
        - 重构验证码过滤器
        ```java
              //将SmsCodeFilter和ValidateCodeFilter合并
            @Component
            public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
            
                /**
                 * 验证码校验失败处理器
                 */
                @Autowired
                private AuthenticationFailureHandler authenticationFailureHandler;
            
                /**
                 * 系统配置信息
                 */
                @Autowired
                private SecurityProperties securityProperties;
            
                /**
                 * 系统中的校验码处理器
                 */
                @Autowired
                private ValidateCodeProcessorHolder validateCodeProcessorHolder;
            
                /**
                 * 存放所有需要校验验证码的url
                 */
                private Map<String, ValidateCodeType> urlMap = new HashMap<>();
            
            
                private AntPathMatcher antPathMatcher = new AntPathMatcher();
            
                /**
                 * 所有参数完成初始化后，初始化url
                 * @throws ServletException
                 */
                @Override
                public void afterPropertiesSet() throws ServletException {
                    super.afterPropertiesSet();
                    urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
                    addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);
            
                    urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
                    addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
                }
            
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    ValidateCodeType type = getValidateCodeType(request);
                    if (Objects.nonNull(type)) {
                        logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
                        try {
                            validateCodeProcessorHolder.findValidateCodeProcessor(type)
                                    .validate(new ServletWebRequest(request, response));
                            logger.info("验证码校验通过");
                        } catch (ValidateCodeException e) {
                            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                            return;
                        }
                    }
            
                    filterChain.doFilter(request, response);
                }
            
            
            
                /**
                 * 讲系统中配置的需要校验验证码的URL根据校验的类型放入map
                 *
                 * @param urlString
                 * @param type
                 */
                protected void addUrlToMap(String urlString, ValidateCodeType type) {
                    if (StringUtils.isNotBlank(urlString)) {
                        String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
                        for (String url : urls) {
                            urlMap.put(url, type);
                        }
                    }
                }
            
                /**
                 * 获取校验码的类型，如果当前请求不需要校验，则返回null
                 *
                 * @param request
                 * @return
                 */
                private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
                    ValidateCodeType result = null;
                    if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
                        Set<String> urls = urlMap.keySet();
                        for (String url : urls) {
                            if (antPathMatcher.match(url, request.getRequestURI())) {
                                result = urlMap.get(url);
                            }
                        }
                    }
                    return result;
                }
            }
        ```
        
        - 重构配置
        ```java
        //配置分模块，可扩展，易维护
        
            //表达账号密码模块
            public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
            
                @Autowired
                protected AuthenticationSuccessHandler successHandler;
            
                @Autowired
                protected AuthenticationFailureHandler failureHandler;
            
                /**
                 * 密码登录校验配置
                 * @param http
                 * @throws Exception
                 */
                protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
                    http.formLogin()
                            .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                            .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                            .successHandler(successHandler)
                            .failureHandler(failureHandler);
                }
            }
        
            //验证码过滤器
            @Component
            public class ValidateCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
            
                @Autowired
                private Filter validateCodeFilter;
            
                @Override
                public void configure(HttpSecurity http) throws Exception {
                    http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
                }
            }
  
            //短信验证模块
            @Configuration
            public class SmsCodeAuthenticationConfig extends
                    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
            
                @Autowired
                private AuthenticationSuccessHandler successHandler;
            
                @Autowired
                private AuthenticationFailureHandler failureHandler;
            
                @Autowired
                private UserDetailsService userDetailsService;
            
                @Override
                public void configure(HttpSecurity http) throws Exception {
                    SmsCodeAuthenticationFilter smsFilter = new SmsCodeAuthenticationFilter();
                    smsFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
                    smsFilter.setAuthenticationSuccessHandler(successHandler);
                    smsFilter.setAuthenticationFailureHandler(failureHandler);
            
                    SmsCodeAuthenticationProvider smsProvider = new SmsCodeAuthenticationProvider();
                    smsProvider.setUserDetailsService(userDetailsService);
            
                    http.authenticationProvider(smsProvider)
                            .addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class);
                }
            }
      
            //浏览器模块的配置例子
                     //密码登录配置
                    applyPasswordAuthenticationConfig(http);
            
                    //校验码相关验证配置(图形验证码或短信验证码是否匹配)
                    http.apply(validateCodeSecurityConfig)
                            .and()
                            //短信校验配置
                            .apply(smsCodeAuthenticationConfig)
                            .and()
                            //"记住我"的配置
                            .rememberMe()
                            .tokenRepository(persistentTokenRepository())
                            .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                            .userDetailsService(userDetailsService)
                            .and()
                            .authorizeRequests()
                           //匹配不需要认证的请求
                            .antMatchers(
                                    SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                                    SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                                    securityProperties.getBrowser().getLoginPage(),
                                    SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
                            .and()
                            //跨站伪造攻击配置
                            .csrf().disable();
        
        ```
        - 常量SecurityConstants
  
        
#### Spring Social 第三方认证

- OAuth 协议

    - 解决的问题
    - 角色关系
    
        - 服务提供商(provider): 提供令牌
            - 认证服务器(authorization server): 认证用户并产生令牌
            - 资源服务器(resource server): 存储用户数据，并认证令牌
        - 资源所有者(resource owner): 用户
        - 第三方应用(client)
        
    - 流程
    
        ![OAuth](image/OAuth.png) 
        ```
        0. 用户访问第三方应用
        1. 第三方应向用户申请授权
        2. 用户同意授权
        3. 第三方应用向服务提供商申请令牌token
        4. 认证服务器认证用户授权通过，向第三方服务器发放令牌
        5. 第三方应用携带令牌访问资源服务器
        6. 资源服务器认证令牌的有效性，有效则开放访问权限
        ``` 
    - 授权模式(流程步骤2)
        1. 授权码模式(多数)
            ![oauth-code](image/oauth-code.png)
            
            ```
                1. 第三方用户需要用户授权时，第三方应用将用户导向认证服务器
                2. 用户同意授权(认证服务器中完成)
                3. 用户同意授权后，认证服务器会将用户重新导回第三方应用(
                    第三方应用和认证服务器协商跳转回的地址)，并返回授权码
                4. 携带返回的授权码向认证服务器申请令牌(客户端后台完成)
                5. 认证服务器确认授权码有效后，向第三方应用发放令牌
            ```
        2. 简化模式(无服务器时使用)
        3. 密码模式
        4. 客户端模式(少用)
        
- spring social 原理

    - 基本流程
    ![spring social](image/spring-social.png)
    
        ```
        spring social 主要封装了OAuth2的认证流程
        步骤1到步骤5为标准流程
        ```
    - 接口对应的OAuth认证流程角色
    ![spring social interface](image/social-interface.png)
        
        ```
        1. ServiceProvider(默认实现 AbstractOAuth2ServiceProvider)
           服务提供商抽象：
           例如实现QQ登录时可继承AbstractOAuth2ServiceProvider
           
            1.1. OAuth2Operations(默认实现 OAuth2Template) 
                封装了步骤1到步骤5的标准认证流程
                
            1.2. Api( AbstractOAuth2ApiBinding)
                 获取用户信息行为
                 
         2. 步骤7相关的接口类
            2.1 Connection(OAuth2Connection)
                封装用户提供商提供的用户信息
                
            2.2 ConnectionFactory(OAuth2ConnectionFactory)
                通过执行ServiceProvider的流程，获取用户信息
                通过ApiAdapter将获取的用户信息构建Connection
         3. 将客户端的用户与提供商用户(QQ登录)相关联
            3.1 数据库存储相关联表 UserConnection表
            3.2 UsersConnectionRepository(JdbcUsersConnectionRepository)
                操作相关联表
        ```
- QQ登录实现

    - QQ接入文档
    ```
    
    ```
    
    - QQ用户信息Bean
    ```java
        @Data
        public class QQUserInfo {
        
            /**
             * 	返回码
             */
            private String ret;
            /**
             * 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
             */
            private String msg;
            /**
             *
             */
            private String openId;
            /**
             * 不知道什么东西，文档上没写，但是实际api返回里有。
             */
            private String is_lost;
            /**
             * 省(直辖市)
             */
            private String province;
            /**
             * 市(直辖市区)
             */
            private String city;
            /**
             * 出生年月
             */
            private String year;
            /**
             * 	用户在QQ空间的昵称。
             */
            private String nickname;
            /**
             * 	大小为30×30像素的QQ空间头像URL。
             */
            private String figureurl;
            /**
             * 	大小为50×50像素的QQ空间头像URL。
             */
            private String figureurl_1;
            /**
             * 	大小为100×100像素的QQ空间头像URL。
             */
            private String figureurl_2;
            /**
             * 	大小为40×40像素的QQ头像URL。
             */
            private String figureurl_qq_1;
            /**
             * 	大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100×100的头像，但40×40像素则是一定会有。
             */
            private String figureurl_qq_2;
            /**
             * 	性别。 如果获取不到则默认返回”男”
             */
            private String gender;
            /**
             * 	标识用户是否为黄钻用户（0：不是；1：是）。
             */
            private String is_yellow_vip;
            /**
             * 	标识用户是否为黄钻用户（0：不是；1：是）
             */
            private String vip;
            /**
             * 	黄钻等级
             */
            private String yellow_vip_level;
            /**
             * 	黄钻等级
             */
            private String level;
            /**
             * 标识是否为年费黄钻用户（0：不是； 1：是）
             */
            private String is_yellow_year_vip;
        }
    ```
    
    - 创建Api接口并实现该接口
    ```java
        public interface QQ {
        
            /**
             * 获取QQ返回的用户信息
             * @return {@link QQUserInfo}
             */
            QQUserInfo getUserInfo();
        }
      /**
       * AbstractOAuth2ApiBinding 默认的Api实现
       * 
       */
      public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
          /**
           * 获取openId的路径
           */
          private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
      
          /**
           * 获取用户信息的url
           */
          private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
      
          private String appId;
      
          private String openId;
      
          private ObjectMapper objectMapper = new ObjectMapper();
      
          public QQImpl(String accessToken, String appId) {
              super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
      
              this.appId = appId;
      
              String url = String.format(URL_GET_OPENID, accessToken);
              String result = getRestTemplate().getForObject(url, String.class);
      
              System.out.println(result);
      
              this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
          }
      
          @Override
          public QQUserInfo getUserInfo() {
              String url = String.format(URL_GET_USERINFO, appId, openId);
              String result = getRestTemplate().getForObject(url, String.class);
      
              System.out.println(result);
      
              QQUserInfo userInfo = null;
              try {
                  userInfo = objectMapper.readValue(result, QQUserInfo.class);
                  userInfo.setOpenId(openId);
                  return userInfo;
              } catch (Exception e) {
                  throw new RuntimeException("获取用户信息失败", e);
              }
          }
      }

    ```
    
    - 开发ServiceProvider
    ```java
        public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
        
            private String appId;
        
            /**
             * 认证服务器地址
             */
            private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
        
            /**
             * 申请令牌的地址
             */
            private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
        
            public QQServiceProvider(String appId, String appSecret) {
                //OAuth2Template 是spring 默认的OAuth2Operations实现
                super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
                this.appId = appId;
            }
        
            @Override
            public QQ getApi(String accessToken) {
                //QQImpl 需要多实例，每个用户的accessToken不同
                return new QQImpl(accessToken, appId);
            }
        }
    ```
    
    - 开发Adapter：qq提供的用户数据和spring的标准数据结构间进行适配
    ```java
        public class QQAdapter implements ApiAdapter<QQ> {
        
            @Override
            public boolean test(QQ api) {
                return true;
            }
        
            @Override
            public void setConnectionValues(QQ api, ConnectionValues values) {
                QQUserInfo userInfo = api.getUserInfo();
        
                values.setDisplayName(userInfo.getNickname());
                values.setImageUrl(userInfo.getFigureurl_qq_1());
                //个人主页
                values.setProfileUrl(null);
                //服务商的用户id
                values.setProviderUserId(userInfo.getOpenId());
            }
        
            @Override
            public UserProfile fetchUserProfile(QQ api) {
                return null;
            }
        
            @Override
            public void updateStatus(QQ api, String message) {
        
            }
        }
    ```
    
    - ConnectionFactory
    ```java
        public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
        
        
        
            public QQConnectionFactory(String providerId, String appId, String appSecret) {
                super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
            }
        }
    ```
    
    - 配置
    ````java
        @Configuration
        @EnableSocial
        public class SocialConfig extends SocialConfigurerAdapter {
        
            @Autowired
            private SecurityProperties securityProperties;
        
            @Autowired
            private DataSource dataSource;
        
            /**
             * 数据库配置
             * @param connectionFactoryLocator
             * @return
             */
            @Override
            public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
                JdbcUsersConnectionRepository jdbcRepository = new JdbcUsersConnectionRepository(dataSource,
                        connectionFactoryLocator, Encryptors.noOpText());
                jdbcRepository.setTablePrefix("t_");
        
                return jdbcRepository;
            }
        
            /**
             * 第三方登录拦截路径配置
             * @return
             */
            @Bean
            public SpringSocialConfigurer springSocialConfigurer() {
                String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
                return new MySpringSocialConfigurer(filterProcessesUrl);
            }
        }

    ````
    
    - 根据UserId转为用户信息的配置
    ```java
        @Service
        public class UserDetailServiceImpl implements UserDetailsService, SocialUserDetailsService {
        
            private Logger logger = LoggerFactory.getLogger(getClass());
        
            @Autowired
            private PasswordEncoder passwordEncoder;
        
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                logger.info("表单登录用户名:" + username);
                return buildUser(username);
            }
        
            @Override
            public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
                logger.info("设计登录用户Id:" + userId);
                return buildUser(userId);
            }
        
            private SocialUserDetails buildUser(String userId) {
                // 根据用户名查找用户信息
                //根据查找到的用户信息判断用户是否被冻结
                String password = passwordEncoder.encode("123456");
                logger.info("数据库密码是:"+password);
                return new SocialUser(userId, password,
                        true, true, true, true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
            }
        
        }
    ```
    
    - 配置 appId等
    ```java
        @Data
        public class QQProperties  extends SocialProperties {
        
            private String providerId = "qq";
        }
      @Data
      public class SocialProperties {
      
          private String filterProcessesUrl = "/auth";
      
          private QQProperties qq = new QQProperties();
      }
    
        @Data
        @ConfigurationProperties(prefix = "jeff.security")
        public class SecurityProperties {
        
           //...
        
            private SocialProperties social = new SocialProperties();
        
        }
      
          @Configuration
          //有相应的properties配置时，该类才启用
          @ConditionalOnProperty(prefix = "jeff.security.social.qq", name = "app-id")
          public class QQAutoConfig extends SocialAutoConfigurerAdapter {
          
              @Autowired
              private SecurityProperties properties;
          
              @Override
              protected ConnectionFactory<?> createConnectionFactory() {
                  QQProperties qq = properties.getSocial().getQq();
                  return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
              }
          }
        
        
        // 最后将social的配置添加到应用的过滤链中
             @Autowired
            private SpringSocialConfigurer springSocialConfigurer;
      
          http.apply(validateCodeSecurityConfig)
                      .and()
                      //短信校验配置
                      .apply(smsCodeAuthenticationConfig)
                      .and()
                      //添加social认证配置
                      .apply(springSocialConfigurer)
                    //...
    ```
    
    - 自定义Auth2Template的实现，解决返回类型无法处理问题
    ```java
        public class QQOAuth2Template extends OAuth2Template{
        
            public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
                super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
            }
        
            /**
             * OAuth2Template 默认获取的返回类型是json格式的，但qq返回的参数是字符串格式并通过&分割
             * @param accessTokenUrl
             * @param parameters
             * @return
             */
            @Override
            protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
                String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        
                String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");
        
                String accessToken = StringUtils.substringAfterLast(items[0], "=");
                Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
                String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        
                return new AccessGrant(accessToken, null, refreshToken, expiresIn);
            }
        
            /**
             * spring 默认的OAuth2Template没有设置处理服务商返回的text/html请求
             * @return
             */
            @Override
            protected RestTemplate createRestTemplate() {
                RestTemplate restTemplate = super.createRestTemplate();
                //添加处理text/html的转换器
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
                return restTemplate;
            }
        }
    ```
    
    - 处理注册页
    ```java
    
    ```
    
- 绑定解绑

    - /connect
   ```
    spring 提供获取所有绑定信息的服务,ConnectController
    ```

#### 授权


#### 攻击防护(伪造用户身份)


#### 配置(extends WebSecurityConfigurerAdapter)

1. 表单认证
    ```java
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                //表单验证
                http.formLogin()
                        .and()
                        .authorizeRequests()
                        .anyRequest()
                        .authenticated();
            }
    ```

### Session管理 
#### Session超时处理

#### Session并发控制
#### 集群Session管理


### spring security oauth2

#### 架构图

- 整体结构
    ![spring-security-oauth](image/spring-security-oauth.png)

- 核心源码
    
    ![spring-security-oauth-src](image/spring-security-oauth-src.png)
    
    - 说明
    ```
    TokenEndpoint: 处理获取token的请求(/oauth/token), 判断请求属于哪种授权模式
    
    ClientDetailsService： 默认实现InMemoryClientDetailsService，读取第三方应用的信息
    
    ClientDetails: 封装第三方应用的配置信息, eg: clientId
    
    TokenRequest: 由TokenEndpoint创建，封装请求其他的请求信息(eg.: grantType), 
                  也同时封装了ClientDetails的信息
                  //TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
                  
    TokenGranter: 根据TokenRequest调用TokenGranter接口，该接口封装了4种授权模式的实现，并根据TokenRequest的grantType
                  属性选择具体的授权模式实现token的生成逻辑
                  
    OAuth2Request: ClientDetails和TokenRequest的信息整合
    
    Authentication: 授权用户的信息
    
    OAuth2Authentication: 包含了第三方应用、用户的授权、授权模式和授权的参数信息
    
    AuthorizationServerTokenServices: 认证服务器的令牌服务
    TokenStore:  令牌存储
    TokenEnhancer: 令牌增强器
    ```
    
- 源码分析

    - 处理获取令牌的请求 TokenEndpoint
    ```java
        @RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
    	public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
    	Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
    
    		if (!(principal instanceof Authentication)) {
    			throw new InsufficientAuthenticationException(
    					"There is no client authentication. Try adding an appropriate authentication filter.");
    		}
    
    		String clientId = getClientId(principal);
  		    //将获取第三方应用的信息
    		ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);
    		//根据第三方应用和其他请求的参数的信息创建TokenRequest
    		TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
    
  		    //验证clientId
    		if (clientId != null && !clientId.equals("")) {
    			// Only validate the client details if a client authenticated during this
    			// request.
    			if (!clientId.equals(tokenRequest.getClientId())) {
    				// double check to make sure that the client ID in the token request is the same as that in the
    				// authenticated client
    				throw new InvalidClientException("Given client ID does not match authenticated client");
    			}
    		}
  		    
  		    //一序列验证信息
    		if (authenticatedClient != null) {
    			oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
    		}
    		if (!StringUtils.hasText(tokenRequest.getGrantType())) {
    			throw new InvalidRequestException("Missing grant type");
    		}
    		if (tokenRequest.getGrantType().equals("implicit")) {
    			throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
    		}  
  		
  		    //判断是否为授权码模式，若是则清除scope
    		if (isAuthCodeRequest(parameters)) {
    			// The scope was requested or determined during the authorization step
    			if (!tokenRequest.getScope().isEmpty()) {
    				logger.debug("Clearing scope of incoming token request");
    				tokenRequest.setScope(Collections.<String> emptySet());
    			}
    		}
    		
  		    //判断是否为refresh token请求
    		if (isRefreshTokenRequest(parameters)) {
    			// A refresh token has its own default scopes, so we should ignore any added by the factory here.
    			tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
    		}
    
  		    //核心代码，调用TokenGranter生成OAuth2AccessToken
    		OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
    		if (token == null) {
    			throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
    		}
    
    		return getResponse(token);
    
    	}
    ```
    
    - CompositeTokenGranter 生成OAuth2AccessToken
    ```java
        public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
            //4种授权模式 + refresh token模式的实现
            for (TokenGranter granter : tokenGranters) {
                OAuth2AccessToken grant = granter.grant(grantType, tokenRequest);
                if (grant!=null) {
                    return grant;
                }
            }
            return null;
        }
    ```
    
    - AbstractTokenGranter 生成OAuth2AccessToken的模板代码
    ```java
    
          public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
            //判断是哪种授权模式
    		if (!this.grantType.equals(grantType)) {
    			return null;
    		}
    		
    		String clientId = tokenRequest.getClientId();
    		ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
    		validateGrantType(grantType, client);
    		
    		logger.debug("Getting access token for: " + clientId);
    
    		return getAccessToken(client, tokenRequest);
    
    	}
  	
  	    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
            //根据ClientDetails和tokenRequest创建OAuth2Authentication
            //OAuth2Authentication封装了授权相关信息
    		return tokenServices.createAccessToken(getOAuth2Authentication(client, tokenRequest));
    	}
    ```
    
    - ResourceOwnerPasswordTokenGranter：密码模式获取OAuth2Authentication
    ```java
        @Override
    	 protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
    
    		Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
    		String username = parameters.get("username");
    		String password = parameters.get("password");
    		// Protect from downstream leaks of password
    		parameters.remove("password");
    
    		Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
    		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
    		try {
    			userAuth = authenticationManager.authenticate(userAuth);
    		}
    		catch (AccountStatusException ase) {
    			//covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
    			throw new InvalidGrantException(ase.getMessage());
    		}
    		catch (BadCredentialsException e) {
    			// If the username/password are wrong the spec says we should send 400/invalid grant
    			throw new InvalidGrantException(e.getMessage());
    		}
    		if (userAuth == null || !userAuth.isAuthenticated()) {
    			throw new InvalidGrantException("Could not authenticate user: " + username);
    		}
    		
    		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);		
    		return new OAuth2Authentication(storedOAuth2Request, userAuth);
    	}
    ```
    
    - DefaultTokenServices具体的token生成逻辑( AuthorizationServerTokenServices的默认实现类)
    ```java
        @Transactional
        public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
    
            //查看用户是否已发送token信息
            OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
            OAuth2RefreshToken refreshToken = null;
            if (existingAccessToken != null) {
                //判断是否过期
                if (existingAccessToken.isExpired()) {
                    //令牌过期，需删除token相关信息
                    if (existingAccessToken.getRefreshToken() != null) {
                        refreshToken = existingAccessToken.getRefreshToken();
                        // The token store could remove the refresh token when the
                        // access token is removed, but we want to
                        // be sure...
                        tokenStore.removeRefreshToken(refreshToken);
                    }
                    tokenStore.removeAccessToken(existingAccessToken);
                }
                else {
                    // Re-store the access token in case the authentication has changed
                    //重新存储token，授权模式可能改变了
                    tokenStore.storeAccessToken(existingAccessToken, authentication);
                    return existingAccessToken;
                }
            }
    
            // Only create a new refresh token if there wasn't an existing one
            // associated with an expired access token.
            // Clients might be holding existing refresh tokens, so we re-use it in
            // the case that the old access token
            // expired.
            // 创建刷新令牌
            if (refreshToken == null) {
                refreshToken = createRefreshToken(authentication);
            }
            // But the refresh token itself might need to be re-issued if it has
            // expired.
            else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
                if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                    refreshToken = createRefreshToken(authentication);
                }
            }
    
            OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
            tokenStore.storeAccessToken(accessToken, authentication);
            // In case it was modified
            refreshToken = accessToken.getRefreshToken();
            if (refreshToken != null) {
                tokenStore.storeRefreshToken(refreshToken, authentication);
            }
            return accessToken;
    
        }
      
        private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
            //根据UUID生成令牌信息
      		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
    		
    		//设置令牌的其他属性
      		int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
      		if (validitySeconds > 0) {
      			token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
      		}
      		token.setRefreshToken(refreshToken);
      		token.setScope(authentication.getOAuth2Request().getScope());
      
    		//判断是否有增强器，有则使用增强器对令牌进行增强(添加某些参数)
      		return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
      	}
    ```
 
 - Token 处理
 
    - 基本的Token参数配置
    
    - 使用JWT   
    