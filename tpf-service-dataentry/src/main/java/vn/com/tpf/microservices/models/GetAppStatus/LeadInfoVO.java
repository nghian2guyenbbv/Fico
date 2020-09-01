package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;

@Data
public class LeadInfoVO{

        private String productType;
        private String loanPurpose;
        private String loanStageName;
        private String assetType;
        private String assetCatagory;
        private String make;
        private String model;
        private String variant;
        private String vehiclePrice;
        private String product;
        private String scheme;
        private String rate;
        private String paymentFrequency;
        private String tenure;
        private String loanAmount;
        private String emi;
        private String currency;
}
