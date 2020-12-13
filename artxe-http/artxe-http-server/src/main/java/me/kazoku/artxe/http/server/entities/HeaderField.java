package me.kazoku.artxe.http.server.entities;

public class HeaderField {
    private final String name;
    private final String value;

    public HeaderField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public HeaderField(String raw) {
        int index;
        if ((index = raw.indexOf(':')) != -1) {
            this.name = raw.substring(0, index);
            this.value = raw.substring(++index).trim();
        } else {
            this.name = "";
            this.value = raw;
        }
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, value);
    }
}
