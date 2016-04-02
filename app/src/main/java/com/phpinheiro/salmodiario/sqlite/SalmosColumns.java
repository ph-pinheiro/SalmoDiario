package com.phpinheiro.salmodiario.sqlite;

import android.provider.BaseColumns;

public class SalmosColumns implements BaseColumns{

	private SalmosColumns(){
		
	}
	
	public static final String TABLENAME = "salmos";
	
	public static final String ID = "id";
	
	public static final String NUMERO = "numero";
	
	public static final String TITULO = "titulo";
	
	public static final String TEXTO = "texto";
	
	public static final String DATA = "data";
	
}
