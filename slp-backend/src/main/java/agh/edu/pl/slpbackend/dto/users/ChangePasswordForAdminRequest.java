package agh.edu.pl.slpbackend.dto.users;

public record ChangePasswordForAdminRequest(String newPassword, String email) {
}
