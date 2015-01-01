package com.knirirr.historicalfencingpoolmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by milo on 30/12/14.
 */
public class AssaultFragment extends Fragment
{

  private static String TAG = "HFPM AssaultFragment";
  private String pool_id;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_assault, container, false);
    return rootView;
  }

  @Override
  public void onStart()
  {
    super.onStart();

    pool_id = getArguments().getString("pool_id", "");
    Log.i(TAG, "POOL ID: " + pool_id);
  }

}
