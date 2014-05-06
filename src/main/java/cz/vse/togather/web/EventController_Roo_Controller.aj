// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cz.vse.togather.web;

import cz.vse.togather.domain.Event;
import cz.vse.togather.domain.Group;
import cz.vse.togather.domain.User;
import cz.vse.togather.web.EventController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect EventController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String EventController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Event());
        return "events/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String EventController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("event", Event.findEvent(id));
        uiModel.addAttribute("itemId", id);
        return "events/show";
    }
    
        
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String EventController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Event.findEvent(id));
        return "events/update";
    }
    
        
    void EventController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("event_beginning_date_format", DateTimeFormat.patternForStyle("MS", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("event_end_date_format", DateTimeFormat.patternForStyle("MS", LocaleContextHolder.getLocale()));
    }
    
    void EventController.populateEditForm(Model uiModel, Event event) {
        uiModel.addAttribute("event", event);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("groups", Group.findAllGroups());
        uiModel.addAttribute("users", User.findAllUsers());
    }
    
    String EventController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
