package model;

import enums.UserRole;

public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}