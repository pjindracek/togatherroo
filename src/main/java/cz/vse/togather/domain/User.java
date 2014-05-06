package cz.vse.togather.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class User {

    /**
     */
    @Column(name = "email", unique = true)
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
    
    public static User findUserByEmail(String email) {
        if (email == null) return null;
        return User.entityManager().createQuery("from User u where email like :email", User.class).setParameter("email", email).getSingleResult();
    }
    
    public List<Group> findGroupsOfUser(boolean admin) {
        CriteriaBuilder cb = User.entityManager().getCriteriaBuilder();
        CriteriaQuery<Group> criteria = cb.createQuery(Group.class);
        Root<Membership> membershipRoot = criteria.from(Membership.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(membershipRoot.<User>get("user"), this));
        if (admin) {
            predicates.add(cb.equal(membershipRoot.<Boolean>get("admin"), true));
        }
        criteria.select(membershipRoot.<Group>get("group")).where(predicates.toArray(new Predicate[]{}));
        return User.entityManager().createQuery(criteria).getResultList();
    }
}
