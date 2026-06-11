package jakarta_configuration.resources;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mindrot.jbcrypt.BCrypt;

@Converter(autoApply = false)
public class PasswordConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String password) {
        if (password == null) {
            return null;
        }

        /*
         * Se è già un hash BCrypt, non lo hasho di nuovo.
         * Serve per evitare doppio hash in caso di merge/update.
         */
        if (isBCryptHash(password)) {
            return password;
        }

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

    public static boolean checkPassword(String passwordChiara, String passwordHash) {
        if (passwordChiara == null || passwordHash == null) {
            return false;
        }

        return BCrypt.checkpw(passwordChiara, passwordHash);
    }

    private static boolean isBCryptHash(String value) {
        return value.startsWith("$2a$")
                || value.startsWith("$2b$")
                || value.startsWith("$2y$");
    }
}