package nl.management.finance.app.data.common;

import androidx.annotation.NonNull;

public class TPPMessage {
    private String category;
    private String code;
    private String path;
    private String text;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\ncategory=%s,\ncode=%s,\npath=%s,\ntext=%s]",
                category, code, path, text);
    }
}
