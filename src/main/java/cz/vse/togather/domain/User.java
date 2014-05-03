package cz.vse.togather.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class User {

    /**
     */
    private String email;

    /**
     */
    private String password;

    /**
     */
    private String name;

    /**
     */
    private String bio;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createdAt;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Membership> memberships = new HashSet<Membership>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments = new HashSet<Comment>();

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Event> events = new HashSet<Event>();
}
