package com.dd.web.carcrawler.entities;

import javax.persistence.*;

@Entity
@Table(name = "Car_engine_sizes")
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    private Model model;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Power(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "\n\t\tPower{" +
                "name='" + name +
                '}';
    }
}
