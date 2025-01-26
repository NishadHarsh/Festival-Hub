package com.event.management;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ReportsActivity extends AppCompatActivity {

    ArrayList<EventHistoryLists> productLists;
    SharedPreferences sp;
    Spinner spinner;
    Button getReport, getAllReport;

    ArrayList<String> arrayEventId;
    ArrayList<String> arrayEventName;

    String sEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        spinner = findViewById(R.id.report_spinner);
        getAllReport = findViewById(R.id.reports_generate_all);
        getReport = findViewById(R.id.reports_generate);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sEventId = arrayEventId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (new ConnectionDetector(ReportsActivity.this).isConnectingToInternet()) {
            new getSpinner().execute();
        } else {
            new ConnectionDetector(ReportsActivity.this).connectiondetect();
        }

        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(ReportsActivity.this).isConnectingToInternet()) {
                    new getFilterExcelData().execute();
                } else {
                    new ConnectionDetector(ReportsActivity.this).connectiondetect();
                }
            }
        });

        getAllReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(ReportsActivity.this).isConnectingToInternet()) {
                    new getProductExcelData().execute();
                } else {
                    new ConnectionDetector(ReportsActivity.this).connectiondetect();
                }
            }
        });

    }

    private class getSpinner extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ReportsActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "getEventCollegeSpinner.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    JSONArray array = object.getJSONArray("response");
                    arrayEventId = new ArrayList<>();
                    arrayEventName = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        arrayEventId.add(jsonObject.getString("id"));
                        arrayEventName.add(jsonObject.getString("name") + " ( " + jsonObject.getString("collegeName") + " )");
                    }
                    ArrayAdapter collegeAdapter = new ArrayAdapter(ReportsActivity.this, android.R.layout.simple_list_item_checked, arrayEventName);
                    spinner.setAdapter(collegeAdapter);
                } else {
                    new ToastIntentClass(ReportsActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getFilterExcelData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ReportsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("eventCollegeId", sEventId);
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "getEventHistoryById.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    productLists = new ArrayList<>();
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        EventHistoryLists lists = new EventHistoryLists();
                        lists.setId(jsonObject.getString("id"));
                        lists.setEcId(jsonObject.getString("ec_id"));
                        lists.setUserId(jsonObject.getString("userId"));
                        lists.setName(jsonObject.getString("name"));
                        lists.setPrice(jsonObject.getString("price"));
                        lists.setTransactionId(jsonObject.getString("transactionId"));
                        if (jsonObject.getString("attendance").equalsIgnoreCase("0")) {
                            lists.setAttendance("Pending");
                        } else if (jsonObject.getString("attendance").equalsIgnoreCase("A")) {
                            lists.setAttendance("Absent");
                        } else if (jsonObject.getString("attendance").equalsIgnoreCase("P")) {
                            lists.setAttendance("Present");
                        } else {
                            lists.setAttendance("Pending");
                        }
                        lists.setCreated_date(jsonObject.getString("created_date"));
                        lists.setCollegeName(jsonObject.getString("collegeName"));
                        lists.setEventName(jsonObject.getString("eventName"));
                        lists.setEventDate(jsonObject.getString("event_date"));
                        lists.setEventTime(jsonObject.getString("event_time"));
                        productLists.add(lists);
                    }
                    saveExcelFile(ReportsActivity.this, "EventRegistrationHistory.xls");
                } else {
                    Toast.makeText(ReportsActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Event Registration History");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("RegistrationId");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Event Name");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("College Name");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Event Date");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Event Time");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Student Name");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Attendence");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Registration Date");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));
        sheet1.setColumnWidth(7, (15 * 500));

        int rowNum = 1;
        for (int i = 0; i < productLists.size(); i++) {
            Row row1 = sheet1.createRow(rowNum++);
            row1.createCell(0).setCellValue(productLists.get(i).getId());
            row1.createCell(1).setCellValue(productLists.get(i).getEventName());
            row1.createCell(2).setCellValue(productLists.get(i).getCollegeName());
            row1.createCell(3).setCellValue(productLists.get(i).getEventDate());
            row1.createCell(4).setCellValue(productLists.get(i).getEventTime());
            row1.createCell(5).setCellValue(productLists.get(i).getName());
            row1.createCell(6).setCellValue(productLists.get(i).getAttendance());
            row1.createCell(7).setCellValue(productLists.get(i).getCreated_date());
        }
        Toast.makeText(context, "Save File In Folder", Toast.LENGTH_SHORT).show();
        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private class getProductExcelData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ReportsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "getEventHistoryByAll.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    productLists = new ArrayList<>();
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        EventHistoryLists lists = new EventHistoryLists();
                        lists.setId(jsonObject.getString("id"));
                        lists.setEcId(jsonObject.getString("ec_id"));
                        lists.setUserId(jsonObject.getString("userId"));
                        lists.setName(jsonObject.getString("name"));
                        lists.setPrice(jsonObject.getString("price"));
                        lists.setTransactionId(jsonObject.getString("transactionId"));
                        if (jsonObject.getString("attendance").equalsIgnoreCase("0")) {
                            lists.setAttendance("Pending");
                        } else if (jsonObject.getString("attendance").equalsIgnoreCase("A")) {
                            lists.setAttendance("Absent");
                        } else if (jsonObject.getString("attendance").equalsIgnoreCase("P")) {
                            lists.setAttendance("Present");
                        } else {
                            lists.setAttendance("Pending");
                        }
                        lists.setCreated_date(jsonObject.getString("created_date"));
                        lists.setCollegeName(jsonObject.getString("collegeName"));
                        lists.setEventName(jsonObject.getString("eventName"));
                        lists.setEventDate(jsonObject.getString("event_date"));
                        lists.setEventTime(jsonObject.getString("event_time"));
                        productLists.add(lists);
                    }
                    saveProductExcelFile(ReportsActivity.this, "AllEventRegistrationHistory.xls");
                } else {
                    Toast.makeText(ReportsActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    boolean saveProductExcelFile(ReportsActivity reportsActivity, String s) {
        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myProductOrder");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("RegistrationId");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Event Name");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("College Name");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Event Date");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Event Time");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Student Name");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Attendence");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Registration Date");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));
        sheet1.setColumnWidth(7, (15 * 500));

        int rowNum = 1;
        for (int i = 0; i < productLists.size(); i++) {
            Row row1 = sheet1.createRow(rowNum++);
            row1.createCell(0).setCellValue(productLists.get(i).getId());
            row1.createCell(1).setCellValue(productLists.get(i).getEventName());
            row1.createCell(2).setCellValue(productLists.get(i).getCollegeName());
            row1.createCell(3).setCellValue(productLists.get(i).getEventDate());
            row1.createCell(4).setCellValue(productLists.get(i).getEventTime());
            row1.createCell(5).setCellValue(productLists.get(i).getName());
            row1.createCell(6).setCellValue(productLists.get(i).getAttendance());
            row1.createCell(7).setCellValue(productLists.get(i).getCreated_date());
        }
        Toast.makeText(ReportsActivity.this, "Save File In Folder", Toast.LENGTH_SHORT).show();
        // Create a path where we will place our List of objects on external storage
        File file = new File(reportsActivity.getExternalFilesDir(null), s);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }
}
