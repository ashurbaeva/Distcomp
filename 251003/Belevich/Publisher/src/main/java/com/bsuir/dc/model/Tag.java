package com.bsuir.dc.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany()
    @JoinTable(name = "tbl_tweet_tag", joinColumns = {@JoinColumn (name = "tagId")}, inverseJoinColumns = {@JoinColumn (name = "tweetId")})
    private List<Tweet> tweets = new ArrayList<>();

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setTweets(List<Tweet> tweets) { this.tweets = tweets; }
    public List<Tweet> getTweets() { return tweets; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && name.equals(tag.name);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name); }

    public Tag(String name) { this.name = name; }

    public Tag() {}
}
