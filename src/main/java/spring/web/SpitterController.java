package spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.data.Spitter;
import spring.error.CustomFileAlreadyExistsException;
import spring.error.TestException;
import spring.repository.imp.SpitterRepositoryImp;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/spitter")
public class SpitterController {
    private final SpitterRepositoryImp spitterRepositoryImp;

    @Autowired
    public SpitterController(SpitterRepositoryImp spitterRepositoryImp) { this.spitterRepositoryImp = spitterRepositoryImp; }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new Spitter());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(
            RedirectAttributes redirectAttributes,
            @RequestPart("profilePicture") MultipartFile profilePicture,
            @Valid/* @Valid注解告知 Spring 需要确保这个对象满足校验限制，查看Spitter对象的上的约束 */ Spitter spitter,
            Errors errors
    ) throws IOException {
        System.out.println("提交用户信息...");
        if (errors.hasErrors()) return "registerForm";

        String filename = profilePicture.getOriginalFilename();
        File file = new File("C:\\Users\\jiazh\\Desktop\\spring_practice\\src\\main\\webapp\\uploads\\" + filename);
        System.out.println(filename);
        if (file.exists()) throw new CustomFileAlreadyExistsException();
        profilePicture.transferTo(file);
//        spitterRepositoryImp.save(spitter);

        // Servlet在重定向之后不会共享请求范围内(ServletRequest对象)的所有数据，所以如果想获得重定向之前的数据，则需要使用flash属性进行保存，即：使用RedirectAttributes接口
        redirectAttributes.addFlashAttribute("spitter", spitter);
        return "redirect:/spitter/" + spitter.getUsername();
    }

    // 当定义了Controller异常捕捉器时@ResponseStatus注解会失效，当使用ExceptionHandler注解时，@ResponseStatus注解没意义
//    @ExceptionHandler(CustomFileAlreadyExistsException.class)
//    public String handleFileAlreadyExistsException() { return "error/fileAlready"; }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        System.out.println("生成用户...");
        if (!model.containsAttribute("spitter")) {
            System.out.println("不存在spitter属性");
            model.addAttribute("spitter", spitterRepositoryImp.findByUsername(username));
        }
        throw new TestException();
//        return "profile";
    }
}
