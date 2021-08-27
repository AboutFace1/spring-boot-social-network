package com.JimsonBobson.SocialNetwork.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="status_update")
public class StatusUpdate {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min=5, max=255)
    @Column(name="text")
    private String text;

    @Column(name = "added")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Date added;

    @PrePersist
    protected void onCreate() {
        if (added == null) {
            added = new Date();
        }
    }

    public StatusUpdate() {}

    public StatusUpdate(String text) {
        this.text = text;
    }

    public StatusUpdate(String text, Date added) {
        this.added = added;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StatusUpdate that = (StatusUpdate) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 2118475959;
    }

    @Override
    public String toString() {
        return "StatusUpdate{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", added=" + added +
                '}';
    }
}
