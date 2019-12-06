package com.dd.web.carcrawler.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Car_manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private boolean top;

    @OneToMany(
            mappedBy = "manufacturer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Model> models;

    public Manufacturer() {
    }

    public Manufacturer(String name) {
        this.name = name;
    }
    public Manufacturer(String name, boolean top) {
        this.name = name;
        this.top = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model){
        if (this.models == null) {
            this.models = new ArrayList<>();
        }
        this.models.add(model);
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "name='" + name + '\'' +
                ", models=" + models +
                '}';
    }
}
