package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class LoanDetail {

	private SourcingDetail sourcingDetails;
	private VapDetail vapDetails;

}
