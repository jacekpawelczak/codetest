package com.pierceecom.blog;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Post {

    private String id;
    private String title;
    private String content;

    public Post() {
    }

    public String toString() {
        return "id" + id + "title" + title + "content" + content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

}
