package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Comment;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.CommentRepository;
import com.mehmetselman.socialmediaproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ✅ CommentService
 *
 * Bu servis, gönderiler (Post) üzerindeki yorum (Comment) işlemlerini yönetir.
 * Sağladığı işlemler:
 *  - Yorum ekleme
 *  - Yorum silme (yalnızca sahibi veya ADMIN rolü olan kullanıcı)
 *  - Belirli bir gönderiye ait yorumları listeleme
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 🧩 Yeni bir yorum oluşturur.
     *
     * - Post var mı kontrol eder (yoksa hata fırlatır)
     * - Yorumun sahibini ve metnini atar
     * - Kaydeder ve döndürür
     *
     * @param author  Yorumu yapan kullanıcı
     * @param postId  Yorum yapılacak gönderinin ID’si
     * @param text    Yorum içeriği
     * @return Oluşturulan Comment nesnesi
     */
    @Transactional
    public Comment addComment(User author, Long postId, String text) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post bulunamadı!"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setText(text.trim());

        return commentRepository.save(comment);
    }

    /**
     * 🧩 Belirli bir gönderiye ait TÜM yorumları döndürür.
     *
     * - Post var mı kontrol edilir
     * - İlişkili yorum listesi döndürülür
     * - Lazy initialization hatalarını önlemek için @Transactional kullanılır
     *
     * @param postId Yorumları alınacak gönderinin ID’si
     * @return List<Comment> — Post’a ait yorum listesi
     */
    @Transactional
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post bulunamadı!"));
        return post.getComments(); // Post entity'sindeki mappedBy = "post" sayesinde çalışır
    }

    /**
     * 🧩 Yorum silme işlemini gerçekleştirir.
     *
     * - Yorumun var olup olmadığını kontrol eder
     * - Silme işlemi sadece:
     *     🔹 Yorumu oluşturan kullanıcı, veya
     *     🔹 ADMIN rolündeki kullanıcı
     *   tarafından yapılabilir
     * - Yetkisiz kullanıcı hata alır
     *
     * @param id           Silinecek yorumun ID’si
     * @param currentUser  Şu anda giriş yapmış kullanıcı (token'dan alınan)
     */
    @Transactional
    public void deleteComment(Long id, User currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı!"));

        // Yalnızca kendi yorumunu silebilir veya adminse her şeyi silebilir
        if (!comment.getAuthor().getId().equals(currentUser.getId())
                && !"ADMIN".equals(currentUser.getRole().name())) {
            throw new RuntimeException("Bu yorumu silme yetkiniz yok!");
        }

        commentRepository.delete(comment);
    }
}
