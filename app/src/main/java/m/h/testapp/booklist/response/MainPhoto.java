package m.h.testapp.booklist.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainPhoto {

    @SerializedName("full_size")
    @Expose
    private String fullSize;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getFullSize() {
        return fullSize;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
