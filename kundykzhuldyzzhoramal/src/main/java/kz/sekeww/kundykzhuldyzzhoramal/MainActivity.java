package kz.sekeww.kundykzhuldyzzhoramal;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ListView;
        import android.widget.Toast;




public class MainActivity extends Activity {


    ListView list;
    String[] itemname ={
            "Тоқты",
            "Торпақ",
            "Егіздер",
            "Шаян",
            "Арыстан",
            "Бикеш",
            "Таразы",
            "Сарышаян",
            "Мерген",
            "Тауешкі",
            "Суқұйғыш",
            "Балықтар"

    };

    Integer[] imgid={
            R.drawable.apic1,
            R.drawable.apic2,
            R.drawable.apic3,
            R.drawable.apic4,
            R.drawable.apic5,
            R.drawable.apic6,
            R.drawable.apic7,
            R.drawable.apic8,
            R.drawable.apic9,
            R.drawable.apic10,
            R.drawable.apic11,
            R.drawable.apic12
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setTextFilterEnabled(true);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, activity_detail.class);

                intent.putExtra("title", itemname[position]);
                switch (position) {
                    case 0:
                        intent.putExtra("url", "http://www.google.com");
                        break;
                    case 1:
                        intent.putExtra("url", "http://www.yahoo.com");
                        break;
                    case 2:
                        intent.putExtra("url", "http://www.bing.com");
                        break;
                }
                //запускаем вторую активность
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
