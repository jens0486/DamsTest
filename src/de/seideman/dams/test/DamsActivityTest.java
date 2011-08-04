package de.seideman.dams.test;

import de.seideman.dams.activities.Dams;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DamsActivityTest extends ActivityInstrumentationTestCase2<Dams> {

	private Dams damsActivity;
	
	private EditText searchText;
	
	private Button searchButton;
	private Spinner spin1;
	private Spinner spin2;
	public static final int TEST_STATE_PAUSE_POS1 = 1;
	public static final int TEST_STATE_PAUSE_POS2 = 4;
	public static final String TEST_STATE_PAUSE_POS3 = "PAUSE";
	public static final int TEST_STATE_DEST_POS1 = 1;
	public static final int TEST_STATE_DEST_POS2 = 3;
	public static final String TEST_STATE_DEST_POS3 = "DESTROY";
	public static final String TEST_STATE_RESULT_POS1 = "SCAN";
	
	public DamsActivityTest(){
		super("de.seideman.dams.activities", Dams.class);
	}

	protected void setUp() throws Exception{
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		damsActivity = getActivity();
		spin1 = (Spinner) damsActivity.findViewById(de.seideman.dams.activities.R.id.spinner1);
		spin2 = (Spinner) damsActivity.findViewById(de.seideman.dams.activities.R.id.spinner2);
		searchText = (EditText) damsActivity.findViewById(de.seideman.dams.activities.R.id.textSearchValue);
		searchButton = (Button) damsActivity.findViewById(de.seideman.dams.activities.R.id.btnSearch);
	}

	public void testPreCondition(){
		assertNotNull(damsActivity);
		assertNotNull(spin1);
		assertNotNull(spin2);
		assertNotNull(searchText);
		assertNotNull(searchButton);
	}
	
	public void testOrientation(){
			
		int x1 = spin1.getScrollX();
		int y1 = spin1.getScrollY();
		
		int x2 = spin2.getScrollX();
		int y2 = spin2.getScrollY();
		
		int x3 = searchText.getScrollX();
		int y3 = searchText.getScrollY();
		
		int x4 = searchButton.getScrollX();
		int y4 = searchButton.getScrollY();
		
		int orientation = damsActivity.getRequestedOrientation();
		
		damsActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		assertNotSame(orientation,damsActivity.getRequestedOrientation());
		assertEquals(x1,spin1.getScrollX());
		assertEquals(y1,spin1.getScrollY());
		assertEquals(x2,spin2.getScrollX());
		assertEquals(y2,spin2.getScrollY());
		assertEquals(x3,searchText.getScrollX());
		assertEquals(y3,searchText.getScrollY());
		assertEquals(x4,searchButton.getScrollX());
		assertEquals(y4,searchButton.getScrollY());
	}
	
	@UiThreadTest
	public void testOnPause(){
		Instrumentation inst = this.getInstrumentation();
		
		spin1.setSelection(TEST_STATE_PAUSE_POS1);
		spin2.setSelection(TEST_STATE_PAUSE_POS2);
		searchText.setText(TEST_STATE_PAUSE_POS3);
		
		inst.callActivityOnPause(damsActivity);
		
		spin1.setSelection(0);
		spin2.setSelection(3);
		searchText.setText("");
		
		inst.callActivityOnResume(damsActivity);
			
		assertEquals(TEST_STATE_PAUSE_POS1,spin1.getSelectedItemPosition());
		assertEquals(TEST_STATE_PAUSE_POS2,spin2.getSelectedItemPosition());
		assertEquals(TEST_STATE_PAUSE_POS3,searchText.getText().toString());
		
	}
	
	@UiThreadTest
	public void testOnDestroy(){
				
		spin1 = (Spinner) damsActivity.findViewById(de.seideman.dams.activities.R.id.spinner1);
		spin2 = (Spinner) damsActivity.findViewById(de.seideman.dams.activities.R.id.spinner2);
		searchText = (EditText)damsActivity.findViewById(de.seideman.dams.activities.R.id.textSearchValue);
		
		spin1.setSelection(TEST_STATE_DEST_POS1);
		spin2.setSelection(TEST_STATE_DEST_POS2);
		searchText.setText(TEST_STATE_DEST_POS3);
		
		damsActivity.finish();
		damsActivity = getActivity();
					
		assertEquals(TEST_STATE_DEST_POS1,spin1.getSelectedItemPosition());
		assertEquals(TEST_STATE_DEST_POS2,spin2.getSelectedItemPosition());
		assertEquals(TEST_STATE_DEST_POS3,searchText.getText().toString());
		
	}
	
	@UiThreadTest
	public void testScanResult(){
		
		Intent intent = new Intent("TEST");
		//intent.putExtra("RESULT_OK", true);
		intent.putExtra("SCAN_RESULT", TEST_STATE_RESULT_POS1);
		searchText = (EditText)damsActivity.findViewById(de.seideman.dams.activities.R.id.textSearchValue);
		
		//-1 RESULT_OK
		damsActivity.onActivityResult(0, -1, intent);
							
		assertEquals(TEST_STATE_RESULT_POS1,searchText.getText().toString());
		
	}

	public void testPermission(){
		
		int network = damsActivity.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE");
		int internet = damsActivity.checkCallingOrSelfPermission("android.permission.INTERNET");
		
		//0, if permission exists
		assertEquals(0,network);
		assertEquals(0,internet);
				
	}
	
	@UiThreadTest
	public void testItemSelected(){
				
		assertTrue(spin1.isEnabled());
		assertTrue(spin2.isEnabled());
		
		damsActivity.onItemSelected(spin1, null, 0, 1);
		assertFalse(spin2.isEnabled());
		
		damsActivity.onItemSelected(spin1, null, 0, 0);
		assertTrue(spin2.isEnabled());
	}
}
