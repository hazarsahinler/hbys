package com.example.demo.Enum;

public enum Role {
    Hasta(0),
    Personel(1);

    private int role;

    private Role(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public static Role fromRole(int role) {
        for (Role role1 : values()) {
            if (role1.getRole() == role) {
                return role1;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + role);
    }
}
