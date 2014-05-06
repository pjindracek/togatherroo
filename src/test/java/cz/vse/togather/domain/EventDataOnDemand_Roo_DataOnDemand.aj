// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cz.vse.togather.domain;

import cz.vse.togather.domain.Event;
import cz.vse.togather.domain.EventDataOnDemand;
import cz.vse.togather.domain.GroupDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect EventDataOnDemand_Roo_DataOnDemand {
    
    declare @type: EventDataOnDemand: @Component;
    
    private Random EventDataOnDemand.rnd = new SecureRandom();
    
    private List<Event> EventDataOnDemand.data;
    
    @Autowired
    GroupDataOnDemand EventDataOnDemand.groupDataOnDemand;
    
    public Event EventDataOnDemand.getNewTransientEvent(int index) {
        Event obj = new Event();
        setBeginning(obj, index);
        setDescription(obj, index);
        setEnd(obj, index);
        setTitle(obj, index);
        return obj;
    }
    
    public void EventDataOnDemand.setBeginning(Event obj, int index) {
        Date beginning = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setBeginning(beginning);
    }
    
    public void EventDataOnDemand.setDescription(Event obj, int index) {
        String description = "description_" + index;
        if (description.length() > 500) {
            description = description.substring(0, 500);
        }
        obj.setDescription(description);
    }
    
    public void EventDataOnDemand.setEnd(Event obj, int index) {
        Date end = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEnd(end);
    }
    
    public void EventDataOnDemand.setTitle(Event obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public Event EventDataOnDemand.getSpecificEvent(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Event obj = data.get(index);
        Long id = obj.getId();
        return Event.findEvent(id);
    }
    
    public Event EventDataOnDemand.getRandomEvent() {
        init();
        Event obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Event.findEvent(id);
    }
    
    public boolean EventDataOnDemand.modifyEvent(Event obj) {
        return false;
    }
    
    public void EventDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Event.findEventEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Event' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Event>();
        for (int i = 0; i < 10; i++) {
            Event obj = getNewTransientEvent(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
