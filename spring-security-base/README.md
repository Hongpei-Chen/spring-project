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
            
                String message() default "{org.jeff.spring.security.demo.validator.IPValid.message}";
            
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
            
                @Around("execution(* org.jeff.spring.security.demo.controller.UserController.*(..))")
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