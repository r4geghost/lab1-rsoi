package ru.dyusov.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Person {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "address")
    private String address;
    @Column(name = "work")
    private String work;

    @Override
    public String toString() {
        return String.format(
                "Person[id=%d, name='%s', age='%s', address='%s', work='%s']",
                id, name, age, address, work);
    }
}
