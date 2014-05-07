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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
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
    @Pattern(regexp = "[^@][\\w.-]+@[\\w.-]+[.][a-z]{2,4}")
    @Column(name = "email", unique = true)
    private String email;

    /**
     */
    private String passwordEncrypted;
    
    /**
     */
    @Transient
    private String password;

    /**
     */
    @Transient
    private String passwordConfirm;

    /**
     */
    @NotBlank
    private String name;

    /**
     */
    @Size(max = 500, message = "Bio is too long, only 500 characters are allowed")
    private String bio;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MS")
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

    @AssertTrue
    private boolean isPasswordValid() {
        return getId() == null ? StringUtils.isNotBlank(this.password) : true;
    }
    
    @AssertTrue
    private boolean isPasswordConfirmationValid() {
        return StringUtils.isNotBlank(this.password) ? this.password.equals(this.passwordConfirm) : true;
    }
    
    @PrePersist @PreUpdate
    public void adjustPassword() {
        if (StringUtils.isNotBlank(this.password) && isPasswordValid() && isPasswordConfirmationValid()) {
            setPasswordEncrypted(DigestUtils.sha256Hex(password));
        }
    }
    
    public void updateUser(User user) {
        this.name = user.name;
        this.bio = user.bio;
        this.password = user.password;
        this.passwordConfirm = user.passwordConfirm;
        adjustPassword();
        this.merge();
    }

    public static User findUserByEmail(String email) {
        if (email == null) return null;
        List<User> results = User.entityManager().createQuery("from User u where email like :email", User.class).setParameter("email", email).getResultList();
        return results.size() == 1 ? results.get(0) : null;
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
        criteria.select(membershipRoot.<Group>get("group")).where(predicates.toArray(new Predicate[] {  }));
        return User.entityManager().createQuery(criteria).getResultList();
    }
}
