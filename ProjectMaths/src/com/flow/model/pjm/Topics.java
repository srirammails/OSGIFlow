package com.flow.pjm.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Topics {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	@PrimaryKey
	public long topicid;
	public String topicsname;
	public String status;
	public String category_id;

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String topiccreatedby;
	public String latestreplyby;

	public long getTopicid() {
		return topicid;
	}

	public void setTopicid(long topicid) {
		this.topicid = topicid;
	}

	public String getTopicsname() {
		return topicsname;
	}

	public void setTopicsname(String topicsname) {
		this.topicsname = topicsname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTopiccreatedby() {
		return topiccreatedby;
	}

	public void setTopiccreatedby(String topiccreatedby) {
		this.topiccreatedby = topiccreatedby;
	}

	public String getLatestreplyby() {
		return latestreplyby;
	}

	public void setLatestreplyby(String latestreplyby) {
		this.latestreplyby = latestreplyby;
	}

}