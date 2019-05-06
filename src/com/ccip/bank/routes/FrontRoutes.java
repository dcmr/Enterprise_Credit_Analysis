package com.ccip.bank.routes;

import com.ccip.bank.company.CompanyController;
import com.ccip.bank.company.IndexController;
import com.ccip.bank.company.PredictController;
import com.jfinal.config.Routes;

/**
 * @date 2018年3月19日 上午10:52:27 
 */
/**
 * @author Mason
 *
 */
public class FrontRoutes extends Routes{
		
	@Override
	public void config() {
		add("/",IndexController.class);
		add("/company",CompanyController.class);
		add("/predict",PredictController.class);
	}
}

