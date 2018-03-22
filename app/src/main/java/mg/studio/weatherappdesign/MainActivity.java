package mg.studio.weatherappdesign;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Net(this))
            new DateUpdate().execute();
        else
            Toast.makeText(this, "网络不可用,无法连接", Toast.LENGTH_SHORT).show();
    }
    public boolean Net(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                return true;
            }
        }
        return false;
    }
    public void btnClick(View view) {

        if(Net(this))
            new DateUpdate().execute();
        else
            Toast.makeText(this, "网络不可用,无法连接", Toast.LENGTH_SHORT).show();
    }
    private class DateUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings){
            String str="";
            String response="";
            final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=101040100";
            HttpURLConnection urlConnection = null;
            Calendar date = Calendar.getInstance();
            int day = date.get(Calendar.DAY_OF_MONTH);

            try {
                URL url = new URL(address);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(8000);
                urlConnection.setReadTimeout(8000);
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer s = new StringBuffer();

                while((str=reader.readLine())!=null)
                    s.append(str);

                response = s.toString();
                Log.d("response",response);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            StringBuilder dataBack = new StringBuilder();

            //解析XML数据
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));
                int eventType = xmlPullParser.getEventType();
                String date2= "";
                String high = "";
                String low = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        //  开始解析某个结点
                        case XmlPullParser.START_TAG: {
                            if ("date".equals(nodeName)) {
                                date2 = xmlPullParser.nextText();
                            } else if ("high".equals(nodeName)) {
                                high = xmlPullParser.nextText();
                            } else if ("low".equals(nodeName)) {
                                low = xmlPullParser.nextText();
                            }

                            break;
                        }

                        default:
                            break;
                    }

                    if(date2.contains( day+"日") && !high.isEmpty() && !low.isEmpty()){
                        dataBack.append(date+"/");
                        dataBack.append(high+"/");
                        dataBack.append(low);
                        break;
                    }
                    eventType = xmlPullParser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String data = dataBack.toString();

            return data;
        }
        @Override

        protected void onPostExecute(String data) {
            Calendar calendar = Calendar.getInstance();//获取系统的日期
            //字符串切割
            String[] temperature = data.split("/");

            //当日的最高温和最低温
            String highTemp = temperature[1].substring(3,5);
            String lowTemp  = temperature[2].substring(3,5);
            int year = calendar.get(Calendar.YEAR);//年
            int month = calendar.get(Calendar.MONTH);//月
            int day = calendar.get(Calendar.DAY_OF_MONTH);//日
            int day_of_week =calendar.get(Calendar.DAY_OF_WEEK);//星期
            String[] weeks = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
            ((TextView) findViewById(R.id.dayoftheweek)).setText(weeks[day_of_week-1]);
            ((TextView) findViewById(R.id.tv_date)).setText((month+1)+"/"+day+"/"+year);
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(lowTemp+"~"+highTemp);
        }
    }
}