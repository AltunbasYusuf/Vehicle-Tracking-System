package application.security;

public interface PasswordEncoderInterface {

    public String hash(String rawPassword); //bu metod kullanıcının girdiği şifreyi hashler ve bize o şifreye özel anlamsız bir String verir.
    /*bu anlamsız String daha sonra userları tutacağımız .txt dosyası içinde password yerine geçecek çünkü güvenlik açısından sağlam olsun diye
    txt dosyasında (yani repoda) şifrenin ham hali değil değiştirilmiş hali tutulmalıdır */



    boolean matches(String rawPassword, String hashedPassword); //bu metod kullanıcının loginde girdiği şifre ile o şifrenin hash halini kıyaslar.
}
