# 💸 QR Bank - Spring Boot QR Code Payment System

**QR Bank**, kullanıcıların hesapları arasında QR kod aracılığıyla anında para transferi yapabilmesini sağlayan modern bir **Spring Boot** uygulamasıdır.

---

## 🧠 Proje Özeti

QR Bank, bankacılık işlemlerini QR kod teknolojisi ile daha hızlı ve güvenli hale getirmek amacıyla geliştirilmiş bir backend sistemidir. Kullanıcılar ödeme talebi oluşturur, bu talep bir QR kod ile temsil edilir ve başka bir kullanıcı bu kodu tarayarak işlemi tamamlayabilir.

---

## 🚀 Kullanılan Teknolojiler

| Teknoloji           | Açıklama                                      |
|---------------------|-----------------------------------------------|
| Spring Boot         | Uygulama çatısı                               |
| Spring Security     | Kimlik doğrulama ve yetkilendirme            |
| JWT (jjwt)          | Token tabanlı authentication                  |
| Spring Data JPA     | ORM ve veri yönetimi                          |
| H2 Database         | Geliştirme aşamasında hafif veritabanı        |
| ZXing (QR Code)     | QR kod üretimi ve taraması                    |
| Lombok              | Boilerplate kodları azaltmak için             |
| OpenAPI (Swagger)   | API dökümantasyonu (isteğe bağlı)             |

---

## 📦 Özellikler

✅ Kullanıcı kayıt ve giriş sistemi  
✅ Her kullanıcıya özel hesap (IBAN, bakiye)  
✅ Para transferi (Transaction Management)  
✅ QR kod ile ödeme talebi oluşturma  
✅ QR kod taranarak ödeme gerçekleştirme  
✅ QR durum takibi (`CREATED`, `SCANNED`, `COMPLETED`)  
✅ JWT tabanlı güvenli istekler

---
## 📸 QR Kod İş Akışı

1. **Kullanıcı A**, bir ödeme talebi oluşturur (`amount`, `senderAccountId`).
2. Sistem bu bilgileri içeren bir QR kod üretir ve bu talebi veritabanına kaydeder.
3. **Kullanıcı B**, QR kodu tarar ve `scan` endpointine gönderir.
4. Sistem QR kodun geçerliliğini kontrol eder ve işlemi gerçekleştirir.
5. Bakiye güncellenir, işlem tamamlanır (`COMPLETED`).

---

## 📍 API Endpointleri (Örnek)

| Endpoint                      | Açıklama                     | Method |
|------------------------------|------------------------------|--------|
| `/api/auth/register`         | Kullanıcı kaydı              | POST   |
| `/api/auth/login`            | Giriş yap                    | POST   |
| `/api/qr/create`             | QR ile ödeme isteği oluştur | POST   |
| `/api/qr/scan`               | QR kodu tara ve işle         | POST   |
| `/api/account/balance`       | Bakiye sorgulama             | GET    |

---

## 🧪 Örnek İstek: QR Kod Oluştur

```json
POST /api/qr/create
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "senderAccountId": 1,
  "amount": 250.00
}

## 🧪 Örnek İstek: QR Kod Tara
```json
POST /api/qr/scan
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
{
"eyJhY2NvdW50SWQiOjEsImFtb3VudCI6MjUwLjAsInFydGltZSI6IjIwMjUtMDUtMTVUMTQ6NTAifQ=="
}

##🛡️ Güvenlik
JWT tabanlı authentication

Role bazlı endpoint koruması

QR kodlar bir kere kullanılabilir (status takibi yapılır)

##⭐ Katkıda Bulun
PR’ler her zaman açıktır. Lütfen önce bir issue açmayı unutmayın. 🙌

