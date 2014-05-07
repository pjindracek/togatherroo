package cz.vse.togather.domain;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Event {

    /**
     */
    @NotBlank
    private String title;

    /**
     */
    @Size(max = 500)
    @NotBlank
    private String description;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MS")
    @NotNull
    private Date beginning;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MS")
    @NotNull
    private Date end;
    
    /**
     */
    @ManyToOne
    private Group group;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "events")
    private Set<User> users = new HashSet<User>();
    
    public boolean isAttending(User user) {
        return users.contains(user);
    }
    
    public void addUser(User user) {
        user.getEvents().add(this);
        user.persist();
    }
    
    public void removeUser(User user) {
        user.getEvents().remove(this);
        user.persist();
    }
}
