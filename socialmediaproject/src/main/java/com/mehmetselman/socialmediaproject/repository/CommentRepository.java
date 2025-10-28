package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ✅ CommentRepository
 *
 * Bu repository, kullanıcıların gönderilere yaptığı yorumların (Comment) veritabanı işlemlerini yönetir.
 *
 * Spring Data JPA sayesinde temel CRUD (Create, Read, Update, Delete) işlemleri
 * için ek kod yazmaya gerek yoktur.
 *
 * Otomatik sağlanan bazı metotlar:
 *  - save(comment) → Yeni yorum oluşturma veya mevcut yorumu güncelleme
 *  - findById(id) → Yorum ID’sine göre bulma
 *  - findAll() → Tüm yorumları listeleme
 *  - deleteById(id) → Yorum silme
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Şu anda özel sorgu gerekmiyor.
    // Ancak istenirse aşağıdaki örnekler eklenebilir:
    // List<Comment> findByPost(Post post);           // Belirli bir postun yorumlarını getirir.
    // List<Comment> findByAuthor(User author);       // Belirli bir kullanıcının yaptığı yorumları getirir.
}
