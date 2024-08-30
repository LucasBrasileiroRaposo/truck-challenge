package com.challenge.truckManagement;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@SelectPackages({
		"com.challenge.truckManagement.services.controllers",
		"com.challenge.truckManagement.services"
})
@Suite
class TruckManagementApplicationTests {

}
