package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * ✅ PostService
 *
 * Bu servis, gönderiler (Post) ile ilgili tüm CRUD (oluşturma, okuma, güncelleme, silme)
 * ve ek işlemleri yönetir.
 *
 * Sağladığı işlemler:
 *  - Yeni post oluşturma
 *  - Post getirme (tek veya tüm liste)
 *  - Post güncelleme (sadece sahibi)
 *  - Post silme (sahibi veya admin)
 *  - Görüntülenme sayısını artırma
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    /**
     * 🧩 Yeni bir gönderi oluşturur.
     *
     * - Post’a ait açıklama (description) ve resim (imageUrl) bilgileri atanır.
     * - Gönderi oluşturan kullanıcı (author) ilişkilendirilir.
     * - Veritabanına kaydedilip kaydedilen Post döndürülür.
     *
     * @param author      Gönderiyi oluşturan kullanıcı
     * @param description Gönderi açıklaması
     * @param imageUrl    Gönderi görsel bağlantısı
     * @return Kaydedilen Post nesnesi
     */
    @Transactional
    public Post createPost(User author, String description, String imageUrl) {
        Post post = new Post();
        post.setAuthor(author);
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        return postRepository.save(post);
    }

    /**
     * 🧩 ID’ye göre gönderiyi getirir.
     *
     * - Eğer gönderi bulunamazsa hata fırlatır.
     *
     * @param id Gönderinin ID’si
     * @return İstenen Post nesnesi
     */
    @Transactional
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gönderi bulunamadı!"));
    }

    /**
     * 🧩 Tüm gönderileri listeler.
     *
     * - Veritabanındaki tüm gönderiler döndürülür.
     *
     * @return Post listesi
     */
    @Transactional
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * 🧩 Gönderiyi günceller.
     *
     * - Sadece gönderiyi oluşturan kullanıcı güncelleme yapabilir.
     * - Açıklama ve görsel bağlantısı güncellenir.
     *
     * @param id          Güncellenecek gönderinin ID’si
     * @param currentUser Şu anda giriş yapan kullanıcı
     * @param description Yeni açıklama
     * @param imageUrl    Yeni görsel bağlantısı
     */
    @Transactional
    public void updatePost(Long id, User currentUser, String description, String imageUrl) {
        Post post = getPost(id);

        // Sadece post sahibi güncelleyebilir
        if (!post.getAuthor().getId().equals(currentUser.getId()))
            throw new RuntimeException("Bu gönderiyi güncelleme yetkiniz yok!");

        post.setDescription(description);
        post.setImageUrl(imageUrl);
        postRepository.save(post);
    }

    /**
     * 🧩 Gönderiyi siler.
     *
     * - Sadece gönderinin sahibi veya admin olan kullanıcı silebilir.
     *
     * @param id          Silinecek gönderinin ID’si
     * @param currentUser Şu anda giriş yapan kullanıcı
     */
    @Transactional
    public void deletePost(Long id, User currentUser) {
        Post post = getPost(id);

        // Sadece sahibi veya ADMIN silebilir
        if (!post.getAuthor().getId().equals(currentUser.getId())
                && !"ADMIN".equals(currentUser.getRole().name())) {
            throw new RuntimeException("Bu gönderiyi silme yetkiniz yok!");
        }

        postRepository.delete(post);
    }

    /**
     * 🧩 Görüntülenme sayısını artırır.
     *
     * - Her çağrıldığında post’un viewCount alanı 1 artırılır.
     *
     * @param id Görüntülenen gönderinin ID’si
     */
    @Transactional
    public void viewPost(Long id) {
        Post post = getPost(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }
}
