package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ✅ PostRepository
 *
 * Bu repository sınıfı, `Post` entity’siyle ilgili tüm veritabanı işlemlerini yönetir.
 *
 * Spring Data JPA sayesinde temel CRUD (Create, Read, Update, Delete) işlemleri
 * için manuel sorgu yazmaya gerek kalmaz.
 *
 * Otomatik olarak sağlanan metotlar:
 *  - save(post) → Yeni post oluşturma veya mevcut postu güncelleme
 *  - findById(id) → Post’u ID’ye göre getirme
 *  - findAll() → Tüm postları listeleme
 *  - deleteById(id) → Post’u ID’ye göre silme
 *  - count() → Toplam post sayısını öğrenme
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Şu anda özel bir sorguya gerek yok,
    // ancak istenirse ileride örneğin şunlar eklenebilir:
    // List<Post> findByAuthor(User author);
    // List<Post> findByDescriptionContaining(String keyword);
}
