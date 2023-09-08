package spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {
    @GetMapping
    public String home(Model model) {
        System.out.println("请求收到了...正在处理中");
        model.addAttribute("thymeleafURLDesc", new String[] {
                "th:href是一个修饰符属性：处理后，它将计算要使用的链接 URL，并将该值设置为标记的属性。href<a>",
                "我们可以对 URL 参数使用表达式（如 中所示）。所需的 URL 参数编码操作也将自动执行。orderId=${o.id}",
                "如果需要多个参数，则这些参数将用逗号分隔：@{/order/process(execId=${execId},execType='FAST')}",
                "URL 路径中也允许使用变量模板：@{/order/{orderId}/details(orderId=${orderId})}",
                "以 （例如： ） 开头的相对 URL 将自动以应用程序上下文名称为前缀。//order/details",
                "如果未启用 Cookie 或尚不清楚，则可以向相对 URL 添加后缀，以便保留会话。这称为 URL 重写，Thymeleaf 允许您使用 Servlet API 中针对每个 URL 的机制插入自己的重写过滤器。\";jsessionid=...\" response.encodeURL(...)",
                "该属性允许我们（可选地）在模板中具有一个有效的静态属性，以便我们的模板链接在直接打开以进行原型设计时仍可由浏览器导航。"
        });
        return "home";
    }
}
