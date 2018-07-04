package com.example.shivamvk.facebookandroid;

public class WallPosts {

    String postId,postBy,postMessage,postImage;

    public WallPosts(String postId, String postBy, String postMessage, String postImage) {
        this.postId = postId;
        this.postBy = postBy;
        this.postMessage = postMessage;
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostBy() {
        return postBy;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public String getPostImage() {
        return postImage;
    }
}
