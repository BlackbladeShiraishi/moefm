package com.github.blackbladeshiraishi.fm.moe.client.android.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.github.blackbladeshiraishi.fm.moe.client.android.R

public class RadioActivity extends AppCompatActivity {

  static String EXTRA_NAME_RADIO_ID = "${RadioActivity.name}.extra.name.radio_id"

  String radioId

  TextView titleView

  static Intent buildIntent(Context context, long radioId) {
    def intent = new Intent(context, RadioActivity)
    intent.putExtra(EXTRA_NAME_RADIO_ID, radioId)
    return intent
  }

  static void startThis(Context context, long radioId) {
    context.startActivity(buildIntent(context, radioId))
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_radio)

    if (intent.hasExtra(EXTRA_NAME_RADIO_ID)) {
      radioId = intent.extras.getLong(EXTRA_NAME_RADIO_ID)
    } else {
      throw new IllegalStateException("neet extra: $EXTRA_NAME_RADIO_ID")
    }

    titleView = findViewById(R.id.title) as TextView

    titleView.text = "id: $radioId"
  }

}
