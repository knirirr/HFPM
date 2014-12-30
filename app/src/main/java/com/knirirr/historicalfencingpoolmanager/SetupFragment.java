package com.knirirr.historicalfencingpoolmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


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
        Log.i(TAG, "Adding fencer: " + big_shots[i]);
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
}
