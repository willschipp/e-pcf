package io.pivotal.poc.emirates.wskt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Position implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column
	private String sessionId;
	
	@Column
	private Double positionX;
	
	@Column
	private Double positionY;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	@Override
	public String toString() {
		return "Position [id=" + id + ", sessionId=" + sessionId
				+ ", positionX=" + positionX + ", positionY=" + positionY + "]";
	}
	
	
}
