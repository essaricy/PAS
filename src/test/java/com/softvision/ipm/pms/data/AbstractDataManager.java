package com.softvision.ipm.pms.data;

import java.util.Random;

public interface AbstractDataManager {

	Random RANDOM = new Random();

	void clearData() throws Exception;

	void loadData() throws Exception;

}
