package company.core.model.database;

public class Users {
    private static String id;
    private static String email;
    private static String encrypted_password;
    private static String reset_password_token;
    private static String reset_password_sent_at;
    private static String remember_created_at;
    private static String confirmation_token;
    private static String confirmed_at;
    private static String confirmation_sent_at;
    private static String unconfirmed_email;
    private static int failed_attempts;
    private static String unlock_token;
    private static String locked_at;
    private static String username;
    private static String encrypted_otp_secret;
    private static String encrypted_otp_secret_iv;
    private static String encrypted_otp_secret_salt;
    private static boolean otp_required_for_login;
    private static String otp_backup_codes;
    private static boolean first_login;
    private static int activation_status;
    private static String birthday;
    private static int gender;
    private static String referral_token;
    private static int maximum_attempts;
    private static String first_name;
    private static String last_name;
    private static int status;
    private static String created_at;
    private static String updated_at;
    private static int consumed_timestep;
    private static String anti_phishing_code;
    private static String image;
    private static String role_user_id;
    private static String phone;
    private static int kyc_level;
    private static String nationality;
    private static String address;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Users.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Users.email = email;
    }

    public static String getEncrypted_password() {
        return encrypted_password;
    }

    public static void setEncrypted_password(String encrypted_password) {
        Users.encrypted_password = encrypted_password;
    }

    public static String getReset_password_token() {
        return reset_password_token;
    }

    public static void setReset_password_token(String reset_password_token) {
        Users.reset_password_token = reset_password_token;
    }

    public static String getReset_password_sent_at() {
        return reset_password_sent_at;
    }

    public static void setReset_password_sent_at(String reset_password_sent_at) {
        Users.reset_password_sent_at = reset_password_sent_at;
    }

    public static String getRemember_created_at() {
        return remember_created_at;
    }

    public static void setRemember_created_at(String remember_created_at) {
        Users.remember_created_at = remember_created_at;
    }

    public static String getConfirmation_token() {
        return confirmation_token;
    }

    public static void setConfirmation_token(String confirmation_token) {
        Users.confirmation_token = confirmation_token;
    }

    public static String getConfirmed_at() {
        return confirmed_at;
    }

    public static void setConfirmed_at(String confirmed_at) {
        Users.confirmed_at = confirmed_at;
    }

    public static String getConfirmation_sent_at() {
        return confirmation_sent_at;
    }

    public static void setConfirmation_sent_at(String confirmation_sent_at) {
        Users.confirmation_sent_at = confirmation_sent_at;
    }

    public static String getUnconfirmed_email() {
        return unconfirmed_email;
    }

    public static void setUnconfirmed_email(String unconfirmed_email) {
        Users.unconfirmed_email = unconfirmed_email;
    }

    public static int getFailed_attempts() {
        return failed_attempts;
    }

    public static void setFailed_attempts(int failed_attempts) {
        Users.failed_attempts = failed_attempts;
    }

    public static String getUnlock_token() {
        return unlock_token;
    }

    public static void setUnlock_token(String unlock_token) {
        Users.unlock_token = unlock_token;
    }

    public static String getLocked_at() {
        return locked_at;
    }

    public static void setLocked_at(String locked_at) {
        Users.locked_at = locked_at;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Users.username = username;
    }

    public static String getEncrypted_otp_secret() {
        return encrypted_otp_secret;
    }

    public static void setEncrypted_otp_secret(String encrypted_otp_secret) {
        Users.encrypted_otp_secret = encrypted_otp_secret;
    }

    public static String getEncrypted_otp_secret_iv() {
        return encrypted_otp_secret_iv;
    }

    public static void setEncrypted_otp_secret_iv(String encrypted_otp_secret_iv) {
        Users.encrypted_otp_secret_iv = encrypted_otp_secret_iv;
    }

    public static String getEncrypted_otp_secret_salt() {
        return encrypted_otp_secret_salt;
    }

    public static void setEncrypted_otp_secret_salt(String encrypted_otp_secret_salt) {
        Users.encrypted_otp_secret_salt = encrypted_otp_secret_salt;
    }

    public static boolean isOtp_required_for_login() {
        return otp_required_for_login;
    }

    public static void setOtp_required_for_login(boolean otp_required_for_login) {
        Users.otp_required_for_login = otp_required_for_login;
    }

    public static String getOtp_backup_codes() {
        return otp_backup_codes;
    }

    public static void setOtp_backup_codes(String otp_backup_codes) {
        Users.otp_backup_codes = otp_backup_codes;
    }

    public static boolean isFirst_login() {
        return first_login;
    }

    public static void setFirst_login(boolean first_login) {
        Users.first_login = first_login;
    }

    public static int getActivation_status() {
        return activation_status;
    }

    public static void setActivation_status(int activation_status) {
        Users.activation_status = activation_status;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static void setBirthday(String birthday) {
        Users.birthday = birthday;
    }

    public static int getGender() {
        return gender;
    }

    public static void setGender(int gender) {
        Users.gender = gender;
    }

    public static String getReferral_token() {
        return referral_token;
    }

    public static void setReferral_token(String referral_token) {
        Users.referral_token = referral_token;
    }

    public static int getMaximum_attempts() {
        return maximum_attempts;
    }

    public static void setMaximum_attempts(int maximum_attempts) {
        Users.maximum_attempts = maximum_attempts;
    }

    public static String getFirst_name() {
        return first_name;
    }

    public static void setFirst_name(String first_name) {
        Users.first_name = first_name;
    }

    public static String getLast_name() {
        return last_name;
    }

    public static void setLast_name(String last_name) {
        Users.last_name = last_name;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        Users.status = status;
    }

    public static String getCreated_at() {
        return created_at;
    }

    public static void setCreated_at(String created_at) {
        Users.created_at = created_at;
    }

    public static String getUpdated_at() {
        return updated_at;
    }

    public static void setUpdated_at(String updated_at) {
        Users.updated_at = updated_at;
    }

    public static int getConsumed_timestep() {
        return consumed_timestep;
    }

    public static void setConsumed_timestep(int consumed_timestep) {
        Users.consumed_timestep = consumed_timestep;
    }

    public static String getAnti_phishing_code() {
        return anti_phishing_code;
    }

    public static void setAnti_phishing_code(String anti_phishing_code) {
        Users.anti_phishing_code = anti_phishing_code;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        Users.image = image;
    }

    public static String getRole_user_id() {
        return role_user_id;
    }

    public static void setRole_user_id(String role_user_id) {
        Users.role_user_id = role_user_id;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Users.phone = phone;
    }

    public static int getKyc_level() {
        return kyc_level;
    }

    public static void setKyc_level(int kyc_level) {
        Users.kyc_level = kyc_level;
    }

    public static String getNationality() {
        return nationality;
    }

    public static void setNationality(String nationality) {
        Users.nationality = nationality;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Users.address = address;
    }


}
