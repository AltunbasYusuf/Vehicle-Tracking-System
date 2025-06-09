package Infrastructure;

import application.repository.UserRepositoryInterface;
import domain.user.User;

import java.io.*;

public class TxtUserRepository implements UserRepositoryInterface {

    private final String filepath; /* bu file keyi bu değişkenin bir kere değer aldıktan sonra bir daha o değerinin
    değişemeyeceği anlamına geliyor.*/

    public TxtUserRepository(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void save(User user) throws IOException {

        String email=user.getMail();
        String password = user.getPassword(); // signUp() metodunda şifreyi user'a hashleyip ekliyoruz.
        String role = user.getRole();
        String name= user.getName();
        String surname = user.getSurname();

        //Yazma formatı : email,şifre,rol(araç sahibi ,satıcı),ad,soyad....
        try (BufferedWriter txtWriter = new BufferedWriter(new FileWriter(this.filepath, true))) {
            txtWriter.write(email + "," + password + "," + role + "," + name + "," + surname);
            txtWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong when writing TXTUser repository.", e);
        }
    }


    @Override
    public boolean doesExist(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Beklenen format: mail,hashedPassword,name,surname,role
                String[] parts = line.split(",");
                if (parts.length == 0) continue;

                if (parts[0].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong when reading TXTUser repository.", e);
        }

        return false;
    }

    @Override
    public User findByMail(String mail) {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                if (parts[0].equalsIgnoreCase(mail)) {
                    String hashedPassword = parts[1];
                    String name = parts[2];
                    String surname = parts[3];
                    String role = parts[4];

                    if (role.equalsIgnoreCase("driver")) {
                        return new DriverUser(mail, hashedPassword, name, surname);
                    } else if (role.equalsIgnoreCase("vehicle_owner")) {
                        return new VehicleOwnerUser(mail, hashedPassword, name, surname);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while reading the user file.", e);
        }

        return null;
    }


}
