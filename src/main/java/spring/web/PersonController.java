package spring.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import spring.data.Person;

import javax.annotation.Nonnull;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
@RequestMapping("/persons")
// 演示过程，首先访问/persons/sessionAttribute，此时会在全局注册一个Model的person属性，
// 接着由于使用了@SessionAttributes("person")注解，所以会把key为person的Model数据(相当于session.addAttribute(model.getAttribute("person")))提升至Session级别
//@SessionAttributes("person")
public class PersonController {

    private HttpServletRequest request;

    @Autowired(required = false)
    public void setRequest(HttpServletRequest request) { this.request = request; }

    @GetMapping
    public String handlePerson(Model model) {
        model.addAttribute(new Person());
        return "persons";
    }

    @GetMapping("/httpHeaders")
    public HttpHeaders handlePersonReturnHttpHeaders() {
        return new HttpHeaders();
    }

    @GetMapping("/view")
    public View handlePersonReturnView() {
        return new View() {
            private final String contentType = "text/plain";

            @Override public String getContentType() { return contentType; }
            @Override public void render(Map<String, ?> model, @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response) throws Exception {
                response.addHeader("Content-Type", contentType);
                PrintStream out = new PrintStream(response.getOutputStream());
                out.println("请求方法返回一个view");
                out.println(request.getRequestURI());
                out.println(request.getRequestURL());
                out.close();
            }
        };
    }

    @GetMapping("/{id}")
    public void getPerson(@PathVariable long id, Model model) {
        System.out.println("请求路径为：/persons/" + id);
        System.out.printf("@ModelAttribute注解在全局中执行，model值 -> %s", model.getAttribute("person"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(Person person) {
        System.out.println(person);
    }

    @GetMapping("/owners/{ownerId}/pets/{petId}")
    public void showPet(@PathVariable long ownerId, @PathVariable long petId) {
        System.out.printf("/owners/%d/pets/%d%n", ownerId, petId);
        System.out.println("Success!");
    }

    @GetMapping(value = "/{name:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{ext:\\.[a-z]+}")
    public void handle(@PathVariable String name, @PathVariable String version, @PathVariable String ext) {
        System.out.printf("匹配/persons/spring-web-3.0.5.jar -> %s%s%s%n", name, version, ext);
    }

    // 矩阵变量
    // 当需要使用带有矩阵的URL格式(http://localhost:8080/persons/matrix/22;q=33;h=11)时，需要在Spring配置文件启用支持矩阵URL模式
    // <mvc:annotation-driven enable-matrix-variables="true" />
    // 矩阵模式和普通的带有请求参数GET请求不同，普通的请求参数一个key只能对应一个value，而矩阵URL模式一个key可以对应多个value
    // 如：http://localhost:8080/persons/matrix/22;q=33,44,55;h=11
    // 当获取所有矩阵变量时，可以使用类型MultiValueMap<String, String>
    @GetMapping("/matrix/{id}")
    public void handleMatrix(@PathVariable long id, @MatrixVariable MultiValueMap<String, String> matrixVariables) {
        System.out.printf("获取到的Matrix为：@PathVariable == %s, @MatrixVariable == %s%n", id, matrixVariables);
    }

    // 分别获取id，解决歧义，因为ownerId和petId是不同的，@MatrixVariable(name="q", pathVar="ownerId || petId")
    // http://localhost:8080/persons/matrix/owners/42;q=11/pets/21;q=22
    @GetMapping("/matrix/owners/{ownerId}/pets/{petId}")
    public void handleMatrixFindPet(
            @PathVariable long ownerId,
            @PathVariable long petId,
            @MatrixVariable(name="q", pathVar="ownerId") int q1,
            @MatrixVariable(name="q", pathVar="petId") int q2
    ) {
        System.out.printf("@MatrixVariable(name=\"q\", pathVar=\"ownerId\") == %s, @MatrixVariable(name=\"q\", pathVar=\"petId\") == %s%n", q1, q2);
    }

    // 矩阵变量可以定义为可选变量，并指定默认值。如果矩阵模式的URL中没有q的参数，则q的值为defaultValue的值，即: q == 1
    @GetMapping("/matrix/pets/{petId}")
    public void handleMatrixFindPetDefaultValue(@PathVariable long petId, @MatrixVariable(required = false, defaultValue = "1" ) int q) {
        System.out.printf("Does the matrix URL have a q parameter? %b%n", q == 1);
    }

    // http://localhost:8080/persons/requestParam?param=requestParam，则参数param == requestParam
    // 如果接收的参数类型不是String，则在Spring内部会自动进行转换
    // 例如：获取请求参数的数组，则请求URL为 -> http://localhost:8080/persons/requestParam?param=requestParam1&param=requestParam2&param=requestParam3
    // 那么结果为param == [requestParam1, requestParam2, requestParam3]
    // 如果使用param[]=requestParam1&param[]=requestParam2&param[]=requestParam3这种格式的URL(需要对[]进行转义)时，则@RequestParam("param[]")进行获取数组值
    // 当使用@RequestParam注解时，请求的参数(@RequestParam注解的value值)不是必须的，可以设置@RequestParam(required = false)或接收的参数为Optional，两者等价
    // 当使用@RequestHeader注解时，如果获取的请求头的某一项不存在时，则这个方法不会得到任何执行。如：@RequestHeader("Keep-Alive")，如果请求头中不存在Keep-Alive，则方法不会执行
    @GetMapping("/requestParam")
    public void handleRequestParam(
            @RequestParam("param") Optional<String[]> param,
            @RequestHeader("Accept-Encoding") String encoding,
            @RequestHeader("Connection") String connection,
            @CookieValue("JSESSIONID") String cookie
    ) {
        // 当有param请求参数时，会正常输出Get request parameters -> [ ... ]
        // 否则会输出Get request parameters -> [ "defaultValue1", "defaultValue2", "defaultValue3" ]
        String[] defaultValues = new String[]{ "defaultValue1", "defaultValue2", "defaultValue3" };
        System.out.printf("Get request parameters -> %s%n", Arrays.toString(param.orElse(defaultValues)));
        System.out.printf("Get request header -> Accept-Encoding: %s; Connection: %s%n", encoding, connection);
        System.out.printf("Get request cookie -> %s%n", cookie);
    }

    /*
    * @ModelAttribute注解用于将方法的参数或方法的返回值绑定到指定的模型属性上，并返回给Web视图，相当于执行model.addAttribute(name, value)
    * 有两种用法，用在方法上和用在参数上。
    * 当用在方法上时会在这个Controller每个方法执行前被执行(相当于全局执行model.addAttribute(name, value)方法)
    * 当用在方法参数上则不会这样，这样就不必在不必要的访问路径中进行填充数据，以免浪费资源。
    * 用在方法参数上时，也会执行数据绑定功能(即：请求参数名称和attribute值的字段相匹配，匹配之后就可以为实例字段进行赋值)
    *
    * 示例：用在返回值为void的方法
    * @ModelAttribute
      public void populateModel(@RequestParam String abc, Model model) {
         model.addAttribute("attributeName", abc);
      }

      @GetMapping(value = "/helloWorld")
      public String helloWorld() {
         return "helloWorld";
      }
    *
    * 用在有返回值的方法：(返回的数据的名字和model.addAttribute方法没有key值的规则一样，如下面的生成为model.addAttribute("account", accountManager.findAccount(number)))
    * @ModelAttribute
      public Account addAccount(@RequestParam String number) {
          return accountManager.findAccount(number);
      }
    *
    * 用在方法参数上：
    * @GetMapping("/postModelAttribute")
      public String handleCreateModelPerson(@ModelAttribute Person person) {
          return "postModelAttribute";
      }

    * 组合使用：(返回的字符串hi并不是jsp视图名称，而是Model的属性值，相当于在访问/helloWorld.do之前，执行model.addAttribute("attributeName", hi))
    * @RequestMapping(value = "/helloWorld.do")
      @ModelAttribute("attributeName")
      public String helloWorld() {
         return "hi";
      }

    * @ModelAttribute(binding = false)以禁用数据绑定
    * 示例URL：http://localhost:8080/persons/postModelAttribute?name=father&age=-1&email=lala@163.com
    * 在Person类中，age设置了验证，最小为1，最大为100，URL中的请求参数age为-1，显然不符合正常范围，所以在数据绑定会出现一个异常
    * */
    @GetMapping("/postModelAttribute")
    public String handleCreateModelPerson(@Valid @ModelAttribute Person person, BindingResult result) {
        if (result.hasErrors()) {
            System.out.printf("target: %s, ", Objects.requireNonNull(result.getTarget()).getClass().getName());
            result.getAllErrors().forEach(error -> System.out.printf("errorMessage: %s%n", error.getDefaultMessage()));
        }

        return "postModelAttribute";
    }

//    @GetMapping("/sessionAttribute")
//    @ModelAttribute
//    public Person handleSessionAttribute() {
//        return new Person();
//    }

    // 无法使用@RequestBody Person person，因为没有将请求体转为JavaBean的HttpMessageConverter接口实现类
    // 这里可以传入String类型是因为Spring MVC框架默认实现了StringHttpMessageConverter，此类支持将请求体转换为String
    // 默认使用WebDataBinder(通过请求参数绑定到JavaBean的字段上)只支持基础类型，如：int、String...
    // 像下面，如果想接收postModelAttribute.jsp中表单的date类型的input，则会接收为基础类型(String)，那么通过参数类型Date就不符合
    // 解决办法通过使用@InitBinder注解提前注册一个字段值的转换器(Converter)，当通过接收一个Date类型的参数时，就会有一个可以处理的转换器，从而可以正确的匹配Controller方法
    @PostMapping("/postModelAttribute")
    public ResponseEntity<String> handlePostModelAttribute(
            Date date,
            HttpEntity<String> entity,
            @CookieValue(value = "JSESSIONID", required = false) String cookie,
            @RequestBody(required = false) String requestBody
    ) throws JsonProcessingException {
        entity.getHeaders()
                .forEach((key, val) -> System.out.printf("%s: %s%n", key, StringUtils.replaceChars(val.toString(), "[]", "")));
        System.out.println("Body: " + entity.getBody());
        System.out.println("Get Cookie -> " + cookie);
        System.out.println("Get RequestBody -> " + requestBody);
        System.out.println("Get Date -> " + date);

        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setName("霸霸");
        person.setAge(22);
        person.setEmail("koko@163.com");
        person.setDate(date.toString());

        String json = mapper.writeValueAsString(person);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Length", String.valueOf(bytes.length));
        headers.add("ETag", Base64.getEncoder().encodeToString(bytes)); // Last-Modified由于精确度比 ETag 要低，所以这是一个备用机制
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        /*
        * Last-Modified示例：Last-Modified: Wed, 21 Oct 2015 07:28:00 GMT
        * 等价于使用SimpleDateFormat，只不过支持Java8以上版本，DateTimeFormatter的格式字符说明和SimpleDateFormat差不多，只不过比SimpleDateFormat更强大一些，多了一些字符
        * SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", DateFormatSymbols.getInstance(Locale.US) || Locale.US);
          sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
          sdf.format(new Date())
        * 注：对POST请求貌似无效
        * */
        headers.add(
                "Last-Modified",
                LocalDateTime.of(2022, 8, 14, 19, 13, 54)
                        .atZone(ZoneId.of("GMT"))
                        .format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US)));
        headers.add("Cache-Control", "max-age=3600");

        return ResponseEntity.ok().headers(headers).body(json);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, false));
        // 或者使用实现Formatter接口的类
        // binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(
            Exception ex,
            HandlerMethod method,
            WebRequest webRequest,
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            HttpSession session,
            HttpMethod httpMethod,
            Locale locale,
            TimeZone zone,
            ZoneId zoneId,
            OutputStream out,
            Model model,
            RedirectAttributes redirectAttributes,
            @SessionAttribute(required = false) Person sessionPerson,
            @RequestAttribute(required = false) Person requestPerson
    ) {
        System.out.println(ex.getCause().toString());
        printExceptionHandlerArgsMethod(
                new HashMap<Class<?>, Object>() {{
                    put(Exception.class, ex);
                    put(HandlerMethod.class, method);
                    put(WebRequest.class, webRequest);
                    put(ServletRequest.class, servletRequest);
                    put(ServletResponse.class, servletResponse);
                    put(HttpSession.class, session);
                    put(HttpMethod.class, httpMethod);
                    put(Locale.class, locale);
                    put(TimeZone.class, zone);
                    put(ZoneId.class, zoneId);
                    put(OutputStream.class, out);
                    put(Model.class, model);
                    put(RedirectAttributes.class, redirectAttributes);
                    put(SessionAttribute.class, sessionPerson);
                    put(RequestAttribute.class, requestPerson);
                }}
        );

        // UriComponentsBuilder帮助从包含变量的 URI 模板构建 URI
        URI uri =
                UriComponentsBuilder.fromUriString("https://example.com/hotels/{hotel}")
                        .queryParam("q", "{q}")
                        .encode()
                        .build("Westin", 123);
        System.out.println(uri);
        return ResponseEntity.badRequest().body("出错啦");
    }

    private void printExceptionHandlerArgsMethod(Map<Class<?>, Object> map) {
        StringBuilder builder = new StringBuilder("@ExceptionHandler注解所注释的方法接收的所有参数为：\n");
        List<Class<?>[]> classList = new ArrayList<>();
        List<Method> methods = new ArrayList<>();

        for (Method method: this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                classList.add(method.getParameterTypes());
                methods.add(method);
                break;
            }
        }

        if (classList.size() > 0) {
            for (Class<?>[] classes: classList)
                for (Class<?> cls: classes)
                    builder.append("\t").append(cls.getSimpleName()).append(" -> ").append(map.get(cls)).append("\n");
            for (Method method: methods)
                for (Annotation[] annotations: method.getParameterAnnotations())
                    if (annotations.length > 0) {
                        for (Annotation annotation: annotations)
                            builder.append("\t@")
                                    .append(annotation.getClass().getSimpleName())
                                    .append(": ")
                                    .append(map.get(annotation.getClass()))
                                    .append("\n");
                    }
        }
        System.out.println(builder);
    }

    @GetMapping("/uriComponents/{uriComponentsId}")
    public void handle(@PathVariable long uriComponentsId) {
        UriComponents uriComponents =
                MvcUriComponentsBuilder.fromMethodName(PersonController.class, "handle", (long) 21)
                        .buildAndExpand(42);
        URI uri = uriComponents.encode().toUri();
        System.out.println("MvcUriComponentsBuilder -> " + uri);    // http://localhost:8080/persons/uriComponents/21

        URI servletUriComponentsUri =
                ServletUriComponentsBuilder.fromRequest(request)
                        .replaceQueryParam("accountId", "{id}")
                        .build("123");
        System.out.println("ServletUriComponentsBuilder -> " + servletUriComponentsUri);    // http://localhost:8080/persons/uriComponents/33?accountId=123

        URI contextPathUri = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/uriComponentses")
                .build()
                .toUri();
        System.out.println("ServletUriComponentsBuilder.fromContextPath -> " + contextPathUri); // http://localhost:8080/uriComponentses
    }

    private static final Map<Long, SseEmitter> responseBodyEmitterMap = new HashMap<>();
    @GetMapping(value = "/{id}/rbe") //1
    public ResponseEntity<ResponseBodyEmitter> responseBodyEmitter(@PathVariable Long id){
        SseEmitter emitter = new SseEmitter();
        responseBodyEmitterMap.put(id, emitter);
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(emitter); //2

    }

    @GetMapping("/{id}/invoke-rbe") //3
    public void invokeResponseBodyEmitter(@PathVariable Long id) throws Exception {
        ResponseBodyEmitter emitter = responseBodyEmitterMap.get(id); //4
        emitter.send("Hello World", MediaType.TEXT_PLAIN); //5
        Thread.sleep(3000);
        emitter.send(new Person("john", 35, "lala@163.com"), MediaType.APPLICATION_JSON);
        Thread.sleep(3000);
        emitter.send(new Person("john2", 32, "lala@163.com"), MediaType.APPLICATION_JSON);
    }

    @GetMapping("/{id}/close-rbe") //6
    public void closeResponseBodyEmitter(@PathVariable Long id) {
        ResponseBodyEmitter emitter = responseBodyEmitterMap.get(id);
        emitter.complete(); //7
        responseBodyEmitterMap.remove(id);
    }
}
