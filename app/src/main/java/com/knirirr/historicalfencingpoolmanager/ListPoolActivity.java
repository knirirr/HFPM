/**
 * Created by milo on 30/12/14.
 */
package com.knirirr.historicalfencingpoolmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;


public class ListPoolActivity extends ActionBarActivity
{

  private static String TAG = "Historical Fencing (List)";

  private DbxAccountManager mAccountManager;
  private DbxDatastoreManager mDatastoreManager;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    // Set up the account manager
    mAccountManager = DbxAccountManager.getInstance(getApplicationContext(),
        getString(R.string.app_key),
        getString(R.string.app_secret));

    // Set up the datastore manager
    if (mAccountManager.hasLinkedAccount())
    {
      try
      {
        // Use Dropbox datastores
        mDatastoreManager = DbxDatastoreManager.forAccount(mAccountManager.getLinkedAccount());
      }
      catch (DbxException.Unauthorized e)
      {
        System.out.println("Account was unlinked remotely");
      }
    }
    if (mDatastoreManager == null)
    {
      // Account isn't linked yet, use local datastores
      mDatastoreManager = DbxDatastoreManager.localManager(mAccountManager);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings)
    {
      return true;
    }
    else if (id == R.id.new_pool)
    {
      Log.i(TAG, "New pool!");
      Toast.makeText(ListPoolActivity.this, "Time to create a new pool!", Toast.LENGTH_SHORT).show();
      Intent i = new Intent(this, PoolActivity.class);
      this.startActivity(i);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
