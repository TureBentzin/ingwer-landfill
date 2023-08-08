package de.bentzin.ingwer.landfill.db.test;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
@Entity
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue
    private int id;

    @Enumerated(EnumType.STRING)
    private @NotNull Type type;

    public void setType(Type type) {
        this.type = type;
    }

    private @NotNull String data;

    public Data(int id, Type type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public Data() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public @NotNull String toString() {
        final StringBuffer sb = new StringBuffer("Data{");
        sb.append("id=").append(id);
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public @NotNull Type getType() {
        return type;
    }
}
