package de.bentzin.ingwer.landfill.db.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
@Entity
@Table(name = "data")
public class Data {

    @Id
    private int id;


    private String data;

    public Data(int id, String data) {
        this.id = id;
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
    public String toString() {
        final StringBuffer sb = new StringBuffer("Data{");
        sb.append("id=").append(id);
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
