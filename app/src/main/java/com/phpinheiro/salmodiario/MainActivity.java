package com.phpinheiro.salmodiario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.phpinheiro.salmodiario.model.Salmo;
import com.phpinheiro.salmodiario.sqlite.MySQLiteHelper;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import android.graphics.Typeface;

public class MainActivity extends Activity {

    private String dataDeHoje;

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/script.otf");
        ((TextView) findViewById(R.id.salmoDoDiaTexto)).setTypeface(typeface);

        if(!isMyServiceRunning(AlarmService.class)){
            Log.v(TAG, "serviço de alarme não está rodando. Iniciando");
            Intent alarmIntent = new Intent(this, AlarmService.class);
            startService(alarmIntent);
        }
        else{
            Log.v(TAG, "serviço de alarme já está rodando.");
        }

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



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		//mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
	//	mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.pordia:
                criarSalmoDoDia();
                break;
            // action with ID action_settings was selected
            case R.id.aleatorio:
                criarSalmoAleatorio();
                break;
            case R.id.sobre:
                Intent intent = new Intent(this, SobreActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

		return false;
	}

    public void criarSalmoDoDia(){

        MySQLiteHelper db = new MySQLiteHelper(this);
        dataDeHoje = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        //dataDeHoje = "25/10/2015";
        //Log.v(TAG, "dataDeHoje = " + dataDeHoje);

        //Salmo salmoTeste = db.getSalmoPorNumero("3");
        Salmo salmoTeste = db.getSalmoPorData3(dataDeHoje);



        ((TextView) findViewById(R.id.salmoDoDiaTitulo)).setText("Salmo " + salmoTeste.getNumero() + " - " + dataDeHoje);
        ((TextView) findViewById(R.id.salmoDoDiaTexto)).setText(salmoTeste.getTexto());

    }

    public void criarSalmoAleatorio(){

        MySQLiteHelper db = new MySQLiteHelper(this);
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(150);
        while(randomInt == 0){
        	randomInt = randomGenerator.nextInt(150);
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
        updateTime.set(Calendar.MINUTE, 56);

        Intent downloader = new Intent(this, AlarmReceiver.class);
        downloader.putExtra("NOTIFICOU", "teste");
        PendingIntent recurringDownload = PendingIntent.getBroadcast(this,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringDownload);
    }

    /**
     * Verifica se o serviço já está rodando.
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}