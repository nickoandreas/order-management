package nandreas.ordermanagement.controller;

import lombok.AllArgsConstructor;
import nandreas.ordermanagement.model.Order;
import nandreas.ordermanagement.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@AllArgsConstructor
@Controller
public class WebOrderController
{
    private OrderService orderService;

    private TemplateEngine templateEngine;

    @GetMapping(
            path = "/order/payment/{orderNumber}",
            produces = MediaType.TEXT_HTML_VALUE
    )
    @ResponseBody
    public String update(@PathVariable("orderNumber") String orderNumber, ModelAndView modelAndView)
    {
        Boolean isUpdated = this.orderService.updateOrder(orderNumber);

        String resultMessage = isUpdated ? "Your payment has been successfully processed." : "Something went wrong.";

        Context context = new Context();
        context.setVariable("title", orderNumber);
        context.setVariable("message", resultMessage);

        return this.templateEngine.process("index", context);
    }
}
