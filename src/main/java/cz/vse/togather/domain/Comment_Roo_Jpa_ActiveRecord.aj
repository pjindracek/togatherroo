// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cz.vse.togather.domain;

import cz.vse.togather.domain.Comment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Comment_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Comment.entityManager;
    
    public static final List<String> Comment.fieldNames4OrderClauseFilter = java.util.Arrays.asList("title", "message", "createdAt", "user", "group");
    
    public static final EntityManager Comment.entityManager() {
        EntityManager em = new Comment().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Comment.countComments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Comment o", Long.class).getSingleResult();
    }
    
    public static List<Comment> Comment.findAllComments() {
        return entityManager().createQuery("SELECT o FROM Comment o", Comment.class).getResultList();
    }
    
    public static List<Comment> Comment.findAllComments(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Comment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Comment.class).getResultList();
    }
    
    public static Comment Comment.findComment(Long id) {
        if (id == null) return null;
        return entityManager().find(Comment.class, id);
    }
    
    public static List<Comment> Comment.findCommentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Comment o", Comment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Comment> Comment.findCommentEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Comment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Comment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Comment.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Comment.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Comment attached = Comment.findComment(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Comment.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Comment.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Comment Comment.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Comment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
