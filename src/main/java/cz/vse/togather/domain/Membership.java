package cz.vse.togather.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Membership {

    /**
     */
    private Boolean admin = false;

    /**
     */
    @ManyToOne
    private User user;

    /**
     */
    @ManyToOne
    private Group group;
}
