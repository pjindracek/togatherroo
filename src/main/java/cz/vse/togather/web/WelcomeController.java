package cz.vse.togather.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cz.vse.togather.domain.Group;

@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("groups", Group.findGroupEntries(0, 3, "id", "desc"));
        return "welcome/index";
    }
}
