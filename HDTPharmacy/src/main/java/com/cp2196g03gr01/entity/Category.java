package com.cp2196g03gr01.entity;

import java.beans.Transient;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.cp2196g03gr01.common.CategoryLevelEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;
	
	@NotBlank(message = "")
	@Column(name="category_name", length=50, nullable=false)
    private String name;
	
	@Column(name="category_alias", length=50, nullable=false, unique=true)
    private String alias;
	
	@Column(name="category_image")
    private String image;
	
	@Column(name="category_is_online")
	private Boolean showOnline;
	
	@Column(name="category_level")
	@Enumerated(EnumType.STRING)
    private CategoryLevelEnum level;
	
	@ManyToOne(targetEntity = Category.class, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
	,fetch = FetchType.EAGER)
    @JoinColumn(name = "category_parent", referencedColumnName = "category_id",nullable = true)
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
    		fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Category> childCategories;
    
    @Transient  
    public String getParentName() {
    	String result=this.getName();
    	Category parent=parentCategory;
    	while(parent!=null) {
    		result=parent.getName()+" -- "+result;
    		parent=parent.getParentCategory();
    	}
    	return result;
    }
}
