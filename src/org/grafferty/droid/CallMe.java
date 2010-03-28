package org.grafferty.droid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CallMe extends ListActivity {
	
	ListAdapter mAdapter;
	ListView listView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = new ListView(this);
        String[] projection = new String[] {People.NAME.toUpperCase(),People.NUMBER};
        int[] names = new int[] {R.id.row,R.id.row2};
        Uri contacts = People.CONTENT_URI;
        Cursor c = managedQuery(contacts,
        		null,People.NAME + " NOT NULL AND + " +People.NUMBER + " NOT NULL",
        		null,People.NAME + " ASC");
        mAdapter = new SimpleCursorAdapter(this, R.layout.main, c, projection, names);
        setListAdapter(mAdapter);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, final int position, final long id) {
    	super.onListItemClick(l, v, position, id);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Definately want to send a call me?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   	Cursor c = (Cursor)mAdapter.getItem(position);
    	            	String phoneNumber = c.getString(c.getColumnIndex(People.NUMBER));
    	            	SmsManager s = SmsManager.getDefault();
    	            	s.sendTextMessage("50001", null, sanitize(phoneNumber.toString()), null, null);
    	           }
    	       })
    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	builder.show();
    }
    
    public String sanitize(String num) {
    	return num.replaceAll("-", "");
    }
    
}