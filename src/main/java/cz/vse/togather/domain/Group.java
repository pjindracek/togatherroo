package cz.vse.togather.domain;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Group {

    /**
     */
    private String name;
    
    /**
     */
    @Enumerated(EnumType.ORDINAL)
    private CategoryEnum category;

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

    public boolean isMember(User user) {
        return getMembership(user) != null;
    }

    public boolean isAdmin(User user) {
        return getMembership(user) != null && getMembership(user).getAdmin();
    }

    private Membership getMembership(User user) {
        List<Membership> memberships = Group.entityManager().createQuery("from Membership m where m.group = :group and m.user = :user", Membership.class).setParameter("group", this).setParameter("user", user).getResultList();
        return memberships.size() == 1 ? memberships.get(0) : null;
    }

    public List<User> getMembers() {
        return Group.entityManager().createQuery("select m.user from Membership m where m.group = :group", User.class).setParameter("group", this).getResultList();
    }

    public List<Event> findCurrentEvents() {
        return Group.entityManager().createQuery("from Event e where e.end > :date", Event.class).setParameter("date", new Date()).getResultList();
    }

    public List<Event> findPastEvents() {
        return Group.entityManager().createQuery("from Event e where e.end < :date", Event.class).setParameter("date", new Date()).getResultList();
    }

    public void addAdmin(User user) {
        Membership membership = setupMembership(user);
        membership.setAdmin(true);
        membership.persist();
    }

    public void addMember(User user) {
        Membership membership = setupMembership(user);
        membership.persist();
    }

    private Membership setupMembership(User user) {
        Membership membership = new Membership();
        membership.setGroup(this);
        membership.setUser(user);
        return membership;
    }

    public void removeMember(User user) {
        getMembership(user).remove();
    }

    public static List<Group> searchGroups(String query) {
        query = "%" + query + "%";
        return Group.entityManager().createQuery("from Group g where g.name like :q1 or g.motto like :q2", Group.class).setParameter("q1", query).setParameter("q2", query).getResultList();
    }
}
