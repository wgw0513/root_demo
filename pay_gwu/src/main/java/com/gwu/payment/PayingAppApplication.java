package com.gwu.payment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.gwu.payment.wechat.util.TokenThread;
/**启动mybatise事务*/
//@EnableScheduling
@SpringBootApplication
public class PayingAppApplication  extends SpringBootServletInitializer implements CommandLineRunner  {
	public static void main(String[] args) {
		SpringApplication.run(PayingAppApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.sources(this.getClass());
		return super.configure(builder);
	
	}

	@Override
	public void run(String... args) throws Exception {
		new Thread(new TokenThread()).start();
	}
	
}