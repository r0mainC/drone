package drone.proto204.fr.dronealerts;

import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {



    // link
    private static final String address = "http://192.168.43.226:8888/drone/getAlert.php";
    private static final String dismiss_address = "http://192.168.43.226:8888/drone/dimiss.php?id=";

    // Debug log tag.
    private static final String TAG_HTTP_URL_CONNECTION = "HTTP_URL_CONNECTION";
    // Request method GET. The value must be uppercase.
    private static final String REQUEST_METHOD_GET = "GET";

    private boolean data_ready=false;


    // variable du layout
    LinearLayout layout;

    CardView cardViews[];
    LinearLayout container[];
    LinearLayout first_raw[];
    LinearLayout second_raw[];
    TextView alert_id[];
    TextView drone_id[];
    TextView type[];
    TextView date[];
    TextView time[];
    Button dismiss[];



    // variable de traitement de la reponse
    JSONObject json_response;
    JSONArray requests;

    // timer
    Timer autoUpdate;

    // le listener de dismiss
    ButtonListener buttonListener = new ButtonListener();


    // an attribute of wrap content
    LinearLayout.LayoutParams wrap_content= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams weight_1  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
    LinearLayout.LayoutParams weight_2  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);
    LinearLayout.LayoutParams weight_3  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f);
    LinearLayout.LayoutParams weight_4  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4.0f);
    LinearLayout.LayoutParams weight_5  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f);
    LinearLayout.LayoutParams weight_6  = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 6.0f);
    LinearLayout.LayoutParams line_param= new LinearLayout.LayoutParams(200, 3);
    LinearLayout.LayoutParams card_with_margins= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams buttons_layout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // on va recuperer les layout
        layout =(LinearLayout) findViewById(R.id.layout);


        card_with_margins.setMargins(5,25,5,25);
        // recuperer le layout
      //  layout= (LinearLayout) findViewById(R.id.layout);

        // get the requests for the logged user

            getRequests();
            while (!data_ready) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            setUpLayout();

    }

    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        getRequests();
                        setUpLayout();
                    }
                });
            }
        }, 0, 1000); // updates each 40 secs
    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }


    public void setUpLayout()
    {
        int n= requests.length();
        // creation des cartes
        cardViews = new CardView[n];

        // initialisation des layout
        container = new LinearLayout[n];
        first_raw = new LinearLayout[n];
        second_raw = new LinearLayout[n];
        alert_id = new TextView[n];
        drone_id = new TextView[n];
        type= new TextView[n];
        date = new TextView[n];
        time = new TextView[n];
        dismiss = new Button[n];

        layout.removeAllViews();

        for (int i=0; i<requests.length(); i++)
        {
            try {
              createCard(requests.getJSONObject(i), i);
              layout.addView(cardViews[i], card_with_margins);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("ccc", "index "+i);
            Log.d("ccc", "size is "+n);

        }

    }

    // fonction de creations d'une carte:
      void createCard(JSONObject object, int i)
      {
          container[i]= new LinearLayout(MainActivity.this);
          first_raw[i]= new LinearLayout(MainActivity.this);
          second_raw[i]=  new LinearLayout(MainActivity.this);
          cardViews[i]= new CardView(MainActivity.this);
        alert_id[i]= new TextView(MainActivity.this);
        drone_id[i] = new TextView(MainActivity.this);
        type[i]=new TextView(MainActivity.this);
        date[i]=new TextView(MainActivity.this);
        time[i]= new TextView(MainActivity.this);
        dismiss[i] = new Button(MainActivity.this);
        dismiss[i].setOnClickListener(buttonListener);
          container[i].setOrientation(LinearLayout.VERTICAL);
          first_raw[i].setOrientation(LinearLayout.HORIZONTAL);
          second_raw[i].setOrientation(LinearLayout.HORIZONTAL);

          try {
              alert_id[i].setText("ALERTE "+ object.getString("id"));
              alert_id[i].setTextColor(Color.WHITE);
              drone_id[i].setText("DRONE "+object.get("drone"));
              type[i].setText( object.getString("type"));
              date[i].setText(object.getString("date"));
              time[i].setText(object.getString("heure"));
              dismiss[i].setText("supprimer");
              dismiss[i].setTag(object.getInt("id"));
          } catch (JSONException e) {
              e.printStackTrace();
          }

          // setting the card size
          cardViews[i].setMinimumWidth(400);
          cardViews[i].setMinimumHeight(200);
          try {
              if(object.getString("type").equals("CRITICAL"))
                cardViews[i].setBackgroundColor(getResources().getColor(R.color.CRITICAL));
              else if(object.getString("type").equals("DANGER"))
                  cardViews[i].setBackgroundColor(getResources().getColor(R.color.DANGER));
              else if(object.getString("type").equals("INFO"))
                  cardViews[i].setBackgroundColor(getResources().getColor(R.color.INFO));
              else
                  cardViews[i].setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
          } catch (JSONException e) {
              e.printStackTrace();
          }

          first_raw[i].addView(alert_id[i], weight_2);
          first_raw[i].addView(drone_id[i], weight_2);
          first_raw[i].addView(type[i], weight_1);
          second_raw[i].addView(date[i], weight_2);
          second_raw[i].addView(time[i], weight_1);

          container[i].addView(first_raw[i]);
          container[i].addView(second_raw[i]);
          container[i].addView(dismiss[i]);
          container[i].setPadding(20,20,20, 20);
          cardViews[i].addView(container[i]);

      }

    private void getRequests()
    { Log.d("myrequests", " getting the requests ");
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;
                // Read text input stream.
                InputStreamReader isReader = null;
                // Read text into buffer.
                BufferedReader bufReader = null;
                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();
                // forming the url for a GET request
                String reqUrl = address;

                try {
                    // Create a URL object use page url.
                    URL url = new URL(reqUrl);
                    // Open http connection to web server.
                    httpConn = (HttpURLConnection)url.openConnection();
                    // Set http request method to get.
                    httpConn.setRequestMethod(REQUEST_METHOD_GET);
                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);
                    // Get input stream from web url connection.
                    InputStream inputStream = httpConn.getInputStream();
                    // Create input stream reader based on url connection input stream.
                    isReader = new InputStreamReader(inputStream);
                    // Create buffered reader.
                    bufReader = new BufferedReader(isReader);
                    // Read line of text from server response.
                    String line = bufReader.readLine();
                    // Loop while return line is not null.
                    while(line != null)
                    {
                        // Append the text to string buffer.
                        readTextBuf.append(line);
                        // Continue to read text line.
                        line = bufReader.readLine();
                    }

                    // casting the http response to a string
                    String http_response= readTextBuf.toString();
                    Log.d("debug_message", http_response);

                   // parsing the http_response
                    json_response = new JSONObject(http_response);
                    // getting a json table of requests
                    requests=null;
                    requests = json_response.getJSONArray("alerts");
                    data_ready= true;


                }catch(MalformedURLException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }catch(IOException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufReader != null) {
                            bufReader.close();
                            bufReader = null;
                        }

                        if (isReader != null) {
                            isReader.close();
                            isReader = null;
                        }

                        if (httpConn != null) {
                            httpConn.disconnect();
                            httpConn = null;
                        }
                    }catch (IOException ex)
                    {
                        Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                    }
                }
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }

     class ButtonListener implements View.OnClickListener{

         @Override
         public void onClick(View v) {
            dismissRequest((int) v.getTag());

         }

     }



    private void dismissRequest(final int id)
    { Log.d("myrequests", " id ");
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;
                // Read text input stream.
                InputStreamReader isReader = null;
                // Read text into buffer.
                BufferedReader bufReader = null;
                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();
                // forming the url for a GET request
                String reqUrl = dismiss_address+id;

                try {
                    // Create a URL object use page url.
                    URL url = new URL(reqUrl);
                    // Open http connection to web server.
                    httpConn = (HttpURLConnection)url.openConnection();
                    // Set http request method to get.
                    httpConn.setRequestMethod(REQUEST_METHOD_GET);
                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);
                    // Get input stream from web url connection.
                    InputStream inputStream = httpConn.getInputStream();
                    // Create input stream reader based on url connection input stream.
                    isReader = new InputStreamReader(inputStream);
                    // Create buffered reader.
                    bufReader = new BufferedReader(isReader);
                    // Read line of text from server response.
                    String line = bufReader.readLine();
                    // Loop while return line is not null.
                    while(line != null)
                    {
                        // Append the text to string buffer.
                        readTextBuf.append(line);
                        // Continue to read text line.
                        line = bufReader.readLine();
                    }

                    // casting the http response to a string
                    String http_response= readTextBuf.toString();
                    Log.d("debug_message", http_response);




                }catch(MalformedURLException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }catch(IOException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }finally {
                    try {
                        if (bufReader != null) {
                            bufReader.close();
                            bufReader = null;
                        }

                        if (isReader != null) {
                            isReader.close();
                            isReader = null;
                        }

                        if (httpConn != null) {
                            httpConn.disconnect();
                            httpConn = null;
                        }
                    }catch (IOException ex)
                    {
                        Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                    }
                }
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }
}
