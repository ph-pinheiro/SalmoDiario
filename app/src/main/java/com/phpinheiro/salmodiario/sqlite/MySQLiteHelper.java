package com.phpinheiro.salmodiario.sqlite;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.phpinheiro.salmodiario.R;
import com.phpinheiro.salmodiario.model.Salmo;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	//versao do banco - incrementar a cada alteração no banco
	private static final int DATABASE_VERSION = 13;
	
	//nome do banco
	private static final String DATABASE_NAME = "SalmosDB";
	
	private final Context fContext;
	
	private static final String[] COLUMNS = {SalmosColumns.ID, SalmosColumns.NUMERO, SalmosColumns.TITULO, SalmosColumns.TEXTO, SalmosColumns.DATA, SalmosColumns.TRECHO};

    private static final String TAG = "MySQLiteHelper";

	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		fContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_SALMOS_TABLE = "CREATE TABLE salmos ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"numero TEXT, " +
				"titulo TEXT, " +
				"texto TEXT, " +
				"data TEXT, " +
				"trecho TEXT )";
		
		db.execSQL(CREATE_SALMOS_TABLE);
		
		ContentValues valores = new ContentValues();
		Resources res = fContext.getResources();
		XmlResourceParser xml = res.getXml(R.xml.salmos_records);
		
		try{
			int eventType = xml.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				if((eventType == XmlPullParser.START_TAG) && (xml.getName().equals("record"))){
					String _numero = xml.getAttributeValue(null, SalmosColumns.NUMERO);
					String _titulo = xml.getAttributeValue(null, SalmosColumns.TITULO);
					String _texto = xml.getAttributeValue(null, SalmosColumns.TEXTO);
					String _data = xml.getAttributeValue(null, SalmosColumns.DATA);
					String _trecho = xml.getAttributeValue(null, SalmosColumns.TRECHO);
					valores.put(SalmosColumns.NUMERO, _numero);
					valores.put(SalmosColumns.TITULO, _titulo);
					valores.put(SalmosColumns.TEXTO, _texto);
					valores.put(SalmosColumns.DATA, _data);
						valores.put(SalmosColumns.TRECHO, _trecho);
					db.insert(SalmosColumns.TABLENAME, null, valores);
				}
				eventType = xml.next();
			}
		}
		catch (XmlPullParserException e)
        {       
			
        }
        catch (IOException e)
        {
             
        }           
        finally
        {           
            //Close the xml file
            xml.close();
        }

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS salmos");
		
		this.onCreate(db);
	}
	
	public Salmo getSalmoPorNumero(String titulo){
	
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(SalmosColumns.TABLENAME, //tabela
				COLUMNS, //colunas
				"numero = ?", //sele��o
				new String[]{String.valueOf(titulo)}, //argumentos
				null, //group by
				null, //having
				null, //order by
				null); //limit
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		
		Salmo salmo = new Salmo();
		salmo.setId(Integer.parseInt(cursor.getString(0)));
		salmo.setNumero(cursor.getString(1));
		salmo.setTitulo(cursor.getString(2));
		salmo.setTexto(cursor.getString(3));
		salmo.setData(cursor.getString(4));
		salmo.setTrecho(cursor.getString(5));
		
		return salmo;
	}
	
	public Salmo getSalmoPorData(String data){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SalmosColumns.TABLENAME, //tabela
                COLUMNS, //colunas
                "data LIKE ?", //sele��o
                new String[]{"'%"+data+"%'" }, //argumentos
                null, //group by
                null, //having
                null, //order by
                null); //limit

        if(cursor != null){
            cursor.moveToFirst();
        }

        Salmo salmo = new Salmo();
        salmo.setId(Integer.parseInt(cursor.getString(0)));
        salmo.setNumero(cursor.getString(1));
        salmo.setTitulo(cursor.getString(2));
        salmo.setTexto(cursor.getString(3));
        salmo.setData(cursor.getString(4));
		salmo.setTrecho(cursor.getString(5));

        return salmo;
    }

    public Salmo getSalmoPorData2(String data){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[1];
        args[0] = "%"+data+"%";
        Cursor cursor = db.rawQuery("SELECT * FROM salmos WHERE data like ?", args);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Salmo salmo = new Salmo();
        salmo.setId(Integer.parseInt(cursor.getString(0)));
        salmo.setNumero(cursor.getString(1));
        salmo.setTitulo(cursor.getString(2));
        salmo.setTexto(cursor.getString(3));
        salmo.setData(cursor.getString(4));
		salmo.setTrecho(cursor.getString(5));

        return salmo;
    }

    public Salmo getSalmoPorData3(String data){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[1];
        args[0] = "%"+data+"%";
        Cursor cursor = db.rawQuery("SELECT * FROM salmos WHERE data like ?", args);

        Salmo salmo = new Salmo();

        boolean flag = true;

        if(cursor.getCount()==0){
            flag = false;
        }

        if((cursor != null) && (flag == true)){
            cursor.moveToFirst();

            salmo.setId(Integer.parseInt(cursor.getString(0)));
            salmo.setNumero(cursor.getString(1));
            salmo.setTitulo(cursor.getString(2));
            salmo.setTexto(cursor.getString(3));
            salmo.setData(cursor.getString(4));
			salmo.setTrecho(cursor.getString(5));
        }
        else{
            salmo.setId(0);
            salmo.setNumero("0");
            salmo.setTitulo("ERRO");
            salmo.setTexto("Houve um erro na base de dados de salmo. Isso pode ocorrer devido à falta de atualização. Por favor aguarde.");
            salmo.setData("");
        }

        return salmo;
    }

}
