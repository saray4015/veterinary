package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public abstract String getPersonType();
}
