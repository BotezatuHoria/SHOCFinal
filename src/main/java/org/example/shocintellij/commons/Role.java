package org.example.shocintellij.commons;

import java.util.Objects;

public class Role {
    public String role;
    public String content;

    public Role(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(getRole(), role1.getRole()) && Objects.equals(getContent(), role1.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRole(), getContent());
    }
}
