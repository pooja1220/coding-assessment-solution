package com.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="image_table")
public class Image {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageId;

    private String generatedId;

    private String deleteImageId;

    @ManyToOne
	@JoinColumn(name="userid")
    @JsonIgnore
    private User user;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public String getDeleteImageId() {
        return deleteImageId;
    }

    public void setDeleteImageId(String deleteImageId) {
        this.deleteImageId = deleteImageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
