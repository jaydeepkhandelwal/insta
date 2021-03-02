package com.oci.insta.entities.models.base;

import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
@TypeDef(name = "jsonb", typeClass = JsonNodeStringType.class)
@Accessors(chain = true)
@Where(clause = "deleted!=true")
public class BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    protected Long id;

    @Column(name = "deleted")
    protected Boolean deleted;

    @Column(name = "created_on")
    protected Date createdOn;

    @Column(name = "last_updated_on")
    protected Date lastUpdatedOn;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "last_updated_by")
    protected String lastUpdatedBy;

    @NotNull
    @Column(name = "ext_id")
    private String extId;

    @PrePersist
    public void beforeInsert() {
        this.lastUpdatedOn = new Date();
        this.createdOn = new Date();
        if (this.createdBy == null) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
            }

        }
        this.lastUpdatedBy = this.createdBy;
        this.deleted = Boolean.FALSE;
        this.extId =  UUID.randomUUID().toString();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.lastUpdatedOn = new Date();
        if (SecurityContextHolder.getContext().getAuthentication() != null)
            this.lastUpdatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        else
            this.lastUpdatedBy = "admin";
    }
}
