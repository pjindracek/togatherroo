// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cz.vse.togather.domain;

import cz.vse.togather.domain.GroupDataOnDemand;
import cz.vse.togather.domain.Membership;
import cz.vse.togather.domain.MembershipDataOnDemand;
import cz.vse.togather.domain.UserDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect MembershipDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MembershipDataOnDemand: @Component;
    
    private Random MembershipDataOnDemand.rnd = new SecureRandom();
    
    private List<Membership> MembershipDataOnDemand.data;
    
    @Autowired
    GroupDataOnDemand MembershipDataOnDemand.groupDataOnDemand;
    
    @Autowired
    UserDataOnDemand MembershipDataOnDemand.userDataOnDemand;
    
    public Membership MembershipDataOnDemand.getNewTransientMembership(int index) {
        Membership obj = new Membership();
        setAdmin(obj, index);
        return obj;
    }
    
    public void MembershipDataOnDemand.setAdmin(Membership obj, int index) {
        Boolean admin = Boolean.TRUE;
        obj.setAdmin(admin);
    }
    
    public Membership MembershipDataOnDemand.getSpecificMembership(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Membership obj = data.get(index);
        Long id = obj.getId();
        return Membership.findMembership(id);
    }
    
    public Membership MembershipDataOnDemand.getRandomMembership() {
        init();
        Membership obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Membership.findMembership(id);
    }
    
    public boolean MembershipDataOnDemand.modifyMembership(Membership obj) {
        return false;
    }
    
    public void MembershipDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Membership.findMembershipEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Membership' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Membership>();
        for (int i = 0; i < 10; i++) {
            Membership obj = getNewTransientMembership(i);
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