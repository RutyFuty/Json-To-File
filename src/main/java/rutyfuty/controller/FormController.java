package rutyfuty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rutyfuty.service.FormService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("form")
public class FormController {

    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public String handleGet() {
        return formService.getForm();
    }

    @PostMapping
    public void handlePost(@RequestBody String formParams, HttpServletResponse response) {
        formService.handleForm(formParams, response);
    }
}
