package csh.entity;

public class WiseSaying {
    private int id;
    private String content;
    private String author;

    private WiseSaying(Builder builder) {
        this.id = builder.id;
        this.content = builder.content;
        this.author = builder.author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static class Builder {
        private int id;
        private String content;
        private String author;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public WiseSaying build() {
            return new WiseSaying(this);
        }
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":" + id + ",");
        sb.append("\"content\":\"" + content + "\",");
        sb.append("\"author\":\"" + author + "\"");
        sb.append("}");
        return sb.toString();
    }

}
