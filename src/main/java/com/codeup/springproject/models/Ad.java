package com.codeup.springproject.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
public class Ad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@Column(name = "title", nullable = false, length = 100)
	private String Title;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String Description;

	@OneToOne
	private User owner;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "ad")
	private List<Image> images;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "ads_categories",
			joinColumns = {@JoinColumn(name = "ad_id")},
			inverseJoinColumns={@JoinColumn(name="category_id")}
	)
	private List<Category> categories;



	public Ad() {
	}

	public Ad(Long id, String title, String description) {
		Id = id;
		Title = title;
		Description = description;
	}

	public Ad(String title, String description, User owner) {
		Title = title;
		Description = description;
		this.owner = owner;
	}

	public Ad(String title, String description) {
		Title = title;
		Description = description;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}