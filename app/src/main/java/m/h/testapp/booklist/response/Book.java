package m.h.testapp.booklist.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Book {

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
    private List<String> authors = new ArrayList<String>();
    @SerializedName("narrators")
    @Expose
    private List<String> narrators = new ArrayList<String>();
    @SerializedName("categories")
    @Expose
    private List<String> categories = new ArrayList<String>();
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("price_currency")
    @Expose
    private String priceCurrency;
    @SerializedName("size_str")
    @Expose
    private String sizeStr;
    @SerializedName("duration_str")
    @Expose
    private String durationStr;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_free")
    @Expose
    private Boolean isFree;
    @SerializedName("wish_listed_by_viewer")
    @Expose
    private Boolean wishListedByViewer;
    @SerializedName("feedback_by_viewer")
    @Expose
    private Boolean feedbackByViewer;
    @SerializedName("bought_by_viewer")
    @Expose
    private Boolean boughtByViewer;
    @SerializedName("finished_by_viewer")
    @Expose
    private Boolean finishedByViewer;

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public MainPhoto getMainPhoto() {
        return mainPhoto;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getNarrators() {
        return narrators;
    }

    public void setNarrators(List<String> narrators) {
        this.narrators = narrators;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Double getPrice() {
        return price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public String getDurationStr() {
        return durationStr;
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

    public Boolean getWishListedByViewer() {
        return wishListedByViewer;
    }

    public Boolean getFeedbackByViewer() {
        return feedbackByViewer;
    }

    public Boolean getBoughtByViewer() {
        return boughtByViewer;
    }

    public Boolean getFinishedByViewer() {
        return finishedByViewer;
    }


}
