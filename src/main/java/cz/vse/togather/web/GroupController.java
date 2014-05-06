package cz.vse.togather.web;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vse.togather.domain.Comment;
import cz.vse.togather.domain.Group;
import cz.vse.togather.domain.User;

@RequestMapping("/groups")
@Controller
@RooWebScaffold(path = "groups", formBackingObject = Group.class)
public class GroupController {

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model model, Principal principal,
            @ModelAttribute("commentForm") Comment comment) {
        Group group = Group.findGroup(id);
	    model.addAttribute("group", group);
        model.addAttribute("itemId", id);
        if (principal != null) {
            User user = User.findUserByEmail(principal.getName());
            model.addAttribute("isMember", group.isMember(user));
            model.addAttribute("isAdmin", group.isAdmin(user));
            model.addAttribute("currentEvents", group.findCurrentEvents());
            model.addAttribute("pastEvents", group.findPastEvents());
            model.addAttribute("commentForm", comment);
        }
        return "groups/show";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Group group, Principal principal,
            BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, group);
            return "groups/create";
        }
        uiModel.asMap().clear();
        group.persist();
        group.addAdmin(User.findUserByEmail(principal.getName()));
        return "redirect:/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
    }
	
	@RequestMapping(method = RequestMethod.POST, value="/{id}/register")
	public String register(Principal principal, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
	    Group group = Group.findGroup(id);
	    User user = User.findUserByEmail(principal.getName());
	    if (!group.isMember(user)) {
	        group.addMember(user);
	    }
	    return "redirect:/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
	}

	@RequestMapping(method = RequestMethod.POST, value="/{id}/unregister")
	public String unregister(Principal principal, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
	    Group group = Group.findGroup(id);
	    User user = User.findUserByEmail(principal.getName());
	    if (group.isMember(user)) {
	        group.removeMember(user);
	    }
	    return "redirect:/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/comment")
	public String saveComment(@Valid @ModelAttribute("commentForm") Comment comment, Model model, Principal principal, 
	        @PathVariable("id") Long id, BindingResult bindingResult, HttpServletRequest request) {
	    if (bindingResult.hasErrors()) {
	        return show(id, model, principal, comment);
	    }
	    comment.setGroup(Group.findGroup(id));
	    comment.setCreatedAt(new Date());
	    comment.setUser(User.findUserByEmail(principal.getName()));
	    comment.persist();
	    return "redirect:/groups/" + encodeUrlPathSegment(id.toString(), request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, Integer page, Model uiModel, Principal principal) {
	    Group group = Group.findGroup(id);
	    if (group.isAdmin(User.findUserByEmail(principal.getName()))) {
	        group.remove();
	    }
        return "redirect:/users/profile";
    }
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String query, Model model) {
	    List<Group> foundGroups = Group.searchGroups(query);
	    model.addAttribute("foundGroups", foundGroups);
	    if (foundGroups.isEmpty()) {
	        model.addAttribute("randomGroups", Group.findGroupEntries(0, 10, "id", "desc"));
	    }
	    return "groups/search";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, Principal principal) {
	    Group group = Group.findGroup(id);
	    if (group.isAdmin(User.findUserByEmail(principal.getName()))) {
	        populateEditForm(uiModel, group);
	    }
        return "groups/update";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Group group, BindingResult bindingResult, Model uiModel,
            Principal principal, HttpServletRequest httpServletRequest) {
        if (group.isAdmin(User.findUserByEmail(principal.getName()))) {
            if (bindingResult.hasErrors()) {
                populateEditForm(uiModel, group);
                return "groups/update";
            }
            uiModel.asMap().clear();
            group.merge();
        }
        return "redirect:/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
    }
}
