# 💸 QR Bank - Spring Boot QR Code Payment System

Bu proje, QR kod tabanlı finansal işlemleri yönetmek için geliştirilmiş bir **Spring Boot REST API**'dir.
Proje; QR kod oluşturma ve okuma, hesap yönetimi, ve finansal işlemler (para gönderme, alma, yatırma, çekme) gibi temel bankacılık işlemlerini içerir.

---
## 🔄 İş Akışı (Flow)

1️⃣ **Hesap Oluşturma:**  
   - Kullanıcı veya banka yetkilisi, yeni bir hesap oluşturur.  
   - Hesap için kullanıcı bilgileri ve IBAN atanır.  

2️⃣ **QR Kod Oluşturma:**  
   - Belirli bir işlem (ör. para yatırma) için işlem verisi ile QR kod oluşturulur.  
   - QR kod PNG formatında döner.  

3️⃣ **QR Kod Okuma:**  
   - Kullanıcı QR kodu yükler ve işlem verisini çözümler.  

4️⃣ **Para Transferi (IBAN veya QR ile):**  
   - Kullanıcı IBAN veya QR kod verisi ile para yatırma/çekme/gönderme işlemi başlatır.  
   - İşlem sonucu başarılı veya hatalı olarak döner.  

5️⃣ **Hesap Bakiyesi Güncelleme:**  
   - Başarılı işlem sonrasında ilgili hesapların bakiyesi güncellenir.  

6️⃣ **Finansal İşlem Kayıtları:**  
   - Tüm işlemler veri tabanına kaydedilir ve sonradan izlenebilir.  

---

## 📚 API Dökümantasyonu

Proje **Swagger/OpenAPI 3.0** ile dokümante edilmiştir.  
Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
## 🛠️ API Örnek Endpointleri

### 🔹 QR Kod İşlemleri

| Metot | Endpoint | Açıklama |
|-------|----------|----------|
| `POST` | `/api/v1/qr-code/generate` | QR kodu oluşturur (PNG). |
| `POST` | `/api/v1/qr-code/decode` | Yüklenen QR kod resmini çözümler. |
| `GET` | `/api/v1/accounts/{id}` | ID ile hesabı getirir. |
| `GET` | `/api/v1/accounts/user/{userId}` | Kullanıcı ID'sine ait hesapları listeler. |
| `PUT` | `/api/v1/accounts/{id}` | Hesabı günceller. |
| `DELETE` | `/api/v1/accounts/{id}` | Hesabı siler. |
| `POST` | `/api/v1/transactions/send` | IBAN ile para gönderir. |
| `GET` | `/api/v1/transactions/receiver` | Para alımını onaylar (transactionId ve receiverAccountIban). |
| `POST` | `/api/v1/transactions/deposit` | Para yatırma. |
| `POST` | `/api/v1/transactions/withdraw` | Para çekme. |
| `POST` | `/api/v1/transactions/deposit/qr` | QR kod ile para yatırma. |
| `POST` | `/api/v1/transactions/withdraw/qr` | QR kod ile para çekme. |

                
---

#### Örnek İstek – QR Kod Oluşturma
```http
POST /api/v1/qr-code/generate
Content-Type: application/json
Accept: image/png

{

  "accountIban": "String",
  "transactionType": {},
  "description": "String",
  "amount": 0
}

```http
POST /api/v1/qr-code/decode
Content-Type: multipart/form-data

file: qr_image.png

---

## 🚀 Kullanılan Teknolojiler

| Teknoloji           | Açıklama                                      |
|---------------------|-----------------------------------------------|
| Spring Boot         | Uygulama çatısı                               |
| Spring Security     | Kimlik doğrulama ve yetkilendirme             |
| JWT (jjwt)          | Token tabanlı authentication                  |
| Spring Data JPA     | ORM ve veri yönetimi                          |
| MySql               | Verilerin kaydedileceği Veritabanı            |
| ZXing (QR Code)     | QR kod üretimi ve taraması                    |
| Lombok              | Boilerplate kodları azaltmak için             |
| OpenAPI (Swagger)   | API dökümantasyonu              |
| Validation           | Girdi doğrulama ve validasyon işlemleri       |
---

## 📦 Özellikler

✅ Kullanıcı kayıt ve giriş sistemi
✅ JWT tabanlı kimlik doğrulama ve role bazlı yetkilendirme
✅ Her kullanıcıya özel TC standardına uygun IBAN ve başlangıç bakiyesi
✅ Hesaplar arası para transferi (EFT, havale)
✅ Hesaba para yatırma ve para çekme işlemleri
✅ QR kod ile para yatırma, para çekme ve ödeme işlemleri
✅ QR kod durum takibi: PENDING,COMPLETED,FAILED
✅ QR kodlar tek kullanımlık ve işlem tamamlandığında geçersiz olur.

---




##🛡️ Güvenlik

🔒 JWT tabanlı authentication
🔒 Role bazlı endpoint koruması (ör. ROLE_USER, ROLE_ADMIN)
🔒 QR kodlar tek kullanımlık ve durum takibi yapılır (CREATED → SCANNED → COMPLETED)

##⭐ Katkıda Bulun
PR’ler her zaman açıktır. Lütfen önce bir issue açmayı unutmayın. 🙌

