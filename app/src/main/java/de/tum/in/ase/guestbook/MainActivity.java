package de.tum.in.ase.guestbook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import org.restlet.resource.ClientResource;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guestbook_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_download:
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        downloadGuestbook();
                        return null;
                    }
                }.doInBackground(null);
            default:
                return false;
        }
    }

    private void downloadGuestbook() {
        String url = "http://ase2016-148507.appspot.com/rest/guestbook/";
        try {
            final String rawAnswer = new ClientResource(url).get().getText();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) findViewById(R.id.text_view);
                    textView.setText(rawAnswer);
                }
            });
        } catch (IOException e) {
            Toast.makeText(this.getApplicationContext(),
                    "Could not download guestbook", Toast.LENGTH_SHORT).show();
        }
    }
}
