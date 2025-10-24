package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Like;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.LikeRepository;
import com.mehmetselman.socialmediaproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    //yeni gonderi olustur
    public Post createPost(User author, String description, String imageUrl){
        Post post = new Post();
        post.setAuthor(author);
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        return postRepository.save(post);
    }

    //tek gonderi goruntule
    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Gonderi bulunamadi!"));
    }

    //Tum gonderileri goruntule
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    //gonderi guncelleme
    public void updatePost(Long id, User currentUser, String newDescription,String newImage ){
        Post post = getPost(id);
        if (!post.getAuthor().equals(currentUser) && !"ADMIN".equals(currentUser.getRole())) throw new RuntimeException("Bu islem icin yetkiniz yoktur!");
        post.setDescription(newDescription);
        post.setImageUrl(newImage);
        postRepository.save(post);
    }

    //gonderi silme
    public void deletePost(Long id, User currentUser){
        Post post = getPost(id);
        if (!post.getAuthor().equals(currentUser) && !"ADMIN".equals(currentUser.getRole())) throw new RuntimeException("Bu islem icin yetkiniz yoktur!");
        postRepository.delete(post);
    }

    //goruntuleme sayisini artirir
    public void viewPost(Long id){
        Post post = getPost(id);
        post.setViewCount(post.getViewCount()+1);
        postRepository.save(post);
    }

    //begeni sayisini artirir
    public void likePost(Long postId, User user){
        Post post = getPost(postId);
        if (likeRepository.findByUserAndPost(user, post) != null ) throw new RuntimeException("Bu gonderiyi zaten begendiniz");
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);

    }

    //begeniyi geri al
    public void unlikePost(Long postId, User user){
        Post post = getPost(postId);
        Like like = likeRepository.findByUserAndPost(user, post);
        if (like==null) throw new RuntimeException("Gonderi begenilmedi.");
        likeRepository.delete(like);
        post.setLikeCount(post.getLikeCount()-1);
        postRepository.save(post);

    }




}
