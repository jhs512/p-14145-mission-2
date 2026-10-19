package org.example;

public class WiseSaying {
    int id;
    String text = "";
    String auth = "";

    public WiseSaying(int id) {
        this.id = id;
    }

    public WiseSaying(int id, String text, String auth) {
        this.id = id;
        this.text = text;
        this.auth = auth;
    }

    public int getId() {
        return id;
    }

    public String getAuth() {
        return auth;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String toString() {
        return  id + " / " + auth + " / " + text;
    }

    public String toJson() {
        return """
            {
                "id": %d,
                "content": "%s",
                "author": "%s"
            }
            """.formatted(id, text, auth);
    }
}