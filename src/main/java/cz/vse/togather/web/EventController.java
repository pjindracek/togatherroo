package cz.vse.togather.web;
import cz.vse.togather.domain.Event;
import cz.vse.togather.domain.Group;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/groups/{groupId}/events")
@Controller
@RooWebScaffold(path = "events", formBackingObject = Event.class)
public class EventController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Event event, BindingResult bindingResult, Model uiModel, 
            @PathVariable("groupId") Long groupId, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, event);
            return "events/create";
        }
        uiModel.asMap().clear();
        event.setGroup(Group.findGroup(groupId));
        event.persist();
        return "redirect:/groups/" + groupId.toString() + "/events/" + encodeUrlPathSegment(event.getId().toString(), httpServletRequest);
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Event event, BindingResult bindingResult, Model uiModel, 
            @PathVariable("groupId") Long groupId, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, event);
            return "events/update";
        }
        uiModel.asMap().clear();
        event.merge();
        return "redirect:/groups/" + groupId + "/events/" + encodeUrlPathSegment(event.getId().toString(), httpServletRequest);
    }
}
