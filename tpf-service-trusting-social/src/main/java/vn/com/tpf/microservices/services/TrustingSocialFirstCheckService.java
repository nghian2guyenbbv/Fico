package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.TrustingSocialFirstCheck;

@Service
public class TrustingSocialFirstCheckService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ApiService apiService;



	public Map<String, Object> firstCheck(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		TrustingSocialFirstCheck body = mapper.treeToValue(request.get("body"), TrustingSocialFirstCheck.class);

		Assert.hasText(body.getPhoneNumber(), "phoneNumber is required");
		Assert.hasText(body.getNationalId(), "nationalId is required");
		Assert.hasText(body.getFirstName(), "firstName is required");
		Assert.hasText(body.getMiddleName(), "middleName is required");
		Assert.hasText(body.getLastName(), "lastName is required");
		Assert.notNull(body.getDob(), "dob is required");
		Assert.hasText(body.getGender(), "gender is required");
		Assert.hasText(body.getProvinceCode(), "provinceCode is required");
		Assert.hasText(body.getDistrictCode(), "districtCode is required");
		Assert.hasText(body.getAddressNo(), "addressNo is required");

		TrustingSocialFirstCheck entity = TrustingSocialFirstCheck.builder().phoneNumber(body.getPhoneNumber())
				.nationalId(body.getNationalId()).firstName(body.getFirstName()).middleName(body.getMiddleName())
				.lastName(body.getLastName()).dob(body.getDob()).gender(body.getGender())
				.provinceCode(body.getProvinceCode()).districtCode(body.getDistrictCode())
				.addressNo(body.getAddressNo()).build();

		mongoTemplate.save(entity);
		
		apiService.CallApi(request);
		
		return Map.of("status", 201, "data", entity);
	}

}