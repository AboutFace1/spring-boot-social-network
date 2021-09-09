package com.JimsonBobson.SocialNetwork.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Interests")

public class Interest implements Comparable<Interest>{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "interest_name", unique = true, length = 25)
    private String name;


    public Interest() {

    }

    public Interest(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return Objects.equals(name, interest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Interest other) {
        return this.name.compareTo(other.name);
    }
}
