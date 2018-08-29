package com.pierceecom.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDataService {
    private Map<String, Post> posts = new HashMap<>();

    private static PostDataService ourInstance = new PostDataService();

    public static PostDataService getInstance() {
        return ourInstance;
    }

    public void addPost(Post post) {
        posts.put(post.getId(), post);
    }

    public void replacePost(Post post) {
        posts.replace(post.getId(), post);
    }

    public List<Post> getPostList() {
        return new ArrayList(posts.values());
    }

    public Post getPostById(String id) {
        return posts.get(id);
    }

    public Post deletePostById(String id) {
        Post post = posts.get(id);
        posts.remove(id);
        return post;
    }

}
