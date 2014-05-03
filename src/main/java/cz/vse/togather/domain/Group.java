package cz.vse.togather.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Group {

    /**
     */
    private String name;

    /**
     */
    @Size(max = 500)
    private String motto;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Membership> memberships = new HashSet<Membership>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Comment> comments = new HashSet<Comment>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Event> events = new HashSet<Event>();
}
