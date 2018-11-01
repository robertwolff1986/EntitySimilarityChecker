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
	@Column(name = "merge_group")
	private Integer mergeGroup;

	protected Physician() {
	}

	public Physician(Physician physician) {
		this.physicianId=physician.getPhysicianId();
		this.name=physician.getName();
		this.street=physician.getStreet();
		this.zip=physician.getZip();
		this.city=physician.getCity();
		this.degree=physician.getDegree();
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
	
	public Integer getMergeGroup() {
		return mergeGroup;
	}

	public void setMergeGroup(Integer mergeGroup) {
		this.mergeGroup = mergeGroup;
	}

	public void setPhysicianId(Long physicianId) {
		this.physicianId = physicianId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "name=" + name + ", street=" + street + ", zip=" + zip + ", degree=" + degree + ", city=" + city;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((physicianId == null) ? 0 : physicianId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Physician other = (Physician) obj;
		if (physicianId == null) {
			if (other.physicianId != null)
				return false;
		} else if (!physicianId.equals(other.physicianId))
			return false;
		return true;
	}


	
}