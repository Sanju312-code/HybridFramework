package com.ActionKeywords;

import org.openqa.selenium.WebDriver;

import com.Utils.PropertyUtil;
import com.reporting.ExtentManager;

public class Testcase_functions {

	public static String userstoryname;

	WebDriver driver = ExtentManager.getDriver();
	public void login() throws Exception {

		ActionKeywords.navigate("url");
		Thread.sleep(5000);
		ActionKeywords.click("username");
		ActionKeywords.enterText("username", "username", false);
		ActionKeywords.click("signbutton");
		Thread.sleep(2000);
		//ActionKeywords.logStep("login page");
		ActionKeywords.logToBothReports("login page", "login page",driver , true);
		ActionKeywords.click("password");
		ActionKeywords.enterText("password", "password", false);
		ActionKeywords.click("signbutton");
		Thread.sleep(5000);

		// ActionKeywords.validateText("homepage",
	}

	public void logout() throws Exception {

		Thread.sleep(2000);
		ActionKeywords.click("profileicon");
		ActionKeywords.waitUntilElementPresent("logoutbutton");
		ActionKeywords.click("logoutbutton");
		ActionKeywords.waitUntilElementPresent("Simplifyqaloginpage");
		//ActionKeywords.logStep("Simplify QA login Page");
	}

	public void nagivationToRequirementManagement() throws Exception {
		ActionKeywords.moveToElement("requirementmanagementmodule");
		ActionKeywords.click("requirementmanagementmodule");
		//ActionKeywords.logStep("Requirement Management Module Page");
		Thread.sleep(2000);
		ActionKeywords.waitUntilElementPresent("requirementmanagementheader");
	}

	public void nagivationToRequirementManagementCreateUserStory() throws Exception {
		ActionKeywords.click("userstorycreatebutton");
		ActionKeywords.click("createuserstorybutton");
		Thread.sleep(2000);
		ActionKeywords.validateText("createuserstoryvalidation", PropertyUtil.getProperty("createuserstoryvalidation"));
		//ActionKeywords.logStep("createuserstoryvalidation");
		Thread.sleep(3000);
	}

	public void CreateUserStory() throws Exception {
		userstoryname = ActionKeywords.generateRandomString(8);
		ActionKeywords.enterText("userstorynamefield", userstoryname, true);
		ActionKeywords.click("userstoryunassignedfield");
		ActionKeywords.enterText("unassignedfieldsearch", "unassignedfiled", false);
		ActionKeywords.click("selectunassigned");

		//ActionKeywords.logStep("selectunassigned");

		ActionKeywords.enterText("usacceptancecriteriafield", "usacceptancecriteria", false);
		Thread.sleep(1000);
		ActionKeywords.enterText("usselectmodulefield", "usselectmoduleselect", false);
		Thread.sleep(1000);
		ActionKeywords.click("usselectmodulefield");
		ActionKeywords.click("selectmodulename");

		//ActionKeywords.logStep("selectmodulename");

		ActionKeywords.click("ussavebutton");
		ActionKeywords.validatePartialText("ussuccessmessage", "ussucessmessage");
		Thread.sleep(3000);
		ActionKeywords.enterText("projectpagenamesearchbox", userstoryname, true);
		Thread.sleep(3000);
		ActionKeywords.waitUntilDynamicElementPresent("Userstoryname", userstoryname);
		Thread.sleep(3000);

	}

	public void deleteUserstory() throws Exception {
		ActionKeywords.click("usid");
		Thread.sleep(3000);
		ActionKeywords.click("usdeleteicon");
		Thread.sleep(3000);
		ActionKeywords.click("confirmDeletebutton");
		Thread.sleep(3000);
		ActionKeywords.enterText("projectpagenamesearchbox", userstoryname, true);
		ActionKeywords.waitUntilDynamicElementNotPresent("Userstoryname", userstoryname);

	}

	public void userstorydragdrop() throws Exception {
		ActionKeywords.moveToElement("myboardmodule");
		ActionKeywords.click("myboardmodule");
		//ActionKeywords.logStep("My Board Page");
		ActionKeywords.click("myboarduserstory");
		Thread.sleep(5000);
		ActionKeywords.performDragAndDrop("dragpoint", "droppoint");
		Thread.sleep(5000);
	}

	public void nagivationtoepic() throws Exception {
		ActionKeywords.moveToElement("layoutfield");
		ActionKeywords.click("layoutfield");
		//ActionKeywords.logStep("My layout Page");
		ActionKeywords.click("epicclick");
		Thread.sleep(3000);

	}

	public void switchtoadmin() throws Exception {
		ActionKeywords.click("profileicon");
		ActionKeywords.clickIfElementIsPresent("switchtoadminbutton");
		Thread.sleep(2000);
		ActionKeywords.waitUntilElementPresent("adminpagevalidation");
		//ActionKeywords.logStep("Admin page");
		ActionKeywords.click("profileicon");
	}
	
	public void switchtouser() throws Exception {
		ActionKeywords.click("profileicon");
		ActionKeywords.clickIfElementIsPresent("switchtouserbutton");
		Thread.sleep(2000);
		ActionKeywords.validateText("homepage",PropertyUtil.getProperty("homepage"));
		Thread.sleep(1000);
		ActionKeywords.click("profileicon");
	}

	public void epicdraganddropleft() throws Exception {
		ActionKeywords.performDragAndDrop("dragpoint", "droplistforepic");
		Thread.sleep(1000);
		ActionKeywords.waitUntilElementPresent("epicaddtextfieldvalidation");
		//ActionKeywords.logStep("Add a text field Popup");
		Thread.sleep(1000);
		ActionKeywords.click("cancelbutton");
	}

	public void epicdraganddroptoptobottom() throws Exception {
		//ActionKeywords.logStep("Before drag and drop");
		ActionKeywords.performDragAndDrop("epicdragdown", "epicdropdown");
		Thread.sleep(1000);
		ActionKeywords.click("savebutton");
		Thread.sleep(1000);
		
		ActionKeywords.waitUntilElementPresent("successmessageforepic");
		Thread.sleep(3000);

	}

}
