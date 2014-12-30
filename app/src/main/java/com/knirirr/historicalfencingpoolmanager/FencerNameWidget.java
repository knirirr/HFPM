package com.knirirr.historicalfencingpoolmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by milo on 30/12/14.
 */
public class FencerNameWidget extends LinearLayout implements Serializable
{
  TextView number_view;
  EditText name_view;
  Long id;

  public FencerNameWidget(Context context, AttributeSet attrs)
  {
    super(context, attrs);

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.widget_fencer_name, this, true);
    name_view = (EditText) findViewById(R.id.fencer_name);
    number_view = (TextView) findViewById(R.id.fencer_number);
  }

  public void setNumber(long number)
  {
    number_view.setText(String.valueOf(number));
    id = number;
  }

  public void setHint(String name)
  {
    name_view.setHint(name);
  }

  public String getName()
  {
    String name = name_view.getText().toString();
    return name;
  }

  public long getNumber()
  {
    long number = Long.valueOf(number_view.getText().toString());
    return number;
  }

}
