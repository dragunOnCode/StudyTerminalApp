package com.example.studyterminalapp.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Textbook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tid;

    private String textbookName;

    private String updateDate;

    private String textbookPic;

    private String contentUrl;

    private String courseName;

    private Float textbookPrice;

    private String textbookDescription;

    private String grade;

    /**
     * 出版社名称
     */
    private String pressName;

    /**
     * 书本sn码
     */
    private String bookSn;

    /**
     * 折扣率
     */
    private Integer discountRate;

    /**
     * 折扣金额
     */
    private Float discountPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 销售额
     */
    private Integer saleCount;

    /**
     * 促销开始时间
     */
    private LocalDateTime promotionStartTime;

    /**
     * 促销结束时间
     */
    private LocalDateTime promotionEndTime;

    /**
     * 作者
     */
    private String textbookAuthor;

    /**
     * 可见性
     */
    private Integer visible;

    /**
     * 出版时间
     */
    private String publishDate;


    public Textbook() {
    }

    public Textbook(Integer tid, String textbookName, String updateDate, String textbookPic,
                    String contentUrl, String courseName, Float textbookPrice,
                    String textbookDescription, String grade, String pressName, String bookSn,
                    Integer discountRate, Float discountPrice, Integer stock, String keyword,
                    Integer saleCount, LocalDateTime promotionStartTime,
                    LocalDateTime promotionEndTime, String textbookAuthor,
                    Integer visible, String publishDate) {
        this.tid = tid;
        this.textbookName = textbookName;
        this.updateDate = updateDate;
        this.textbookPic = textbookPic;
        this.contentUrl = contentUrl;
        this.courseName = courseName;
        this.textbookPrice = textbookPrice;
        this.textbookDescription = textbookDescription;
        this.grade = grade;
        this.pressName = pressName;
        this.bookSn = bookSn;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.stock = stock;
        this.keyword = keyword;
        this.saleCount = saleCount;
        this.promotionStartTime = promotionStartTime;
        this.promotionEndTime = promotionEndTime;
        this.textbookAuthor = textbookAuthor;
        this.visible = visible;
        this.publishDate = publishDate;
    }

    public String getBookSn() {
        return bookSn;
    }

    public void setBookSn(String bookSn) {
        this.bookSn = bookSn;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public LocalDateTime getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(LocalDateTime promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public LocalDateTime getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(LocalDateTime promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTextbookName() {
        return textbookName;
    }

    public void setTextbookName(String textbookName) {
        this.textbookName = textbookName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Float getTextbookPrice() {
        return textbookPrice;
    }

    public void setTextbookPrice(Float textbookPrice) {
        this.textbookPrice = textbookPrice;
    }

    public String getTextbookDescription() {
        return textbookDescription;
    }

    public void setTextbookDescription(String textbookDescription) {
        this.textbookDescription = textbookDescription;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public String getTextbookAuthor() {
        return textbookAuthor;
    }

    public void setTextbookAuthor(String textbookAuthor) {
        this.textbookAuthor = textbookAuthor;
    }

    public String getTextbookPic() {
        return textbookPic;
    }

    public void setTextbookPic(String textbookPic) {
        this.textbookPic = textbookPic;
    }

    @Override
    public String toString() {
        return "HomeTextbookVo{" +
                "tid=" + tid +
                ", textbookName='" + textbookName + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", textbookPic='" + textbookPic + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", courseName='" + courseName + '\'' +
                ", textbookPrice=" + textbookPrice +
                ", textbookDescription='" + textbookDescription + '\'' +
                ", grade='" + grade + '\'' +
                ", pressName='" + pressName + '\'' +
                ", textbookAuthor='" + textbookAuthor + '\'' +
                '}';
    }
}
