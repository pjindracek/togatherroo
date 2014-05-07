package cz.vse.togather.web;
import java.security.Principal;
import java.util.Map;

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
import org.springframework.web.servlet.HandlerMapping;

import cz.vse.togather.domain.Event;
import cz.vse.togather.domain.Group;
import cz.vse.togather.domain.User;

@RequestMapping("/groups/{groupId}/events")
@Controller
@RooWebScaffold(path = "events", formBackingObject = Event.class)
public class EventController {
    
    private static final String groupIdString = "groupId";
    
    @ModelAttribute
    public void prepareModel(Model model, HttpServletRequest request) {
        Integer groupId = null;
        
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            if (pathVariables.containsKey(groupIdString)) {
                groupId = Integer.parseInt((String) pathVariables.get(groupIdString));
                model.addAttribute(groupIdString, groupId);
            }
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Event event, BindingResult bindingResult, Model uiModel, 
            @PathVariable(groupIdString) Long groupId, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, event);
            return "events/create";
        }
        uiModel.asMap().clear();
        event.setGroup(Group.findGroup(groupId));
        event.persist();
        return "redirect:/groups/" + groupId.toString() + "/events/" + encodeUrlPathSegment(event.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "text/html")
    public String update(Model uiModel, @PathVariable(groupIdString) Long groupId, @PathVariable("id") Long id,
             HttpServletRequest httpServletRequest, @Valid Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, event);
            return "events/update";
        }
        uiModel.asMap().clear();
        event.merge();
        return "redirect:/groups/" + groupId + "/events/" + encodeUrlPathSegment(id.toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model model) {
        populateEditForm(model, new Event());
        return "events/create";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model model) {
	    populateEditForm(model, Event.findEvent(id));
        return "events/update";
    }
	
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/register")
    public String register(Principal principal, @PathVariable("id") Long id,
            @PathVariable(groupIdString) Long groupId, HttpServletRequest httpServletRequest) {
        Event event = Event.findEvent(id);
        User user = User.findUserByEmail(principal.getName());
        if (!event.isAttending(user)) {
            event.addUser(user);
        }
        return "redirect:/groups/" + groupId.toString() + "/events/" + id.toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/unregister")
    public String unregister(Principal principal, @PathVariable("id") Long id, 
            @PathVariable(groupIdString) Long groupId, HttpServletRequest httpServletRequest) {
        Event event = Event.findEvent(id);
        User user = User.findUserByEmail(principal.getName());
        if (event.isAttending(user)) {
            event.removeUser(user);
        }
        return "redirect:/groups/" + groupId.toString() + "/events/" + id.toString();
    }
	
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, @PathVariable(groupIdString) Long groupId, Model model, Principal principal) {
        addDateTimeFormatPatterns(model);
        User user = User.findUserByEmail(principal.getName());
        Event event = Event.findEvent(id);
        model.addAttribute("isAdmin", Group.findGroup(groupId).isAdmin(user));
        model.addAttribute("event", event);
        model.addAttribute("isAttending", event.isAttending(user));
        return "events/show";
    }
    
	// unimplemented methods - just prevents Spring Roo from recreating those methods in aspect
    public String list() {
        throw new RuntimeException("Not available");
    }
    
    public String delete() {
        throw new RuntimeException("Not available");
    }


}
