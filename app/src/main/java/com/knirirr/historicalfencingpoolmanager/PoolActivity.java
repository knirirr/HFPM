package com.knirirr.historicalfencingpoolmanager;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;


public class PoolActivity extends ActionBarActivity
{

  public static String TAG = "HFPM Pool Activity";
  private long pool_id;

  private DbxAccountManager mAccountManager;
  private DbxDatastoreManager mDatastoreManager;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bout);

    Bundle extras = getIntent().getExtras();
    if(extras !=null)
    {
      pool_id = extras.getLong("pool_id",0);
    }

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

    if (savedInstanceState == null)
    {
      if (pool_id > 0)
      {
        // The user wants to look at an existing pool.
        AssaultFragment assFrag = new AssaultFragment();
        assFrag.setArguments(extras);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, assFrag, "ass_frag");
        transaction.addToBackStack(null);
        transaction.commit();
      }
      else
      {
        // A new pool must be set up
        Log.i(TAG, "Adding Setup Fragment");
        SetupFragment setFrag = new SetupFragment();
        setFrag.setArguments(extras);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, setFrag, "set_frag");
        transaction.addToBackStack(null);
        transaction.commit();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_bout, menu);
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

    return super.onOptionsItemSelected(item);
  }

  public DbxAccountManager getAccountManager()
  {
    return mAccountManager;
  }

  public DbxDatastoreManager getDatastoreManager()
  {
    return mDatastoreManager;
  }




}
