package com.example.smsbooster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSend, btnStop;
    private EditText editNumber, editMsg, editNum;
    boolean stop = false;
    private int SMS_PERMISSION_CODE=33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnStop = (Button) findViewById(R.id.btnStop);

        editNumber = (EditText) findViewById(R.id.editNumber);
        editMsg = (EditText) findViewById(R.id.editMsg);
        editNum = (EditText) findViewById(R.id.editNum);

        btnStop.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double tap to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSend:
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) ==PackageManager.PERMISSION_GRANTED)
                {
                    try {
                        sendNumberOfMesages(editNumber.getText().toString(), editMsg.getText().toString(), Integer.parseInt(editNum.getText().toString()));
                    }
                    catch (Exception e) {
                        Toast.makeText(this, "Fill data correctly", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    requestPermission();
                }
                break;
            case R.id.btnStop:
                stop = true;
                break;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
        {
            new AlertDialog.Builder(this).setTitle("Permission required")
                    .setMessage("SMS Booster needed permission to send SMS")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            .create()
            .show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendNumberOfMesages(String phoneNumber, String message, int count) {
        for (int i=0; i<count; i++)
        {
            if(sendMessage(phoneNumber, message))
            {
                Toast.makeText(this, count+" message, sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, count+" message, not sent", Toast.LENGTH_SHORT).show();
            }
        }
        editNum.setText("");
        editMsg.setText("");
        editNumber.setText("");
    }

    private boolean sendMessage(String phoneNumber, String message)
    {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null,message, null, null);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
