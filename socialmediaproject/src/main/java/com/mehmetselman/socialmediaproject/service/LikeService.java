package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Like;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.LikeRepository;
import com.mehmetselman.socialmediaproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ✅ LikeService
 *
 * Bu servis, gönderilere yapılan beğeni (Like) işlemlerini yönetir.
 *
 * Sağladığı işlemler:
 *  - Kullanıcı bir gönderiyi beğenir (like)
 *  - Kullanıcı beğenisini geri alır (unlike)
 */
@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 🧩 Kullanıcı bir gönderiyi beğenir.
     *
     * - Eğer kullanıcı zaten o gönderiyi beğenmişse hata fırlatır.
     * - Yeni bir Like nesnesi oluşturur ve veritabanına kaydeder.
     * - Post’un likeCount (beğeni sayısı) değerini 1 artırır.
     *
     * @param user   Beğeniyi yapan kullanıcı
     * @param postId Beğenilen gönderinin ID’si
     */
    @Transactional
    public void likePost(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post bulunamadı!"));

        // Aynı kullanıcı aynı gönderiyi tekrar beğenmesin
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("Bu gönderiyi zaten beğendiniz!");
        }

        // Yeni beğeni oluştur ve kaydet
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        // Post’un beğeni sayısını artır
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    /**
     * 🧩 Kullanıcı bir gönderinin beğenisini kaldırır.
     *
     * - Eğer kullanıcı gönderiyi beğenmemişse hata fırlatır.
     * - İlgili Like kaydını siler.
     * - Post’un likeCount değerini 1 azaltır (0’ın altına düşmez).
     *
     * @param user   Beğeniyi kaldıran kullanıcı
     * @param postId Beğenisi kaldırılacak gönderinin ID’si
     */
    @Transactional
    public void unlikePost(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post bulunamadı!"));

        Like like = likeRepository.findByUserAndPost(user, post);
        if (like == null) {
            throw new RuntimeException("Bu gönderiyi zaten beğenmemişsiniz!");
        }

        // Beğeniyi sil
        likeRepository.delete(like);

        // Beğeni sayısını azalt (0’ın altına düşmesin)
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        postRepository.save(post);
    }
}
