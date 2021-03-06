package de.tum.in.ase.guestbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
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
                downloadGuestbook();
            default:
                return false;
        }
    }

    private void downloadGuestbook() {
        String url = "http://ase2016-148507.appspot.com/rest/guestbook/";
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView textView = (TextView) findViewById(R.id.text_view);

        // Construct request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(parseContent(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    private String parseContent(String response) {
        StringBuilder result = new StringBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList entries = (NodeList) xpath.evaluate("//*/content/text()",
                    doc, XPathConstants.NODESET);
            for (int i = 0; i < entries.getLength(); i++) {
                result.append(entries.item(i).getNodeValue());
                result.append("\n\n");
            }
        } catch (ParserConfigurationException
                | XPathExpressionException
                | IOException
                | SAXException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
