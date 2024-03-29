// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cz.vse.togather.domain;

import cz.vse.togather.domain.User;
import cz.vse.togather.domain.UserDataOnDemand;
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
import org.springframework.stereotype.Component;

privileged aspect UserDataOnDemand_Roo_DataOnDemand {
    
    declare @type: UserDataOnDemand: @Component;
    
    private Random UserDataOnDemand.rnd = new SecureRandom();
    
    private List<User> UserDataOnDemand.data;
    
    public User UserDataOnDemand.getNewTransientUser(int index) {
        User obj = new User();
        setBio(obj, index);
        setCreatedAt(obj, index);
        setEmail(obj, index);
        setName(obj, index);
        setPasswordEncrypted(obj, index);
        return obj;
    }
    
    public void UserDataOnDemand.setBio(User obj, int index) {
        String bio = "bio_" + index;
        if (bio.length() > 500) {
            bio = bio.substring(0, 500);
        }
        obj.setBio(bio);
    }
    
    public void UserDataOnDemand.setCreatedAt(User obj, int index) {
        Date createdAt = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreatedAt(createdAt);
    }
    
    public void UserDataOnDemand.setEmail(User obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void UserDataOnDemand.setName(User obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void UserDataOnDemand.setPasswordEncrypted(User obj, int index) {
        String passwordEncrypted = "passwordEncrypted_" + index;
        obj.setPasswordEncrypted(passwordEncrypted);
    }
    
    public User UserDataOnDemand.getSpecificUser(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        User obj = data.get(index);
        Long id = obj.getId();
        return User.findUser(id);
    }
    
    public User UserDataOnDemand.getRandomUser() {
        init();
        User obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return User.findUser(id);
    }
    
    public boolean UserDataOnDemand.modifyUser(User obj) {
        return false;
    }
    
    public void UserDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = User.findUserEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'User' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User obj = getNewTransientUser(i);
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
