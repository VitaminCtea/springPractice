package spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import spring.data.Spittle;
import spring.repository.imp.SpittleRepositoryImp;

import java.util.List;

@Controller
@RequestMapping("/spittles")
public class SpittleController {
    private static final String MAX_LONG_AS_STRING = "9223372036854775807";
    private final SpittleRepositoryImp spittleRepositoryImp;
    @Autowired
    public SpittleController(SpittleRepositoryImp spittleRepositoryImp) { this.spittleRepositoryImp = spittleRepositoryImp; }

    @RequestMapping(method = RequestMethod.GET)
    public List<Spittle> spittles(
            @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
            @RequestParam(value = "count", defaultValue = "20") int count)
    {
        /*
        * Model传值有三种方式：
        *   1.参数类型也可以替换为Map类型，model.put("spittleList", spittleRepository.findSpittles(Long.MAX_VALUE, 20))效果是一样的
        *   2.Model实际是一个Map，它会传递给视图，这样数据就能渲染到客户端，public String spittles(Map model) { ... }
        *   3.也可以直接返回数据，public String spittles(Model model) { return spittleRepository.findSpittles(Long.MAX_VALUE, 20); }
        *     当处理器方法像这样返回对象或集合时，这个值会放到模型中，模型的key会根据其类型推断得出，而逻辑试图的名称将会根据请求路径推断得出。
        *     因为这个方法处理针对"/spittles"的GET请求，因此视图的名称将会是spittles(去掉开头的斜线)
        *     无论选择哪种方式来编写spittles()方法，所达成的结果都是相同的。
        *     模型会存储一个Spittle列表，key为spittleList，然后这个列表会发送到名为spittles的视图中。
        *
        * 如果addAttribute不给key值的话，默认的命名规则是：
        * 如果值是一个Collection，则会查看第一个元素的类型，然后根据元素的名字和实际的类型进行组合，例如此程序就是spittleList(限于类)
        * 如果值是一个数组的话，则会查看数组元素的类型，调用getComponentType方法查看是什么类型，然后会根据(类型 + List = 最终名称)
        * 如果只是一个类的话，则直接获取全限定名称即可
        * */
        // model.addAttribute(new ArrayList<int[]>(){{ add(new int[]{ 1, 2 }); }});
        return spittleRepositoryImp.findSpittles(max, count);
    }

    /*
    * 匹配/spittles URL路径下的子路径，如：/spittles/12345 -> 匹配/12345，/spittles/54321 -> 匹配/54321
    * @PathVariable注解作用是URL中的变量传递到处理器方法的参数中，使其处理器方法能拿到可变的URL路径
    * 当 @PathVariable注解修饰的处理器方法参数和URL可变的路径名是一样时，则可以省略@PathVariable注解的value值，表明默认修饰的处理器方法参数的名称和URL可变的URL路径名称一致
    * 如：
    * @RequestMapping(value="/{spittleId}", method=RequestMethod.GET)
    * public String spittle(@PathVariable\/* value值可省略 *\/ long spittleId, Model model) { ... }
    * 当想要重命名参数时，必须要同时修改占位符的名称，使其互相匹配
     */
    @RequestMapping(value="/{spittleId}", method=RequestMethod.GET)
    public String spittle(@PathVariable long spittleId, Model model) {
        System.out.println(spittleId);
        model.addAttribute("spittle", spittleRepositoryImp.findOne(spittleId));    // 也可以不给key，类会取类名当做key(小写)
        return "spittle";
    }
}
