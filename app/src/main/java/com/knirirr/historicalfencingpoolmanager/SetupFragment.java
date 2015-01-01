package com.knirirr.historicalfencingpoolmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;


/**
 * Created by milo on 30/12/14.
 */
public class SetupFragment extends Fragment
{
  public static String TAG = "HFPM Setup Fragment";
  LinearLayout new_names_here;
  Button new_fencer;
  Button start_fencing;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
    return rootView;
  }

  @Override
  public void onStart()
  {
    super.onStart();

    new_names_here = (LinearLayout) getView().findViewById(R.id.new_names_here);
    int count = new_names_here.getChildCount();
    if (count == 0)
    {
      // replace the max. here with a value from settings, eventually
      String[] big_shots = getResources().getStringArray(R.array.big_shots);
      FencerNameWidget fnw;
      for (int i = 0; i <= 4; i++)
      {
        //Log.i(TAG, "Adding fencer: " + big_shots[i]);
        fnw = new FencerNameWidget(getView().getContext(), null);
        fnw.setHint(big_shots[i]);
        fnw.setNumber(i + 1);
        new_names_here.addView(fnw);
      }
    }

    new_fencer = (Button) getActivity().findViewById(R.id.new_fencer);
    new_fencer.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        switch (v.getId())
        {
          case R.id.new_fencer:
            addFencer();
            Log.i(TAG, "Added fencer?");
            break;
        }
      }
    });

    start_fencing = (Button) getActivity().findViewById(R.id.start_fencing);
    start_fencing.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        switch (v.getId())
        {
          case R.id.start_fencing:
            Toast.makeText(getActivity(), "Start fencing!", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Started fencing?");
            String pool_id = createPool();
            if (!TextUtils.isEmpty(pool_id))
            {
              AssaultFragment assFrag = new AssaultFragment();
              Bundle bundle = new Bundle();
              bundle.putString("pool_id", pool_id);
              assFrag.setArguments(bundle);
              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.replace(R.id.container, assFrag, "ass_frag");
              transaction.addToBackStack(null);
              transaction.commit();
            }
            break;
        }
      }
    });

  }

  public void addFencer()
  {
    String[] big_shots = getResources().getStringArray(R.array.big_shots);
    new_names_here = (LinearLayout) getView().findViewById(R.id.new_names_here);
    int count = new_names_here.getChildCount();

    // At some point we ought to stop - surely a pool should not have more than 12 people in it?
    if (count > 14)
    {
      Toast.makeText(getActivity(), getString(R.string.too_big), Toast.LENGTH_SHORT).show();
      return;
    }

    FencerNameWidget fnw = new FencerNameWidget(getView().getContext(),null);
    fnw.setNumber(count + 1);
    // catch the exception if we've run out of names...
    if (count >= big_shots.length)
    {
      fnw.setHint(getString(R.string.run_out));
    }
    else
    {
      fnw.setHint(big_shots[count]);
    }
    new_names_here.addView(fnw);
  }

  public String createPool()
  {
    // Connnect to the datastore:
    DbxDatastore datastore = null;
    try
    {
      datastore = ((PoolActivity) getActivity()).getDatastoreManager().openDefaultDatastore();
    }
    catch (DbxException e)
    {
      Toast.makeText(getActivity(), "FRC", Toast.LENGTH_SHORT).show();
      Log.e(TAG, "Drobox exception! " + e.toString());
      return "";
    }

    // Create a pool object:
    DbxTable poolTable = datastore.getTable("pools");
    DbxTable userTable = datastore.getTable("users");
    // a bout table will be created once one starts fencing, with a new record entered for each bout
    EditText pool_name = (EditText) getView().findViewById(R.id.pool_name);
    String poolTitle = pool_name.getText().toString();
    if (TextUtils.isEmpty(poolTitle))
    {
      Toast.makeText(getActivity(), getString(R.string.name_please), Toast.LENGTH_SHORT).show();
      return "";
    }

    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();
    DbxRecord pool = poolTable.insert()
        .set("date", ts)
        .set("title", poolTitle);


    // Go through the list of users and create users for this pool.
    int childcount = new_names_here.getChildCount();
    // First, check some names have been supplied. There should be at least three fencers in a pool.
    int total = 0;
    for (int i=0; i < childcount; i++)
    {
      FencerNameWidget fnw = (FencerNameWidget) new_names_here.getChildAt(i);
      if (!TextUtils.isEmpty(fnw.getName()))
      {
        total++;
      }
    }

    if (total < 3)
    {
      Toast.makeText(getActivity(), getString(R.string.not_enough_users), Toast.LENGTH_SHORT).show();
      Log.i(TAG, "Not enough users in this pool.");
      return "";
    }

    // Then, actually create the entries.
    for (int j=0; j < childcount; j++)
    {
      FencerNameWidget fnw = (FencerNameWidget) new_names_here.getChildAt(j);
      if (!TextUtils.isEmpty(fnw.getName()))
      {
        Log.i(TAG,"Creating: " + fnw.getName());
        // These are the final summaries, for reference. A Bouts table will record each bout.
        DbxRecord user = userTable.insert()
            .set("pool", pool.getId())
            .set("number",fnw.getNumber())
            .set("name",fnw.getName())
            .set("hits_for",0)
            .set("hits_against",0)
            .set("indicators",0)
            .set("doubles",0)
            .set("position", 0)
            .set("victories",0);
      }
    }
    try
    {
      datastore.sync();
      String pool_id = pool.getId();
      datastore.close();
      return pool_id;
    }
    catch (DbxException e)
    {
      Toast.makeText(getActivity(), "FRC", Toast.LENGTH_SHORT).show();
      Log.e(TAG, "Another Dropbox exception! " + e.toString());
      return "";
    }
  }

}
