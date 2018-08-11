package spero.shariq.com.myop_restretro1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MessagePage extends AppCompatActivity {
    TextView GetTextView;
    EditText editTextPut;
    String url="http://myop.pythonanywhere.com/api/connect/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        GetTextView = findViewById(R.id.GetTextView);
        editTextPut = findViewById(R.id.editTextPut);
    }
}
