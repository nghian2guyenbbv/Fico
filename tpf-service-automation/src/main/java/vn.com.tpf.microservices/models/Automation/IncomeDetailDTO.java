package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailDTO implements Serializable {
	private String incomeHead;
	private String frequency;
	private String amount;
	private String percentage;
	private String modeOfPayment;
	private String dayOfSalaryPayment;
}
