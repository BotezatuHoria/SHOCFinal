package org.example.shocintellij.commons;

import java.util.List;
import java.util.Objects;

public class JamilaSON {
    public String model;
    List<Role> messages;
    public long max_tokens;

    public JamilaSON(String model, List<Role> messages, long max_tokens) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Role> getMessages() {
        return messages;
    }

    public void setMessages(List<Role> messages) {
        this.messages = messages;
    }

    public long getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(long max_tokens) {
        this.max_tokens = max_tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JamilaSON jamilaSON = (JamilaSON) o;
        return getMax_tokens() == jamilaSON.getMax_tokens() && Objects.equals(getModel(), jamilaSON.getModel()) && Objects.equals(getMessages(), jamilaSON.getMessages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel(), getMessages(), getMax_tokens());
    }
}
