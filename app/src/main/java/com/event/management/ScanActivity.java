package com.event.management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    String sId;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        sId = result.getText();
        if (new ConnectionDetector(ScanActivity.this).isConnectingToInternet()) {
            new addAttendence().execute();
        } else {
            new ConnectionDetector(ScanActivity.this).connectiondetect();
        }
        /*Log.d("RESPONSE", result.getText());
        onBackPressed();*/
    }

    private class addAttendence extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ScanActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", sId);
            hashMap.put("volunteerId", sp.getString(ConstantSp.ID, ""));
            hashMap.put("attendance", "P");
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "addAttendance.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    new ToastIntentClass(ScanActivity.this, object.getString("Message"));
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*@Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        //MainActivity.tvresult.setText(rawResult.getText());
        Log.d("RESPONSE",rawResult.getText());
        onBackPressed();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }*/
}