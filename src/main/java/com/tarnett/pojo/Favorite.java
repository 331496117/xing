package com.tarnett.pojo;

import java.io.Serializable;

/**
 * 收藏实体类
 */
public class Favorite implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer rid;//旅游线路ID
    private String date;//收藏时间
    private Integer uid;//所属用户ID

    /**
     * 无参构造方法
     */
    public Favorite() {
    }

    /**
     * 有参构造方法
     * @param route
     * @param date
     * @param user
     */
    public Favorite(Integer rid, String date, Integer uid) {
            this.rid = rid;
            this.date = date;
            this.uid = uid;
    }

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}
