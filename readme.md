This is an introduction
=
Part1:Use the data from Internet
-
revise MainActivity:
```java
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
```
Part2:judge if network is connecting or not
-
```java
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
```
part3:final  effect
-
![](https://github.com/xpiqiu/weather-application/blob/master/Hasnet.png)![](https://github.com/xpiqiu/weatherapplication/blob/master/NoNet.png)
