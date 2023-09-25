package ru.dyusov.backend.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class PersonRequest {
    @NotNull
    private String name;
    @NotNull
    @Min(1)
    private int age;
    @NotNull
    private String address;
    @NotNull
    private String work;
}
