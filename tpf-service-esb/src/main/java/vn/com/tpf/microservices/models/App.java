package vn.com.tpf.microservices.models;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class App {

	private String project;
	private String applicationId;
	private ApplicationInformation applicationInformation;
	private LoanDetail loanDetails;
	private Set<Reference> references;
	private Set<Map<String, Object>> dynamicForm;
	private Set<Docs> documents;

}
