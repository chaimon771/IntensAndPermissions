package education.haim.intensandpermissions;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final int CODE_CALL_PHONE = 1;
    private static final int CODE_SMS = 2;
    /* same as:
    private static final int REQUEST_CODE_LOCATION = 2;
     */
    EditText etPhoneNumber;
    EditText smsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        smsContent = (EditText) findViewById(R.id.etSMS);
    }

    String message = "Turn off the oven!";


    public void setTimer(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, 12, 20, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, hourOfDay * 360 + minutes * 60)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, false);

        startActivity(intent);

    }

    public void call(@Nullable View view) {
        Uri phoneUri = Uri.parse("tel:" + etPhoneNumber.getText());
        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);

        //ActivityCompact cheching is that OS less then Android 6 or greather.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE} , CODE_CALL_PHONE);

            return;
        }
        startActivity(callIntent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CODE_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call(null);
                }else {
                    Toast.makeText(this, "No Call For You...", Toast.LENGTH_SHORT).show();
                }
                break;
            case CODE_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendSMS(null);
                }else {
                    Toast.makeText(this, "No SMS For You...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void sendSMS(View view) {
        //1)No requires intent
        SmsManager smsManager = SmsManager.getDefault();




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS} , CODE_SMS);

            return;
        }else{
            smsManager.sendTextMessage("+9720502075202" , null, smsContent.getText().toString(), null, null);
        }
    }
}
