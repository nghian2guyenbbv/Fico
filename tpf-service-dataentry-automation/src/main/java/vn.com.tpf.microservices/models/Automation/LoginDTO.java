package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {
    private String userName;
    private String password;
}
