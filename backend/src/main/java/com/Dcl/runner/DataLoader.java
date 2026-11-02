package com.Dcl.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Dcl.entity.Policy;
import com.Dcl.entity.PolicyCategory;
import com.Dcl.entity.User;
import com.Dcl.repo.CategoryRepository;
import com.Dcl.repo.PolicyRepository;
import com.Dcl.repo.UserRepository;

//runs once when the app starts - creates admin login + some demo policies
@Component
public class DataLoader implements CommandLineRunner {

	private static final Logger logger=LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private CategoryRepository crepo;
	
	@Autowired
	private PolicyRepository prepo;
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("DataLoader started - checking default data");
		
		//default admin (admin@gmail.com / admin123)
		if(urepo.findByEmail("admin@gmail.com").isEmpty()) {
			User admin=new User();
			admin.setFullName("Administrator");
			admin.setEmail("admin@gmail.com");
			admin.setPassword("admin123");
			admin.setRole("ADMIN");
			urepo.save(admin);
			logger.info("Default admin created: admin@gmail.com");
		}
		
		//demo data only when there are no categories yet
		if(crepo.count()==0) {
			PolicyCategory health=addCategory("Health", "Medical and hospital cover");
			PolicyCategory life=addCategory("Life", "Life cover for your family");
			PolicyCategory vehicle=addCategory("Vehicle", "Car and bike insurance");
			
			addPolicy(health, "Family Health Plus", 12000.0, 500000.0, 12, "Hospital cover for the whole family.");
			addPolicy(health, "Senior Care", 18000.0, 300000.0, 12, "Health cover for people above 60.");
			addPolicy(life, "Term Life Secure", 9000.0, 5000000.0, 120, "Pays your family if anything happens to you.");
			addPolicy(vehicle, "Car Comprehensive", 8500.0, 600000.0, 12, "Own damage + third party cover for cars.");
			addPolicy(vehicle, "Two-Wheeler Basic", 1500.0, 80000.0, 12, "Third party + theft cover for bikes.");
			logger.info("Demo categories and policies added");
		}
	}
	
	private PolicyCategory addCategory(String name, String description) {
		PolicyCategory c=new PolicyCategory();
		c.setCategoryName(name);
		c.setDescription(description);
		return crepo.save(c);
	}
	
	private void addPolicy(PolicyCategory category, String name, Double premium, Double coverage, Integer months, String description) {
		Policy p=new Policy();
		p.setCategory(category);
		p.setPolicyName(name);
		p.setPremiumAmount(premium);
		p.setCoverageAmount(coverage);
		p.setDurationMonths(months);
		p.setDescription(description);
		p.setStatus("ACTIVE");
		prepo.save(p);
	}
}
