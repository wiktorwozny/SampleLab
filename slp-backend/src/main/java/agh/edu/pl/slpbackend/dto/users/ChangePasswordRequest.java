package agh.edu.pl.slpbackend.dto.users;

public record ChangePasswordRequest(String email, String oldPassword, String newPassword) {
}
