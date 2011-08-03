package de.seideman.dams.test;

import de.seideman.dams.activities.Login;
import de.seideman.dams.exceptions.EmptyInputException;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.UiThreadTest;
import android.test.mock.MockApplication;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<Login> {

	private Login loginActivity;
	
	private EditText userText;
	private EditText passText;
	private Button loginButton;
	public static final String TEST_STATE_USER = "USER";
	public static final String TEST_STATE_PASS = "PASS";
	
	public LoginActivityTest(){
		super("de.seideman.dams.activities", Login.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		loginActivity = getActivity();
		
		userText = (EditText) loginActivity.findViewById(de.seideman.dams.activities.R.id.textUser);
		passText = (EditText) loginActivity.findViewById(de.seideman.dams.activities.R.id.textPass);
		loginButton = (Button) loginActivity.findViewById(de.seideman.dams.activities.R.id.btnLogin);
			
	}
	
	public void testPreCondition(){
		
		assertNotNull(userText);
		assertNotNull(passText);
		assertNotNull(loginButton);
	}
	
	
	public void testCheckWebService(){
		loginActivity.setNet(null);
		
		try{
			loginActivity.startMainActivity();
			fail("Should throw Exception <NetworkManager is NULL>");
		}catch(NullPointerException ex){
			assertTrue(ex.toString(),true);
		} catch (EmptyInputException e) {
			assertTrue(e.toString(),true);
		}		
	}
	
	@UiThreadTest
	public void testEmptyText(){
		userText.setText("");
		passText.setText("");
		
		try{
			loginActivity.startMainActivity();
			fail("Should throw Exception <Empty TextFields>");
		}catch(EmptyInputException ex){
			assertTrue(ex.toString(),true);
		}
	}
	
	@UiThreadTest
	public void testOrientation(){
			
		int x1 = passText.getScrollX();
		int y1 = passText.getScrollY();
		
		int x2 = userText.getScrollX();
		int y2 = userText.getScrollY();
		
		int x3 = loginButton.getScrollX();
		int y3 = loginButton.getScrollY();
		
		loginActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		assertEquals(x1,passText.getScrollX());
		assertEquals(y1,passText.getScrollY());
		assertEquals(x2,userText.getScrollX());
		assertEquals(y2,userText.getScrollY());
		assertEquals(x3,loginButton.getScrollX());
		assertEquals(y3,loginButton.getScrollY());
	}
	
	public void testPermission(){
		
		int network = loginActivity.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE");
		int internet = loginActivity.checkCallingOrSelfPermission("android.permission.INTERNET");
		
		//0, if permission exists
		assertEquals(0,network);
		assertEquals(0,internet);
				
	}
}
