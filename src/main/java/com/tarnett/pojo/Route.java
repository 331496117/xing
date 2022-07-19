package com.tarnett.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 旅游线路商品实体类
 */
public class Route implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer rid;//线路id，必输
    private String rname;//线路名称，必输
    private double price;//价格，必输
    private String routeIntroduce;//线路介绍
    private String rflag;   //是否上架，必输，0代表没有上架，1代表是上架
    private String rdate;   //上架时间
    private Integer count;//收藏数量
    private String rimage;//缩略图
    private Integer sid;//所属商家
    private Integer isDelete;//是否删除，0表示已删除， 1表示未删除

    private List<Category> categoryList;//所属分类
    private Seller seller;//所属商家
    private List<RouteImg> routeImgList;//商品详情图片列表



    /**
     * 无参构造方法
     */
    public Route(){}

    /**
     * 有参构造方法
     * @param rid
     * @param rname
     * @param price
     * @param routeIntroduce
     * @param rflag
     * @param rdate
     * @param isThemeTour
     * @param count
     * @param cid
     * @param rimage
     * @param sid
     * @param sourceId
     */
    public Route(Integer rid, String rname, double price, String routeIntroduce, String rflag, String rdate, Integer count, String rimage, Integer sid) {
        this.rid = rid;
        this.rname = rname;
        this.price = price;
        this.routeIntroduce = routeIntroduce;
        this.rflag = rflag;
        this.rdate = rdate;
        this.count = count;
        this.rimage = rimage;
        this.sid = sid;
    }

    public List<RouteImg> getRouteImgList() {
        return routeImgList;
    }

    public void setRouteImgList(List<RouteImg> routeImgList) {
        this.routeImgList = routeImgList;
    }

    public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRouteIntroduce() {
        return routeIntroduce;
    }

    public void setRouteIntroduce(String routeIntroduce) {
        this.routeIntroduce = routeIntroduce;
    }

    public String getRflag() {
        return rflag;
    }

    public void setRflag(String rflag) {
        this.rflag = rflag;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRimage() {
        return rimage;
    }

    public void setRimage(String rimage) {
        this.rimage = rimage;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	@Override
	public String toString() {
		return "Route [rid=" + rid + ", rname=" + rname + ", price=" + price + ", routeIntroduce=" + routeIntroduce
				+ ", rflag=" + rflag + ", rdate=" + rdate + ", count=" + count + ", rimage=" + rimage + ", sid=" + sid
				+ ", categoryList=" + categoryList + ", seller=" + seller + ", routeImgList=" + routeImgList + "]";
	}
    
}
