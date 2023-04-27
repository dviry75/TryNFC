package com.example.trynfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    String onEt = "  ";
    EditText et;

    TextView show;
    private NfcAdapter mNfcAdapter;
    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B |
            NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V | NfcAdapter.FLAG_READER_NFC_BARCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText)findViewById(R.id.et_NFC);

        show = (TextView)findViewById(R.id.tv_show) ;



        // Get the default NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Enable the NFC foreground dispatch
        Bundle options = new Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000);
        mNfcAdapter.enableReaderMode(this, this, READER_FLAGS, options);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable the NFC foreground dispatch
        mNfcAdapter.disableReaderMode(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable the NFC foreground dispatch
        Bundle options = new Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000);
        mNfcAdapter.enableReaderMode(this, this, READER_FLAGS, options);
    }




    @Override
    public void onTagDiscovered(Tag tag) {
        // Handle the NFC tag discovery event
        onEt = et.getText().toString();

        String tagId = byteArrayToHexString(tag.getId());
        Log.d("NFC", "Tag discovered: " + tagId);
        ((Activity)MainActivity.this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "NFC Working", Toast.LENGTH_SHORT).show();
                show.setText(onEt);
                show.setVisibility(View.VISIBLE);
                et.setText(" ");
                et.setHint("save a word");
            }
        });

    }

    // Helper method to convert byte array to hexadecimal string
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


}
