package com.gwu.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="sys/user")
public class SysUserController {
	
	@RequestMapping(value="/test")
	public Object test() {
		
		
		return null;
	}

}
