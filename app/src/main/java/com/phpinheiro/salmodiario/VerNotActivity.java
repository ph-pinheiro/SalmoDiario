package com.phpinheiro.salmodiario;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.phpinheiro.salmodiario.model.Salmo;
import com.phpinheiro.salmodiario.sqlite.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VerNotActivity extends Activity {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;

    private static final String TAG = "MainActivity";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = "Salmo do Dia";
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add Drawer Item to dataList
		dataList.add(new DrawerItem("Salmo do Dia", R.drawable.ic_action_email));
		dataList.add(new DrawerItem("Buscar Salmo", R.drawable.ic_action_search));
		//dataList.add(new DrawerItem("Cloud", R.drawable.ic_action_cloud));
		//dataList.add(new DrawerItem("Camara", R.drawable.ic_action_camera));
		//dataList.add(new DrawerItem("Video", R.drawable.ic_action_video));
		//dataList.add(new DrawerItem("Groups", R.drawable.ic_action_group));
		//dataList.add(new DrawerItem("Import & Export",
		//		R.drawable.ic_action_import_export));
		dataList.add(new DrawerItem("Sobre", R.drawable.ic_action_about));
		dataList.add(new DrawerItem("Configurações", R.drawable.ic_action_settings));
		//dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			SelectItem(0);
		}
		
        //setRecurringAlarm();
		criarSalmoDoDia();

    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        this.onCreate(savedInstanceState);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.share, menu);

        MenuItem shareItem = (MenuItem) menu.findItem(R.id.menu_item_share);
        ShareActionProvider mShare = (ShareActionProvider) shareItem.getActionProvider();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String textoCompartilhado = "";
        textoCompartilhado += ((TextView) findViewById(R.id.salmoDoDiaTitulo)).getText();
        textoCompartilhado += "\n\n";
        textoCompartilhado += ((TextView) findViewById(R.id.salmoDoDiaTexto)).getText();
        textoCompartilhado += "\n\n\n";
        textoCompartilhado += "Compartilhado pelo aplicativo 'Salmo Diário'";
        shareIntent.putExtra(Intent.EXTRA_TEXT, textoCompartilhado);
        mShare.setShareIntent(shareIntent);

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {
		case 0:  //Salmo do dia - refresh
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 1:  //buscar salmo
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 2:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 3:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 4:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 5:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 6:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 7:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 8:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 9:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 10:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 11:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 12:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		default:
			break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		
		if(title == "Salmo do Dia"){
			
		}
		else if(title == "Buscar Salmo"){
			Intent intent = new Intent(this, BuscarActivity.class);
			startActivity(intent);
		}
		else if(title == "Sobre"){
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
			
		}else if(title == "Configurações"){
			mTitle = title;
			getActionBar().setTitle(mTitle);
		}
        else{
            mTitle = title;
            //getActionBar().setTitle(mTitle);
        }
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;
		}

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.pordia:
                criarSalmoDoDia();
                break;
            // action with ID action_settings was selected
            case R.id.aleatorio:
                criarSalmoAleatorio();
                break;
            default:
                break;
        }

		return false;
	}

    public void criarSalmoDoDia(){

        MySQLiteHelper db = new MySQLiteHelper(this);
        String dataDeHoje = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.v(TAG, "dataDeHoje = " + dataDeHoje);

        //Salmo salmoTeste = db.getSalmoPorNumero("3");
        Salmo salmoTeste = db.getSalmoPorData2(dataDeHoje);

        ((TextView) findViewById(R.id.salmoDoDiaTitulo)).setText("Salmo " + salmoTeste.getNumero() + " - " + salmoTeste.getData());
        ((TextView) findViewById(R.id.salmoDoDiaTexto)).setText(salmoTeste.getTexto());

    }

    public void criarSalmoAleatorio(){

        MySQLiteHelper db = new MySQLiteHelper(this);
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        while(randomInt == 0){
        	randomInt = randomGenerator.nextInt(100);
        }
        Salmo salmoTeste = db.getSalmoPorNumero(String.valueOf(randomInt));

        ((TextView) findViewById(R.id.salmoDoDiaTitulo)).setText("Salmo " + salmoTeste.getNumero());
        ((TextView) findViewById(R.id.salmoDoDiaTexto)).setText(salmoTeste.getTexto());

    }

    private void setRecurringAlarm() {

        // we know mobiletuts updates at right around 1130 GMT.
        // let's grab new stuff at around 11:45 GMT, inexactly
        Calendar updateTime = Calendar.getInstance();
       // updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        updateTime.set(Calendar.HOUR_OF_DAY, 12);
        updateTime.set(Calendar.MINUTE, 35);

        Intent downloader = new Intent(this, AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(this,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringDownload);
    }

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectItem(position);

		}
	}
}