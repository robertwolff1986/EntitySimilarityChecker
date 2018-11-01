package com.wolffr.SimilarityChecker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Physician {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "physician_id")
	private Long physicianId;
	private String name;
	private String street;
	private String zip;
	private String degree;
	private String city;
	@Column(name="merge_group")
	private Integer mergeGroup;

	protected Physician() {
	}

	public Long getPhysicianId() {
		return physicianId;
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getZip() {
		return zip;
	}

	public String getDegree() {
		return degree;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "name=" + name + ", street=" + street + ", zip=" + zip
				+ ", degree=" + degree + ", city=" + city;
	}

	public Integer getMergeGroup() {
		return mergeGroup;
	}

	public void setMergeGroup(Integer mergeGroup) {
		this.mergeGroup = mergeGroup;
	}

}