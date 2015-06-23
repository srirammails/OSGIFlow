package com.flow.pjm.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Forum {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	@PrimaryKey
	public long forum_id;
	public Category category_id;
	public Topics topicid;
	public byte post;
	public String updated_by;
	public String datetime;

	public long getForum_id() {
		return forum_id;
	}

	public void setForum_id(long forum_id) {
		this.forum_id = forum_id;
	}

	public Topics getTopicid() {
		return topicid;
	}

	public void setTopicid(Topics topicid) {
		this.topicid = topicid;
	}

	public byte getPost() {
		return post;
	}

	public void setPost(byte post) {
		this.post = post;
	}

	public Category getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Category category_id) {
		this.category_id = category_id;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}