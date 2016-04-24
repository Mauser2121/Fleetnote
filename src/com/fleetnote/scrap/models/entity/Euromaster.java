package com.fleetnote.scrap.models.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Euromaster")
public class Euromaster extends Agence{
	
	private Integer idEuromaster;
	
	public Euromaster(){
		
	}

	public Integer getIdEuromaster() {
		return idEuromaster;
	}

	public void setIdEuromaster(Integer idEuromaster) {
		this.idEuromaster = idEuromaster;
	}
}
