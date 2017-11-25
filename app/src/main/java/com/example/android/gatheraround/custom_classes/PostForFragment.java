package com.example.android.gatheraround.custom_classes;

/**
 * Created by tamimazmain on 2017/11/25.
 */

public class PostForFragment {
    Post post;
    String userName;

    public PostForFragment(Post Post, String uname){
        post = Post;
        userName = uname;
    }
    public Post getPost(){
        return post;
    }
    public String getUserName(){
        return userName;
    }
}
