package m.h.testapp.booklist.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookDetailRespose {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("main_photo")
    @Expose
    private MainPhoto mainPhoto;
    @SerializedName("authors")
    @Expose
    private List<BookDetailSub> authors = new ArrayList<BookDetailSub>();
    @SerializedName("narrators")
    @Expose
    private List<BookDetailSub> narrators = new ArrayList<BookDetailSub>();
    @SerializedName("categories")
    @Expose
    private List<BookDetailSub> categories = new ArrayList<BookDetailSub>();
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("price_currency")
    @Expose
    private String priceCurrency;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("size_str")
    @Expose
    private String sizeStr;
    @SerializedName("age_limit")
    @Expose
    private Integer ageLimit;
    @SerializedName("duration_str")
    @Expose
    private String durationStr;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_free")
    @Expose
    private Boolean isFree;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("annotation")
    @Expose
    private String annotation;
    @SerializedName("wish_listed_by_viewer")
    @Expose
    private Boolean wishListedByViewer;
    @SerializedName("bought_by_viewer")
    @Expose
    private Boolean boughtByViewer;
    @SerializedName("finished_by_viewer")
    @Expose
    private Boolean finishedByViewer;
    @SerializedName("feedback_by_viewer")
    @Expose
    private Boolean feedbackByViewer;
    @SerializedName("viewer_last_stop_point")
    @Expose
    private Integer viewerLastStopPoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainPhoto getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(MainPhoto mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public List<BookDetailSub> getAuthors() {
        return authors;
    }

    public void setAuthors(List<BookDetailSub> authors) {
        this.authors = authors;
    }

    public List<BookDetailSub> getNarrators() {
        return narrators;
    }

    public void setNarrators(List<BookDetailSub> narrators) {
        this.narrators = narrators;
    }

    public List<BookDetailSub> getCategories() {
        return categories;
    }

    public void setCategories(List<BookDetailSub> categories) {
        this.categories = categories;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Boolean getWishListedByViewer() {
        return wishListedByViewer;
    }

    public void setWishListedByViewer(Boolean wishListedByViewer) {
        this.wishListedByViewer = wishListedByViewer;
    }

    public Boolean getBoughtByViewer() {
        return boughtByViewer;
    }

    public void setBoughtByViewer(Boolean boughtByViewer) {
        this.boughtByViewer = boughtByViewer;
    }

    public Boolean getFinishedByViewer() {
        return finishedByViewer;
    }

    public void setFinishedByViewer(Boolean finishedByViewer) {
        this.finishedByViewer = finishedByViewer;
    }

    public Boolean getFeedbackByViewer() {
        return feedbackByViewer;
    }

    public void setFeedbackByViewer(Boolean feedbackByViewer) {
        this.feedbackByViewer = feedbackByViewer;
    }

    public Integer getViewerLastStopPoint() {
        return viewerLastStopPoint;
    }

    public void setViewerLastStopPoint(Integer viewerLastStopPoint) {
        this.viewerLastStopPoint = viewerLastStopPoint;
    }
}
