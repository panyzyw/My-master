package com.yongyida.robot.voice.master.bean;

public class StockBean {
	private String id;

    private String name;

    private String currentPrice;

    private String growth;

    private String increase;

    private String minurl;

    private String shanghai;

    private String shrate;

    private String shcurprice;

    private String shenzhen;

    private String szrate;

    private String szcurprice;

    private String hongkong;

    private String hkrate;

    private String hkcurprice;

    public StockBean() {
        super();
        // TODO Auto-generated constructor stub
    }


    public StockBean(String id, String name, String currentPrice, String growth,
                 String increase, String minurl, String shanghai, String shrate,
                 String shcurprice, String shenzhen, String szrate, String szcurprice) {
        super();
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.growth = growth;
        this.increase = increase;
        this.minurl = minurl;
        this.shanghai = shanghai;
        this.shrate = shrate;
        this.shcurprice = shcurprice;
        this.shenzhen = shenzhen;
        this.szrate = szrate;
        this.szcurprice = szcurprice;
    }



    public StockBean(String id, String name, String currentPrice, String growth,
                 String increase, String minurl, String hongkong, String hkrate,
                 String hkcurprice) {
        super();
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.growth = growth;
        this.increase = increase;
        this.minurl = minurl;
        this.hongkong = hongkong;
        this.hkrate = hkrate;
        this.hkcurprice = hkcurprice;
    }


    public String getHongkong() {
        return hongkong;
    }


    public void setHongkong(String hongkong) {
        this.hongkong = hongkong;
    }


    public String getHkrate() {
        return hkrate;
    }


    public void setHkrate(String hkrate) {
        this.hkrate = hkrate;
    }


    public String getHkcurprice() {
        return hkcurprice;
    }


    public void setHkcurprice(String hkcurprice) {
        this.hkcurprice = hkcurprice;
    }


    public String getShcurprice() {
        return shcurprice;
    }


    public void setShcurprice(String shcurprice) {
        this.shcurprice = shcurprice;
    }


    public String getSzcurprice() {
        return szcurprice;
    }


    public void setSzcurprice(String szcurprice) {
        this.szcurprice = szcurprice;
    }


    public String getGrowth() {
        return growth;
    }


    public void setGrowth(String growth) {
        this.growth = growth;
    }


    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getShrate() {
        return shrate;
    }

    public void setShrate(String shrate) {
        this.shrate = shrate;
    }

    public String getSzrate() {
        return szrate;
    }

    public void setSzrate(String szrate) {
        this.szrate = szrate;
    }

    public String getShanghai() {
        return shanghai;
    }

    public void setShanghai(String shanghai) {
        this.shanghai = shanghai;
    }



    public String getShenzhen() {
        return shenzhen;
    }

    public void setShenzhen(String shenzhen) {
        this.shenzhen = shenzhen;
    }

    public String getMinurl() {
        return minurl;
    }

    public void setMinurl(String minurl) {
        this.minurl = minurl;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Stock [id=" + id + ", name=" + name + ", currentPrice="
                + currentPrice + ", increase=" + increase + ", minurl="
                + minurl + ", shanghai=" + shanghai + ", shrate=" + shrate
                + ", shenzhen=" + shenzhen + ", szrate=" + szrate + "]";
    }
}