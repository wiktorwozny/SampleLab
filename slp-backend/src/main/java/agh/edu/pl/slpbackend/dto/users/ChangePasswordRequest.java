package agh.edu.pl.slpbackend.dto.users;

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
