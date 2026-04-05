package org.fola.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Admin {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
