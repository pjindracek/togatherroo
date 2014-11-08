package cz.vse.togather.web;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vse.togather.domain.User;

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = User.class, exposeFinders=false, delete=false)
public class UserController {
    
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "login";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid User user, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        validateEmail(user, bindingResult);
	    if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, user);
            return "users/create";
        }
        uiModel.asMap().clear();
        user.setCreatedAt(new Date());
        user.persist();
        return "redirect:/users/profile";
    }
	
	@RequestMapping("/profile")
	public String profile(Principal principal, Model model) {
	    return prepareProfile(principal, null, model);
	}

	@RequestMapping("/profile/{id}")
	public String profile(Model model, @PathVariable("id") Long id) {
	    return prepareProfile(null, id, model);
	}
	
	private String prepareProfile(Principal principal, Long id, Model model) {
	    User user = null;
        if (principal != null) {
            user = User.findUserByEmail(principal.getName());
            model.addAttribute("groupsAdmin", user.findGroupsOfUser(true));
        } else if (id != null) {
            user = User.findUser(id);
            model.addAttribute("publicUser", true);
        } else {
            return "redirect:/users/login";
        }
        model.addAttribute("allGroups", user.findGroupsOfUser(false));
        model.addAttribute("user", user);
        return "users/profile";
	}

	@RequestMapping(value = "/edit", produces = "text/html")
    public String updateForm(Principal principal, Model uiModel) {
        populateEditForm(uiModel, User.findUserByEmail(principal.getName()));
        return "users/update";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(Principal principal, @Valid User user, BindingResult bindingResult, Model uiModel, 
            HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, user);
            return "users/update";
        }
        uiModel.asMap().clear();
        User.findUserByEmail(principal.getName()).updateUser(user);
        return "redirect:/users/profile";
    }
	
	private void validateEmail(User user, BindingResult bindingResult) {
	    if (User.findUserByEmail(user.getEmail()) != null) {
	        bindingResult.rejectValue("email", "email.not_unique");
	    }
	}

    public String delete() {
        throw new RuntimeException("Not available");
    }

    public String list() {
	    throw new RuntimeException("Not available");
    }

    public String show() {
        throw new RuntimeException("Not available");
    }
}
