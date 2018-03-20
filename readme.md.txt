This is an introduction
=
Part1:Adding the new icon to the application
-
I choose the origin icons because I think it's ok

Part2:Adding the blue background
-
revise shape.xml:
```java
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#31a7dc" />
    <size
        android:width="15dp"
        android:height="15dp" />
</shape>
```
some layout change:
```java
<TextView
    android:id="@+id/textView"
    android:layout_width="63dp"
    android:layout_height="60dp"
    android:background="@drawable/b
    android:gravity="center"
    android:text="mon"
    android:textAllCaps="true"
    android:textColor="#909090" />
```

Part3:Adding the refresh button.
-
some layout change:
```java
<ImageView
    android:id="@+id/refresh"
    android:layout_width="48dp"    android:layout_height="48dp"
    android:layout_alignBottom="@+id/linearLayout"
    android:layout_gravity="center"
    android:layout_marginBottom="16dp"
    android:layout_marginEnd="74dp"
    android:layout_marginRight="74dp"
    android:layout_toLeftOf="@+id/tv_temperature"
    android:layout_toStartOf="@+id/tv_temperature"
    android:onClick="btnClick"
    app:srcCompat="@drawable/available_updates"/>
 ```
 Part4:Adding the Message response
 -
 revise in Mainactivity:
 ```java
 public void btnClick(View view) {
    Calendar calendar = Calendar.getInstance();//获取系统的日期
    int year = calendar.get(Calendar.YEAR);//年
    int month = calendar.get(Calendar.MONTH);//月
    int day = calendar.get(Calendar.DAY_OF_MONTH);//日
    int day_of_week =calendar.get(Calendar.DAY_OF_WEEK);//星期
    String[] weeks = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATUR
    ((TextView) findViewById(R.id.dayoftheweek)).setText(weeks[day_of_week-1]);
    ((TextView) findViewById(R.id.tv_date)).setText((month+1)+"/"+day+"/"+year);
    new DownloadUpdate().execute();
}
private class DownloadUpdate extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        String stringUrl = "http://mpianatra.com/Courses/info.txt";
        HttpURLConnection urlConnection = null;
        BufferedReader reader;
        try {
            URL url = new URL(stringUrl);
            // Create the request to get the information from the server, and open the c
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Mainly needed for debugging
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            //The temperature
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String temperature) {
        //Update the temperature displayed
        ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
    }
}
```
Part5:Final effect
-
![](https://github.com/xpiqiu/first-/blob/master/before.png) ![](https://github.com/xpiqiu/first-/blob/master/after.png)


 
               
