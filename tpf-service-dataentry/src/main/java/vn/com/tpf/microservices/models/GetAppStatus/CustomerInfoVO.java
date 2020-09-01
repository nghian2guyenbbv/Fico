package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;

@Data
public class CustomerInfoVO{
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String applicantType;
        private String mobileNumber;
        private String phoneNumber;
        private String emailId;
        private String address;
        private String occupation;
        private String customerNumber;
}
