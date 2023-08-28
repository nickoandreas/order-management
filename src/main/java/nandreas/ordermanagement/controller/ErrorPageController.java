package nandreas.ordermanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@AllArgsConstructor
@Controller
public class ErrorPageController implements ErrorController
{
    private TemplateEngine templateEngine;

    @GetMapping("/error")
    @ResponseBody
    public String error(HttpServletRequest request)
    {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        Context context = new Context();
        context.setVariable("title", "ERROR | " + status);
        context.setVariable("message", message);

        return this.templateEngine.process("index", context);
    }
}
